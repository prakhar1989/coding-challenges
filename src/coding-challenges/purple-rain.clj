; Coding challenge 4 - https://www.youtube.com/watch?v=KkyIDI6rQJI
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 640)
(def height 360)
(def ndrops 500)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn new-drop []
  (let [z (rand-int 20)]
    {:x (rand-int width) 
     :y (rand-between -500 -50)
     :z z
     :yspeed (q/map-range z 0 20 4 10)
     :len (q/map-range z 0 20 10 20)}))

(defn draw-drop [{:keys [x y len z]}]
  (q/stroke 138 43 226)
  (q/stroke-weight (q/map-range z 0 20 1 2))
  (q/line x y x (+ len y)))

(defn setup []
  (take ndrops (repeatedly new-drop)))

(defn update-drop [{:keys [y yspeed z] :as drp}]
  (let [[new-y new-yspeed] 
        (if (> y height)
          [(rand-between -500 -50) (q/map-range z 0 20 4 10)]
          [(+ y yspeed) (+ yspeed 0.2)])]
    (assoc drp :y new-y)))

(defn update-state [state]
  (map update-drop state))

(defn draw-state [state] 
  (q/background 230 230 250)
  (doseq [drp state]
    (draw-drop drp)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
