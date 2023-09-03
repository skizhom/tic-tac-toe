(ns tic-tac-toe.core
  (:gen-class))

(def win-matrix #{#{:a1 :a2 :a3} #{:b1 :b2 :b3} #{:c1 :c2 :c3}
                  #{:a1 :b1 :c1} #{:a2 :b2 :c2} #{:a3 :b3 :c3}
                  #{:a1 :b2 :c3} #{:a3 :b2 :c1}})

(defn check-set
      [x-vec pos]
      (for [x x-vec
            y x-vec
            z [pos]
            :when (not= x y)]
        #{x y z}))

(defn helper-pather
      [turn pos [x-vec o-vec]]
      (if (= turn :x)
        [(vector (conj x-vec pos) o-vec) (check-set x-vec pos)]
        [(vector x-vec (conj o-vec pos)) (check-set o-vec pos)]))

(def next-turn {:x :o, :o :x})

(defn check-result
      [turn tot-pos check]
      (let [win?       (not-empty (clojure.set/intersection win-matrix (set check)))
            x-turn?    (= turn :x)
            o-turn?    (= turn :o)
            max-turns? (= 9 (apply + (map count tot-pos)))]
        (cond
          win?       (cond
                       x-turn? :x-win
                       o-turn? :o-win)
          max-turns? :draw
          :next-play :next)))

(defn main-heart
      [turn position tot-moves]
      (let [[tot-moves check] (helper-pather turn position tot-moves)
            result            (check-result turn tot-moves check)]
        (case result
          :x-win "player x won"
          :o-win "player o won"
          :draw  "draw"
          :next  (main (next-turn turn) tot-moves))))

(defn main
      [turn tot-moves]
      (println (str "player " (name turn) "'s turn"))
      (println "enter the position : ")
      (let [pos-string        (read-line)
            position          (keyword pos-string)
            check-empty?      (empty? (clojure.set/intersection #{:a1 :a2 :a3 :b1 :b2 :b3 :c1 :c2 :c3} #{position}))
            check-duplicate?  (not-empty (clojure.set/intersection (set (flatten tot-moves)) #{position}))]
        (cond
          check-empty?     (do
                             (println "invalid input, enter a valid position.")
                             (main turn tot-moves))
          check-duplicate? (do
                             (println "duplicate position, enter a valid value.")
                             (main turn tot-moves))
          :valid-input     (main-heart turn position tot-moves))))
