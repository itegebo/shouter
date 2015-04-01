(ns shouter.models.shout
  (:require [clojure.java.jdbc :as sql]
            [shouter.system.config :as config]))

(def spec (or (System/getenv "DATABASE_URL")
              ;;"postgresql://localhost:5432/shouter"
              {:classname "org.h2.Driver"
               ;; there's also a shell
               ;; http://www.h2database.com/html/tutorial.html#shell_tool
               ;; Scripts and tracefiles may be used to bootstrap
               ;; migration scripts
               ;; NOTE We'll probably want to create *both* a tcp and
               ;; web server to take advantage of both the console and
               ;; multiple connections.
               ;; http://www.h2database.com/html/tutorial.html#using_server
               ;; TODO Add logging
               ;; http://www.h2database.com/html/features.html#database_url
               ;; http://www.h2database.com/html/features.html#in_memory_databases
               ;; http://www.h2database.com/html/features.html#other_logging
               :subprotocol "h2:mem"
               :subname "shout"
               }
              ))

(defn all ([] (all (config/jdbc-url)))
  ([jdbc-url]
     (into []
           (sql/query jdbc-url
                      ;; assumes id increases with created_at
                      ["select * from shouts order by id desc limit 128"]))))

(defn create
  ([shout] (create (config/jdbc-url) shout))
  ([jdbc-url shout]
     (sql/insert! jdbc-url :shouts [:body] [shout])))
