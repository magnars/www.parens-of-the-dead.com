(defproject parens "0.1.0-SNAPSHOT"
  :description "The code that generates parens-of-the-dead.com"
  :url "http://www.parens-of-the-dead.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [optimus "2022-02-13"]
                 [stasis "2.5.1"]
                 [ring "1.9.6"]
                 [prone "2021-04-23"]
                 [helpful-loader "0.1.1"]
                 [org.clojure/data.xml "0.0.8"]]
  :jvm-opts ["-Djava.awt.headless=true"]
  :ring {:handler parens.web/app
         :port 3334}
  :aliases {"build-site" ["run" "-m" "parens.web/export"]}
  :profiles {:dev {:plugins [[lein-ring "0.12.6"]]}})
