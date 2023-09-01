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

(defn col-wise
  [matrix]
  (apply map vector matrix))

(defn diagonaler
  [matrix]
  (map nth matrix [0 1 2]))

(defn diagaonaler-inv
  [matrix]
  (map nth (reverse matrix) [0 1 2]))

(defn matrix-permutations
  [matrix]
  (apply conj ((juxt col-wise first second last diagonaler diagaonaler-inv) matrix)))

(defn continue?
  [matrix]
  (if (some nil? (vec (flatten matrix))) :continue "draw"))

(defn matrix-check
  [matrix]
  (let [setted-coll (set (matrix-permutations matrix))
        result        (clojure.set/intersection #{[:x :x :x] [:o :o :o]} setted-coll)]
    (cond
      (= result #{[:x :x :x]}) "x won"
      (= result #{[:o :o :o]}) "o won"
      :else                    (continue? matrix))))
