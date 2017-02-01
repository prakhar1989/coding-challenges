; Coding challenge 2 - https://www.youtube.com/watch?v=LG8ZK-rRkXo
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn new-particle [x y]
  {:pos [x y] :vel [0 -2] :acc [0 0]})

(defn update-particle [p]
  (let [vel (map + (:vel p) (:acc p))
        pos (map + (:pos p) vel)]
    ;; reset the acceleration to zero
    {:pos pos :vel vel :acc [0 0]}))

(defn apply-force [p f]
  (assoc p :acc (map + (:acc p) f)))

(defn draw-particle [p]
  (let [[x y] (:pos p)]
    (q/point x y)))

(defn setup [] 
  (q/stroke 255)
  (q/stroke-weight 4)
  [(new-particle (rand-int width) height)])

(defn update-state [state] 
  (map update-particle state))

(defn draw-state [state]
  (q/background 51)
  (doseq [p state]
    (draw-particle p)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
