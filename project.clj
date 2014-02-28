(defproject riemann-http-proxy "0.1.0-SNAPSHOT"
  :description "Riemann HTTP proxy"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure    "1.5.1"]
                 [org.clojure/tools.cli  "0.3.1"]
                 [riemann-clojure-client "0.2.9"]
                 [http-kit               "2.1.16"]
                 [byte-streams           "0.1.9"] ]
  :main riemann-http-proxy.main)
