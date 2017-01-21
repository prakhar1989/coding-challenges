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

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn get-k-random [k]
  (->> (repeatedly #(rand-between -10 10))
       (take k)
       (vec)))

(defn gen-terrain [delta]
  (->> 
    (repeatedly #(get-k-random rows))
    (take cols) 
    (vec)))

(defn setup []
  {:terrain (gen-terrain 0) 
   :flying-speed 0})

(defn update-state [state] 
  (let [speed (:flying-speed state)]
    {:terrain (gen-terrain speed)
     :flying-speed (- speed 0.1)}))

(defn draw-state [state]
  (let [terrain (:terrain state)]
    (q/background 0)
    (q/stroke 255)
    (q/no-fill)
    (q/translate (/ width 2) (/ height 2))
    (q/rotate-x (/ q/PI 3))
    (q/translate (/ (- w) 2) (/ (- h) 2))
    (doseq [row (range (dec rows))]
      (q/begin-shape :triangle-strip)
      (doseq [col (range cols)]
        (q/vertex (* col scl) (* row scl) (get-in terrain [col row]))
        (q/vertex (* col scl) (* (inc row) scl) (get-in terrain [col (inc row)])))
      (q/end-shape))))


(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :renderer :p3d
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
