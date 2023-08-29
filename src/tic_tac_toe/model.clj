(ns tic-tac-toe.model
  (:require [clojure.spec.alpha :as s]))

(s/def ::field #{:o :x nil})
(s/def ::board (-> ::field
                   (s/coll-of :kind vector? :count 3)
                   (s/coll-of :kind vector? :count 3)))
(s/def ::turn #{:o :x})
(s/def ::rule (-> boolean?
                  (s/coll-of :kind vector?)
                  (s/coll-of :kind vector?)))
(s/def ::rules (-> ::rule
                   (s/coll-of :kind vector?)))
(s/def ::game (s/keys :req-un [::board ::turn ::rules]))

(def default-rules [;; straight
                    [[true true true]]
                    ;; diagonal
                    [[true false false]
                     [false true false]
                     [false false true]]])

(def initial-state {})

(defn click [state position])

(defn size [coll]
  [(count coll) (count (first coll))])

(defn transpose [x]
  (apply mapv vector x))

(def reversev (comp vec reverse))

(def rotate90deg "shape->shape"
  (comp transpose reversev))

(defn rotations "shape->shape"
  [x]
  (->> x
       (iterate rotate90deg)
       (take 4)))

(defn positions "returns a list of sets of points which the rule covers"
  ;; keeping "shape" after this would be more and more cumbersome
  [shape area]
  (let [[a-x a-y] area
        [s-x s-y] (size shape)
        diff-x (- a-x s-x)
        diff-y (- a-y s-y)]
    (for [ox (range (inc diff-x))
          oy (range (inc diff-y))]
      ;; this can be done first (TODO)
      (->> (for [x (range s-x)
                 y (range s-y)
                 :when (get-in shape [x y])]
             [(+ ox x) (+ oy y)])
           (into #{})))))

(defn victor [state])
