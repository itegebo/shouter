(defproject shouter "0.0.1"
  :description "Shouter app"
  :url "http://github.com/abedra/shouter"
  :min-lein-version "2.0.0"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [com.h2database/h2 "1.4.182"]
                 [ring/ring-jetty-adapter "1.3.1"]
                 [compojure "1.2.1"]
                 [hiccup "1.0.5"]
                 [com.stuartsierra/component "0.2.2"]]
  :main ^:skip-aot shouter.web
  :uberjar-name "shouter-standalone.jar"
  :profiles {:uberjar {:aot :all}})
