(ns shouter.system.web
  (:require [ring.adapter.jetty :as ring])
  (:require [shouter.web :as web])
  (:require [shouter.system.config :as cfg])
  (:require [com.stuartsierra.component :as component])
  ;;(:require [aleph.http :as http])
  ;;(:require [aleph.netty :as netty])
  ;;(:import [io.netty.util.internal.logging InternalLoggerFactory Slf4JLoggerFactory])
  (:require [immutant.web :as imm])) 

(defrecord Jetty [server app port]
  component/Lifecycle
  (start [this]
    (when (not (and (:server this)
                    (.isRunning (:server this))))
      (println (str "Running jetty on " port))
      (assoc this :server
             (ring/run-jetty app {:port port
                                  :join? false}))))
  (stop [this] (.stop (:server this)) this))

(defn jetty [port]
  (Jetty. nil web/application port))

;; (defrecord AlephHttp [server app port]
;;   component/Lifecycle
;;   (start [this]
;;     (when-not (:server this)
;;       (InternalLoggerFactory/setDefaultFactory (Slf4JLoggerFactory.))
;;       (assoc this :server
;;              (http/start-server app {:port port}))))
;;   (stop [this]
;;     (.close (:server this))
;;     this))

;; (defn aleph-http [port]
;;   (AlephHttp. nil web/application port))

(defrecord Immutant [server app port]
  component/Lifecycle
  (start [this]
    (if-not (:server this)
      (do
        (println (str "running immutant on " port))
        (assoc this :server
               (if (= :dev (cfg/current))
                 (imm/run-dmc app {:port port})
                 (imm/run app {:port port}))))
      (do
        (println (str "already running immutant on " port))
        this)))
  (stop [this]
    (if (:server this)
      (do
        (println "stopping immutant on " port)
        (.close (:server this))
        this)
      (do
        (println ("immutant not running on " port))
        this))))

(defn immutant [port]
  (Immutant. nil web/application port))
