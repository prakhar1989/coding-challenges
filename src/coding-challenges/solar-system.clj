; Coding challenge 7-9 - https://www.youtube.com/watch?v=l8SiJ-RmeHU
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn get-planet [radius distance orbit-speed]
  {:r radius :d distance
   :theta (* 2 q/PI (rand))
   :orbit-speed orbit-speed
   :moons []})

(defn generate-moons
  [{:keys [r d] :as planet} total level]
  (if (> level 2) planet
    (let [get-new-moon (fn [] (get-planet (/ r (* 1.2 level))
                                          (/ (rand-between 100 150) level)
                                          (+ -0.01 (rand 0.08))))
          moons (take total (repeatedly get-new-moon))
          sub-moons (map #(generate-moons % 2 (inc level)) moons)]
      (assoc planet :moons sub-moons))))

(defn setup [] 
  (let [sun (get-planet 50 0 0)
        t (generate-moons sun 5 1)]
    (generate-moons sun 5 1)))

(defn orbit-planet
  [{:keys [theta orbit-speed moons] :as planet}]
  (assoc planet :theta (+ theta orbit-speed)
                :moons (map orbit-planet moons)))

(defn update-state [state]
  (orbit-planet state))

(defn draw-planet [{:keys [r d theta] :as planet}]
  (q/rotate theta)
  (q/with-translation [d 0]
    (q/fill 255 100)
    (q/ellipse 0 0 (* 2 r) (* 2 r))
    (doseq [m (:moons planet)]
      (draw-planet m))))

(defn draw-state [state] 
  (q/background 0)
  (q/with-translation [(/ width 2) (/ height 2)]
    (draw-planet state)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
