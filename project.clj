(defproject record-parser "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [compojure "1.6.2"]
                 [cheshire "5.10.1"]
                 [ring/ring-mock "0.4.0"]]
  :repl-options {:init-ns record-parser.core})
