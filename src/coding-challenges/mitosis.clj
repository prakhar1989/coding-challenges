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
     :color [(rand-between 100 255) 0 (rand-between 100 255) 100]}))

(defn move-cell [cell] 
  (let [[x y] (:pos cell)]
    (assoc cell :pos [(+ x (q/random -1 1))
                      (+ y (q/random -1 1))])))

(defn show-cell [cell]
  (let [[x y] (:pos cell)
        r     (:r cell)]
    (apply q/fill (:color cell))
    (q/no-stroke)
    (q/ellipse x y r r)))

(defn multiply-cell [cell]
  (assoc cell :r 40))

(defn setup [] 
  [(get-cell)])

(defn click-on-cell? [cell]
  (let [[x y] (:pos cell)
        r     (:r cell)
        dist  (fn [a b] (Math/pow (- a b) 2))]
    (and (<= (dist (q/mouse-x) x) (* r r))
         (<= (dist (q/mouse-y) y) (* r r)))))

(defn check-clicks [state]
  (let [clicked-cell (first (filter click-on-cell? state))]
    (if (nil? clicked-cell) state
      (->> state
           (remove #(= clicked-cell %))
           (concat (take 2 (repeatedly #(multiply-cell clicked-cell))))))))

(defn update-state [state]
  (if (q/mouse-pressed?)
    (check-clicks state)
    (map move-cell state)))

(defn draw-state [state] 
  (q/background 201)
  (doseq [cell state]
    (show-cell cell)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
