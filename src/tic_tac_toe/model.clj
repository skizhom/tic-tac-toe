(ns tic-tac-toe.model
  (:require [clojure.spec.alpha :as s])
  (:require [clojure.core]))

(def matrix {:a1 nil, :a2 nil, :a3 nil,
             :b1 nil, :b2 nil, :b3 nil,
             :c1 nil, :c2 nil, :c3 nil})

(defn check-patterns
      [matrix pos1 pos2 pos3]
      [(matrix pos1) (matrix pos2) (matrix pos3)])

(defn pattern-list
      [matrix]
      (map (partial check-patterns matrix)
           [:a1 :b1 :c1 :a1 :a2 :a3 :a1 :a3]
           [:a2 :b2 :c2 :b1 :b2 :b3 :b2 :b2]
           [:a3 :b3 :c3 :c1 :c2 :c3 :c3 :c1]))

(defn game-result
  [matrix]
  (let [pattern-set (set (pattern-list matrix))
        continue?   (contains? (set (map val matrix)) nil)]
    (cond
      (contains? pattern-set [:x :x :x]) :x-won
      (contains? pattern-set [:o :o :o]) :o-won
      (not continue?)                    :is-draw
      :default                              :else)))


(def next-xox {:x :o, :o :x})

(defn main [matrix turn]
  (let [pos-string     (read-line)
        pos-keyw       (keyword pos-string)
        updated-matrix (update matrix pos-keyw turn)
        game-state     (game-result updated-matrix)
        turn-name      (name turn)
        next-turn      (next-xox turn)]
    (print (str "Player " turn-name "'s turn.\n"))
    (print "Enter the position : ")
    (flush)
    (condp = game-state
      :x-won    "player x won"
      :o-won    "player o won"
      :is-draw  "game ended in draw"
      :else     (main updated-matrix next-turn))))
