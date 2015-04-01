(ns shouter.system.db
  (:require [com.stuartsierra.component :as component])
  (:require [shouter.models.migration :as schema])
  (:import [org.h2.tools Server])
  (:require [shouter.system.config :as config]))


(defrecord H2TcpServer [server]
  component/Lifecycle
  (start [this]
    (.start (:server this))
    (schema/migrate (config/jdbc-url))
    this)
  (stop [this] (.stop (:server this)) this))

(defrecord H2Console [server]
  component/Lifecycle
  (start [this] (.start (:server this)) this)
  (stop [this] (.stop (:server this)) this))

(defn h2console [port]
  (let [varargs (into-array String ["-webPort" (str port)])]
    (H2Console. (Server/createWebServer varargs))))

(defn h2tcpserver [port]
  (let [varargs (into-array String ["-tcpPort" (str port)])]
    (H2TcpServer. (Server/createTcpServer varargs))))
