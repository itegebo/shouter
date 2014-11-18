(ns shouter.system
  (:require [ring.adapter.jetty :as ring])
  (:require [com.stuartsierra.component :as component])
  (:require [shouter.web :as web]
            [shouter.models.migration :as schema])
  (:import [org.h2.tools Server]))

;;; Component: Lessons Learned
;;
;; * Key renaming in component/using is confusing
;; * Components must be returned from Lifecycle methods
;; * Logging should be built-in
;; * Record-wrapping becomes boilerplate
;; * So does system start/stop seems obvious as Lifecycle
;; * It's not clear that Lifecycle methods are synchronous
;; * I'll need to support external services as components
;; * It's super annoying to be left in an inconsistent state when
;;   something fails to start/stop.

(defrecord H2Console [server]
  component/Lifecycle
  (start [this] (.start (:server this)) this)
  (stop [this] (.stop (:server this)) this))

(defn h2console [port]
  (let [varargs (into-array String ["-webPort" (str port)])]
    (H2Console. (Server/createWebServer varargs))))

(defrecord H2TcpServer [server host port name]
  component/Lifecycle
  (start [this] (.start (:server this)) this)
  (stop [this] (.stop (:server this)) this))

;; TODO Add to a new protocol for all DBs
;; TODO Support more features through URL config
(defn jdbc-url [s]
  (str "jdbc:h2:tcp://" (:host s) ":" (:port s) "/" "mem:" (:name s)))

(defn h2tcpserver [host port name]
  (let [varargs (into-array String ["-tcpPort" (str port)])]
    (H2TcpServer. (Server/createTcpServer varargs) host port name)))

(declare current-dev-jdbc-url)

(defrecord Jetty [server app port]
  component/Lifecycle
  (start [this]
    (when (not (and (:server this)
                    (.isRunning (:server this))))
      (schema/migrate (current-dev-jdbc-url))
      (assoc this :server
             (ring/run-jetty app {:port port
                                  :join? false}))))
  (stop [this] (.stop (:server this)) this))

;; FIX This is not going to work, we need to separate creation from
;; starting.
(defn jetty [app port]
  (Jetty. nil app port))

(defn system []
  (component/system-map
   :h2console (h2console 7777)
   :h2 (h2tcpserver "localhost" 7001 "shouts")
   :jetty (component/using
           (jetty web/application 7000)
           [:h2])))

(def dev-system (system))

(defn start-dev-system []
  (alter-var-root #'dev-system component/start))

(defn stop-dev-system []
  (alter-var-root #'dev-system component/stop))

(defn current-dev-jdbc-url []
  (jdbc-url (:h2 dev-system)))
