; Coding challenge 7-9 - https://www.youtube.com/watch?v=l8SiJ-RmeHU
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn get-planet [radius distance]
  {:r radius :d distance :theta 0.0 :moons []})

(defn spawn-moons 
  [{:keys [r d] :as planet} total]
  (let [moons (repeat total
                      (get-planet (* 0.5 r)
                                  (rand-between 100 200)))]
    (assoc planet :moons moons)))

(defn setup [] 
  {:sun (get-planet 100 0)})

(defn update-state [state] state)

(defn draw-planet [{:keys [r d]}]
  (let [s (* 2 r)]
    (q/fill 255)
    (q/ellipse d d s s)))

(defn draw-state [state] 
  (q/background 0)
  (q/with-translation [(/ width 2) (/ height 2)]
    (draw-planet (:sun state))))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
