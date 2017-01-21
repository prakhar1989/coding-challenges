(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 500)
(def height 500)

(defn half [x] (/ x 2))

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn random-star []
  (let [r (inc (rand-int (half width)))]
    {:x (rand-between (- (half width)) (half width))
     :y (rand-between (- (half height)) (half height))
     :r r :prev-r r }))

(defn setup []
  (q/background 0)
  (q/no-stroke)
  (take 300 (repeatedly random-star)))

(defn update-star [star]
  (let [{:keys [x y r prev-r]} star
        new-r (- r 5)]
    (if (< new-r 1) (random-star)
      {:x x :y y :r new-r :prev-r prev-r})))

(defn update-state [state]
  (map update-star state))

(defn draw-star [star]
  (let [{:keys [x y r prev-r]} star
        sx (q/map-range (/ x r) 0 1 0 (half width))
        sy (q/map-range (/ y r) 0 1 0 (half height))
        px (q/map-range (/ x prev-r) 0 1 0 (half width))
        py (q/map-range (/ y prev-r) 0 1 0 (half height))
        dr (q/map-range prev-r 0 (half width) 12 0)]
    (q/fill 255)
    (q/ellipse sx sy dr dr)
    (q/stroke 45)
    (q/line px py sx sy)))

(defn draw-state [state]
  (q/background 0)
  (q/with-translation [(half width) (half height)]
    (doseq [star state]
      (draw-star star))))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
