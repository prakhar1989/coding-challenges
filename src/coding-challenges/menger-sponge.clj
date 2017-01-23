; Coding challenge 2 - https://www.youtube.com/watch?v=LG8ZK-rRkXo
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn new-box [x y z r]
  {:pos [x y z] :r r})

(defn setup [] 
  {:a 0 :boxes [(new-box 0 0 0 200)]})

(defn generate-boxes [{:keys [pos r] :as box}]
  (let [new-r (/ r 3)]
    (->> (for [x '(-1 0 1) y '(-1 0 1) z '(-1 0 1)
               :when (> (reduce + (map #(Math/abs %) [x y z])) 1)]
           (new-box (+ (get pos 0) (* new-r x)) 
                    (+ (get pos 1) (* new-r y))
                    (+ (get pos 2) (* new-r z))
                    new-r)))))

(defn show-box [{:keys [pos r] :as box}]
  (q/push-matrix)
  (apply q/translate pos)
  (q/box r)
  (q/pop-matrix))

(defn update-state [state] 
  (update state :a #(+ 0.01 %)))

(defn on-mouse-press [state event]
  (let [new-boxes (mapcat generate-boxes (:boxes state))]
    (assoc state :boxes new-boxes)))

(defn draw-state [state]
  (q/background 51)
  (q/lights)
  (q/no-stroke)
  (q/fill 255 96 17)
  (q/translate (/ width 2) (/ height 2))
  (q/rotate-x (:a state))
  (q/rotate-y (* 0.4 (:a state)))
  (q/rotate-z (* 0.1 (:a state)))
  (doseq [box (:boxes state)]
    (show-box box)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :renderer :p3d
  :mouse-pressed on-mouse-press
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
