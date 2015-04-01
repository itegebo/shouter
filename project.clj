(defproject shouter "0.0.1"
  :description "Shouter app"
  :url "http://github.com/abedra/shouter"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]

                 [org.clojure/java.jdbc "0.3.6"]
                 [com.h2database/h2 "1.4.182"]

                 [compojure "1.2.1"]
                 [hiccup "1.0.5"]

                 [com.stuartsierra/component "0.2.2"]

                 ;; [ch.qos.logback/logback-classic "1.1.2"]
                 ;; [ch.qos.logback/logback-core "1.1.2"]
                 ;; [org.slf4j/slf4j-api "1.7.7"]
                 ;; [org.slf4j/jcl-over-slf4j "1.7.7"]
                 ;; [org.slf4j/log4j-over-slf4j "1.7.7"]
                 ;; [org.slf4j/jul-to-slf4j "1.7.7"]

                 ;; [com.taoensso/timbre "3.3.1"]

                 [ring/ring-jetty-adapter "1.3.1"]

                 ;; [aleph "0.4.0-alpha9"]

                 [org.immutant/web "2.0.0-beta1"]
                 ]
  :jvm-opts ["-noverify" "-XX:PermSize=128m"]
  :main ^:skip-aot shouter.web
  :uberjar-name "shouter-standalone.jar"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies
                   [[ring/ring-devel "1.3.2"]]}})
