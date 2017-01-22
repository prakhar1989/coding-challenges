; Coding challenge 3 - https://www.youtube.com/watch?v=AaGK-fj-BAM
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)
(def scale 20)
(def rows (/ width scale))
(def cols (/ height scale))

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn new-food-location []
  [(rand-int rows) (rand-int cols)])

(defn new-snake []
  {:body [[(/ rows 2) (/ cols 2)]]
   :dir :east})

(defn move-snake [snake]
  (let [{:keys [x y xspeed yspeed]} snake]
    (-> (assoc snake :x (+ x xspeed))
        (assoc :y (+ y yspeed)))))

(defn setup [] 
  (q/frame-rate 10)
  {:snake (new-snake)
   :food (new-food-location)})

(defn update-state [state]
  state)

(defn draw-snake [snake]
  (q/fill 255)
  (doseq [[x y] (:body snake)]
    (q/rect (* x scale) (* y scale) scale scale)))

(defn draw-state [state] 
  (q/background 45)
  (let [[x y] (:food state)]
    (q/fill 252 153 25)
    (q/no-stroke)
    (q/rect (* x scale) (* y scale) scale scale))
  (draw-snake (:snake state)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
