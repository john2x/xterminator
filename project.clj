(defproject xterminator "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2080"]
                 [org.clojure/core.async "0.1.256.0-1bf8cf-alpha"]
                 [prismatic/dommy "0.1.1"]]

  :plugins [[lein-cljsbuild "1.0.0"]]

  :source-paths ["src"]

  :cljsbuild { 
    :builds [{:id "xterminator"
              :source-paths ["src"]
              :compiler {
                :output-to "xterminator.js"
                :output-dir "out"
                :optimizations :advanced
                }}]})
