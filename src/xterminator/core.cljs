(ns xterminator.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:use-macros [dommy.macros :only [node sel sel1]])
  (:require [cljs-x256.core :as x256]
            [cljs-x256.utils :as utils]
            [clojure.string]
            [cljs.core.async :refer [put! chan <!]]
            [dommy.core :as dom]))

(declare sanitize)

(defn log [x]
  (. js/console (log (clj->js x))))

(defn listen
  "utility fn for creating a channel to listen for events"
  [el type]
  (let [out (chan)] ;; create a channel
    (dom/listen! el type ;; listen for events on el of type `type`
               (fn [e] (put! out e))) ;; put! events into the channel
    out)) ;; return the channel

(defn xterminate!
  [hex]
  (let [hex-side (node (sel1 :#hex))
        xterm-side (node (sel1 :#xterm))
        form (sel1 :.form-group)
        input (node (sel1 :input))
        xterm (x256/from-hex hex)
        xterm-hex (x256/to-hex xterm)
        hex-h1-color (if (> (utils/brightness hex) 127) "black" "white")
        xterm-h1-color (if (> (utils/brightness xterm-hex) 127) "black" "white")]
    (dom/remove-class! form "has-error")
    (dom/set-value! input hex)
    (dom/set-style! hex-side :background-color hex)
    (dom/set-style! xterm-side :background-color xterm-hex)
    (-> (sel1 hex-side :h1)
        (dom/set-text! hex)
        (dom/set-style! :color hex-h1-color))
    (-> (sel1 xterm-side :h1)
        (dom/set-text! xterm-hex)
        (dom/set-style! :color xterm-h1-color))
    (-> (sel1 xterm-side :h2)
        (dom/set-text! xterm)
        (dom/set-style! :color xterm-h1-color))))

(defn invalid-input []
  (-> (sel1 :.form-group)
      (dom/add-class! "has-error")))

(defn listen-exterminate []
  (let [xterminate (sel1 :#xterminate)
        clicks (listen xterminate :click)
        key-press (listen (sel1 :input) :keypress)] ;; create channel that listens for click events
    (go (while true
          (let [hex (dom/value (node (sel1 :input)))
                hex (sanitize hex)
                [v c] (alts! [clicks key-press])] ;; listen for events on the 2 channels (omg)
            (if (or (= (.-type v) "click")
                    (and (= (.-type v) "keypress")
                         (= (.-keyCode v) 13)))
              (if (not (clojure.string/blank? hex))
                (xterminate! hex)
                (invalid-input))))))))

(defn listen-random []
  (let [random (sel1 :#random)
        clicks (listen random :click)] ;; create channel that listens for click events
    (go (while true
          (let [hex (rand-int 16777216)
                hex (sanitize (x256/dec->hex hex))
                e (<! clicks)]
            (if (not (clojure.string/blank? hex))
              (xterminate! hex)
              (invalid-input)))))))

(defn- sanitize
  [hex]
  (cond
    (= (count hex) 6) (clojure.string/upper-case hex)
    (= (count hex) 3) (clojure.string/upper-case (clojure.string/replace hex #"(.)" "$1$1"))
    ;; TODO: only allow 0-F
    :else nil))

(defn random-color []
  (let [hex (rand-nth ["1abc9c" "2ecc71" "3498db" "9b59b6" "34495e"
                       "f39c12" "d35400" "c0392b" "bdc3c7"])]
    (xterminate! (sanitize hex))))

(defn init []
  (random-color)
  (listen-random)
  (listen-exterminate))

(init)
