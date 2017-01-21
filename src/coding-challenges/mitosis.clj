; Coding challenge 6 - https://www.youtube.com/watch?v=jxGS3fKPKJA
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn get-cell []
  (let [margin 100]
    {:pos [(rand-between margin (- width margin)) 
           (rand-between margin (- height margin))]
     :r 80
     :color [(rand-between 100 255) 0 (rand-between 100 255)]}))

(defn move-cell [cell] 
  (let [[x y] (:pos cell)]
    {:r (:r cell) :color (:color cell)
     :pos [(+ x (q/random -1 1)) 
           (+ y (q/random -1 1))]}))

(defn show-cell [cell]
  (let [[x y] (:pos cell)
        r     (:r cell)]
    (apply q/fill (:color cell))
    (q/no-stroke)
    (q/ellipse x y r r)))

(defn setup [] 
  (get-cell))

(defn update-state [state]
  (move-cell state))

(defn draw-state [state] 
  (q/background 51)
  (show-cell state))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
