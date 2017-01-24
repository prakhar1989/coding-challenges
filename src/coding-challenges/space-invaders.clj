; Coding challenge 5 - https://www.youtube.com/watch?v=biN3v3ef-Y0
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(def colors '([255 149 5] [253 231 76] [229 89 52] [165 70 87]))

;; --- SHIP ---
(defn get-ship []
  {:x (/ width 2) :dx 0})

(defn draw-ship [ship]
  (q/fill 255)
  (q/no-stroke)
  (q/rect-mode :center)
  (q/rect (:x ship) (- height 20) 20 60))

(defn move-ship [{:keys [x dx] :as ship}]
  (assoc ship :x (+ x dx)))

;; --- FLOWER ---
(defn get-flower [x y]
  {:x x :y y :color (rand-nth colors)})

(defn draw-flower [flower]
  (apply q/fill (:color flower))
  (q/ellipse (:x flower) (:y flower) 60 60))

;; --- DROPLET ---
(defn get-droplet [x y]
  {:x x :y y})

(defn draw-droplet [{:keys [x y]}]
  (q/fill 50 200 200)
  (q/ellipse x y 16 16))

(defn intersect? [droplet flower]
  (let [dist (fn [a b] (* (- a b) (- a b)))
        center-dist (+ (dist (:x droplet) (:x flower))
                       (dist (:y droplet) (:y flower)))]
    (<= center-dist (* 38 38)))) ; 38 = sum of radii of the two

(defn move-droplets [drops]
  (->> drops
       (filter #(pos? (:y %)))
       (map (fn [drp] (update drp :y #(- % 10))))))

;; --- SKETCH ---
(defn setup []
  (let [flowers (for [x (range 60 540 90)]
                      ;y (range 120 300 90)]
                  (get-flower x 100))]
  {:ship (get-ship) :flowers flowers :drops []}))

;; returns new state after removing
;; drops and flowers that hit each other
;; TODO: fix the performance issue in this function
(defn remove-hits [state]
  (let [remaining (into [] (for [f (:flowers state)
                                 d (:drops state)
                                 :when (not (intersect? d f))] f))]
    state))
 
(defn update-state [state] 
  (let [new-ship (move-ship (:ship state))
        new-drops (move-droplets (:drops state))
        new-state (assoc state :drops new-drops)]
    (if (empty? new-drops) (assoc new-state :ship new-ship)
      (remove-hits new-state))))

(defn draw-state [state]
  (q/background 51)
  (draw-ship (:ship state))
  (doseq [flower (:flowers state)]
    (draw-flower flower))
  (doseq [d (:drops state)]
    (draw-droplet d)))

;; --- EVENT HANDLERS ---
(defn on-key-release [state]
  (assoc-in state [:ship :dx] 0))

(defn on-key-down [state event]
  (let [ship-x (get-in state [:ship :x])]
    (case (:key event)
      (:a :left) (assoc-in state [:ship :dx] -1)
      (:d :right) (assoc-in state [:ship :dx] 1)
      (:w :up) (assoc state :drops
                      (conj (:drops state) (get-droplet ship-x height)))
      state)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :key-pressed on-key-down
  :key-released on-key-release
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
