; Coding challenge 5 - https://www.youtube.com/watch?v=biN3v3ef-Y0
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 600)
(def height 600)

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

;; --- EVENT HANDLER ---
(defn on-key-down [state event]
  (let [ship-x (get-in state [:ship :x])]
    (case (:key event)
      (:a :left) (update-in state [:ship :x] #(- % 10))
      (:d :right) (update-in state [:ship :x] #(+ % 10))
      (:w :up) (assoc state :drops
                      (conj (:drops state) (get-droplet ship-x height)))
      state)))

(def colors '([255 149 5] [253 231 76] [229 89 52] [165 70 87]))

;; --- SHIP ---
(defn get-ship []
  {:x (/ width 2)})

(defn draw-ship [ship]
  (q/fill 255)
  (q/no-stroke)
  (q/rect-mode :center)
  (q/rect (:x ship) (- height 20) 20 60))

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
  (let [flowers (for [x (range 60 540 90)
                      y (range 60 300 90)]
                  (get-flower x y))]
  {:ship (get-ship) :flowers flowers :drops []}))

;; returns new state after removing
;; drops and flowers that hit each other
(defn remove-hits [{:keys [drops flowers] :as state}]
  (let [remaining (for [d drops
                        f flowers
                        :when (not (intersect? d f))]
                    [d f])]
    (assoc state :flowers (mapv second remaining))))
 
(defn update-state [state] 
  (let [new-state (if (empty? (:drops state)) state
                    (remove-hits state))]
    (assoc new-state :drops (move-droplets (:drops new-state)))))

(defn draw-state [state]
  (q/background 51)
  (draw-ship (:ship state))
  (doseq [flower (:flowers state)]
    (draw-flower flower))
  (doseq [d (:drops state)]
    (draw-droplet d)))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :key-pressed on-key-down
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
