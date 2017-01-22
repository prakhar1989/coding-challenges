; Coding challenge 3 - https://www.youtube.com/watch?v=AaGK-fj-BAM
(ns quil-site.examples.nanoscopic
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def width 400)
(def height 400)
(def scale 20)
(def rows (/ width scale))
(def cols (/ height scale))

(defn rand-between [start end]
  (+ start (rand-int (- end start))))

(defn new-food-location []
  [(rand-int rows) (rand-int cols)])

(defn new-snake []
  {:body [[(/ rows 2) (/ cols 2)]]
   :dir :right})

(defn add-points [& pts]
  (let [[x y] (vec (apply map + pts))]
    [(mod x rows) (mod y cols)]))

(defn move-snake [{:keys [body dir] :as snake} & grow]
  (let [dirs {:right [1 0] :up [0 -1] :left [-1 0] :down [0 1]}]
    (assoc snake :body 
           (cons (add-points (first body) (dir dirs))
                 (if grow body (butlast body))))))

(defn reset-state []
  {:snake (new-snake)
   :food (new-food-location)
   :score 0})

(defn setup [] 
  (q/frame-rate 10)
  (reset-state))

(defn get-new-dir [new-dir prev-dir]
  (case new-dir
    :left (if (= prev-dir :right) prev-dir :left)
    :up (if (= prev-dir :down) prev-dir :up)
    :right (if (= prev-dir :left) prev-dir :right)
    :down (if (= prev-dir :up) prev-dir :down)
    :else prev-dir))

(defn on-key-down [state event]
  (let [prev-dir (get-in state [:snake :dir])]
    (case (:key event)
      (:w :up)    (assoc-in state [:snake :dir] (get-new-dir :up prev-dir))
      (:s :down)  (assoc-in state [:snake :dir] (get-new-dir :down prev-dir))
      (:a :left)  (assoc-in state [:snake :dir] (get-new-dir :left prev-dir))
      (:d :right) (assoc-in state [:snake :dir] (get-new-dir :right prev-dir))
      state)))

(defn has-collided? [snake]
  (some #(= % (first snake)) (rest snake)))

(defn update-state [{:keys [snake food score]}]
  (cond
    (has-collided? (:body snake))
      (reset-state)
    (= (first (:body snake)) food)
      {:snake (move-snake snake :grow) 
       :food  (new-food-location)
       :score (inc score)}
    :else
      {:snake (move-snake snake)
       :food  food
       :score score}))

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
  (draw-snake (:snake state))
  (q/text-size 24)
  (q/text (str (:score state)) 10 10 100 100))

(q/defsketch nanoscopic
  :host "host"
  :size [width height]
  :setup setup
  :update update-state
  :key-pressed on-key-down
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
