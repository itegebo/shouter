(ns shouter.models.migration
  (:require [clojure.java.jdbc :as sql]))

(defn migrated? [jdbc-url]
  (-> (sql/query jdbc-url
                 ;; needed all caps for SHOUTS - need portable SQL
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='SHOUTS'")])
      first
      (get (keyword "count(*)"))
      pos?))

(defn migrate [jdbc-url]
  (when (not (migrated? jdbc-url))
    (print (str "Creating database structure...\n"
                "in " jdbc-url))
    (flush)
    (sql/db-do-commands jdbc-url
                        (sql/create-table-ddl
                         :shouts
                         [:id :serial "PRIMARY KEY"]
                         ;; controller puts additional constraints
                         ;; non-blank, less than 512 chars
                         [:body :varchar "NOT NULL"]
                         ;; what can we say about CURRENT_TIMESTAMP?
                         ;; i.e. timezone, format?
                         [:created_at :timestamp
                          "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]))
    (println " done")))

;; http://www.h2database.com/javadoc/org/h2/tools/Server.html
;; (def h2 (org.h2.tools.Server/createWebServer (into-array String ["-webPort" "7777"])))
