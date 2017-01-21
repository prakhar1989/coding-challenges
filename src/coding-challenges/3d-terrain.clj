; Coding challenge 11 - https://www.youtube.com/watch?v=IKB1hWWedMk
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)
(def w 2000)
(def h 1600)
(def scl 20)
(def cols (/ w scl))
(def rows (/ h scl))

(defn get-row [yoff]
  (mapv #(q/map-range (q/noise %2 yoff) 0 1 -100 100)
       (range cols)
       (iterate #(+ % 0.2) 0)))

(defn gen-terrain [delta]
  (mapv #(get-row %2)
        (range rows)
        (iterate #(+ % 0.2) delta)))

(defn setup []
  {:terrain (gen-terrain 0) :flying-speed 0})

(defn update-state [state]
  (let [speed (:flying-speed state)]
    {:terrain (gen-terrain speed)
     :flying-speed (- speed 0.1)}))

(defn render-terrain [terrain]
  (doseq [row (range (dec rows))]
    (q/begin-shape :triangle-strip)
    (doseq [col (range cols)]
      (q/vertex (* col scl) (* row scl) (get-in terrain [row col]))
      (q/vertex (* col scl) (* (inc row) scl) (get-in terrain [(inc row) col])))
    (q/end-shape)))

(defn draw-state [state]
  (q/background 0)
  (q/stroke 255)
  (q/no-fill)
  (q/translate (/ width 2) (/ height 2))
  (q/rotate-x (/ q/PI 3))
  (q/translate (/ (- w) 2) (/ (- h) 2))
  (render-terrain (:terrain state)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :renderer :p3d
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
