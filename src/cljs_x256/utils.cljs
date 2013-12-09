(ns cljs-x256.utils
  (:use [cljs-x256.core :only [to-hex from-hex to-rgb from-rgb hex->dec hex->rgb]]))

(declare part)
(declare colorness)

(defn redness [x] (colorness x :red))
(defn greeness [x] (colorness x :green))
(defn blueness [x] (colorness x :blue))

(defn brightness
  "http://georeference.org/doc/colors_as_hue_saturation_and_brightness.htm"
  [h]
  (let [rgb (hex->rgb h)
        M (apply max rgb)
        m (apply min rgb)]
    (/ (+ M m) 2)))

(defn saturation
  [h]
  (let [rgb (hex->rgb h)
        b (brightness h)
        M (apply max rgb)
        m (apply min rgb)]
    (cond
      (apply = rgb) 0
      (< b 128) (* 255 (/ (- M m) (+ M m)))
      :else (* 255 (/ (- M m) (- 511 (+ M m)))))))

(defn more-blue [] nil)
(defn more-red [] nil)
(defn more-green [] nil)

(defn brighten
  []
  nil)
(defn darken
  []
  nil)

(defn saturate
  []
  nil)
(defn desaturate
  []
  nil)

(defn- part
  [hex part]
  (let [range (case part :red [0 2] :green [2 4] :blue [4 6] nil)]
    (if (nil? range)
      nil
      (apply subs hex range))))

(defn- colorness
  [x color]
  (let [hex (to-hex x)
        color (-> (part hex color) hex->dec)]
    (/ color 255)))

(defn- more-color
  [x color]
  nil)
