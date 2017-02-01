;; Coding challenge 27 - https://www.youtube.com/watch?v=CKeyIbT3vXI
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn new-particle [x y]
  (let [v (rand-between -12 -8)]
    {:pos [x y] :vel [0 v] :acc [0 0]}))

(defn update-particle [p]
  (let [vel (map + (:vel p) (:acc p))
        pos (map + (:pos p) vel)]
    ;; reset the acceleration to zero
    {:pos pos :vel vel :acc [0 0]}))

(defn apply-force [p f]
  (assoc p :acc (map + (:acc p) f)))

(defn draw-particle [p]
  (when (not (nil? p))
    (let [[x y] (:pos p)]
      (q/point x y))))

(defn in-bounds? [p]
  (let [[x y] (:pos p)]
    (and (> x 0) (< x width)
         (> y 0) (< y height))))

(defn new-firework []
  (new-particle (rand-int width) height))

(defn update-firework [fw gravity]
  (let [fw (->> (apply-force fw gravity)
                (update-particle))]
    (if (in-bounds? fw) fw nil)))

(defn setup [] 
  (q/stroke 255)
  (q/stroke-weight 4)
  {:gravity   [0 0.2]
   :fireworks [(new-firework)]})

(defn update-state
  [{:keys [gravity fireworks] :as state}] 
  (let [fireworks (filter (comp not nil?) fireworks)
        new-fw (if (> (rand) 0.1) fireworks (conj fireworks (new-firework)))]
    (assoc state :fireworks 
           (map #(update-firework % gravity) new-fw))))

(defn draw-state [state]
  (q/background 51)
  (doseq [fw (:fireworks state)]
    (draw-particle fw)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
