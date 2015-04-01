(ns shouter.system.config)

(defn env [] :dev)

;; I believe these environment names should directly correspond to
;; lein's profiles defined in project.clj
(def envs
  {:dev {:h2 {:host "127.0.1.1" ; debian /etc/hosts bug
              :port 7001
              :name "shouts"}
         :h2console {:port 7777}
         :http {:port 7000}}})

(defn current []
  (envs (env)))

(defn dev [] (envs :dev))

;; TODO Add to a new protocol for all DBs
;; TODO Support more features through URL config
(defn jdbc-url
  ([] (jdbc-url (:h2 (current))))
  ([host port name]
     (str "jdbc:h2:tcp://" host ":" port "/" "mem:" name
          ;; enable SL4J
          ";TRACE_LEVEL_FILE=4"
          ;; keep db open between connections
          ";DB_CLOSE_DELAY=-1"))
  ([{:keys [host port name]}]
     (jdbc-url host port name)))
