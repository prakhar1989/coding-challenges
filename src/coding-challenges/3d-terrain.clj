(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)
(def scl 20)
(def cols (/ width scl))
(def rows (/ height scl))

(defn setup [])

(defn draw-state [state]
  (q/background 0)
  (q/stroke 255)
  (q/no-fill)
  (doseq [row (range rows)]
    (q/begin-shape :triangle-strip)
    (doseq [col (range cols)]
      (q/vertex (* col scl) (* row scl))
      (q/vertex (* col scl) (* (inc row) scl)))
    (q/end-shape)))

(defn update-state [state] state)

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
