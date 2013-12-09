(ns com.john2x.cljs.utils.core
  (:require [clojure.set]
            [clojure.string]))

(defn to-js
  "Convert Clojure(script) datastructure into native
   Javascript object."
  [x]
  x)

(defn log [x]
  (. js/console (log (to-js x))))

