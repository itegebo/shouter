(ns shouter.system
  (:require [com.stuartsierra.component :as component])
  (:require [shouter.system.web :as web])
  (:require [shouter.system.db :as db])
  (:require [shouter.system.config :as config]))

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


(defn system [cfg]
  (component/system-map
   :h2console (db/h2console (get-in cfg [:h2console :port]))
   :h2 (db/h2tcpserver (get-in cfg [:h2 :port]))
   ;; :jetty (component/using
   ;;         (web/jetty (get-in cfg [:http :port]))
   ;;         [:h2])
   ;; :aleph-http (component/using

   ;;              (web/aleph-http (get-in cfg [:http :port]))
   ;;              [:h2])
   :immutant (component/using
                (web/immutant (get-in cfg [:http :port]))
                [:h2])
   ))

(def dev-system (system (config/dev)))

(defn start-dev-system []
  (alter-var-root #'dev-system component/start))

(defn stop-dev-system []
  (alter-var-root #'dev-system component/stop))

