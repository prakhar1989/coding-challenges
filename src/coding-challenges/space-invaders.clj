; Coding challenge 5 - https://www.youtube.com/watch?v=biN3v3ef-Y0
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

;; --- SHIP ---
(defn get-ship []
  {:x (/ width 2)})

(defn draw-ship [ship]
  (q/fill 255)
  (q/no-stroke)
  (q/rect (:x ship) (- height 20) 20 20))

;; --- SKETCH ---
(defn setup []
  {:ship (get-ship)})
 
(defn update-state [state] 
  state)

(defn draw-state [state]
  (let [ship (:ship state)]
    (q/background 51)
    (draw-ship ship)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
