(ns tic-tac-toe.core
  (:gen-class))

(def win-matrix #{#{:a1 :a2 :a3} #{:b1 :b2 :b3} #{:c1 :c2 :c3}
                  #{:a1 :b1 :c1} #{:a2 :b2 :c2} #{:a3 :b3 :c3}
                  #{:a1 :b2 :c3} #{:a3 :b2 :c1}})

(defn check-set
      [ip-vec pos]
      (for [x ip-vec
            y ip-vec
            z [pos]
            :when (not= x y)]
        #{x y z}))

(defn helper-pather
  [turn pos tot-moves]
  [(update tot-moves turn conj pos) (check-set (tot-moves turn) pos)])

(def next-turn {:x :o, :o :x})

(defn check-result
      [turn tot-pos check]
      (let [win?       (not-empty (clojure.set/intersection win-matrix (set check)))
            x-turn?    (= turn :x)
            o-turn?    (= turn :o)
            max-turns? (= 9 (apply + (map count (vals tot-pos))))]
        (cond
          win?       (cond
                       x-turn? :x-win
                       o-turn? :o-win)
          max-turns? :draw
          :next-play :next)))

(defn get-position
  [turn tot-moves]
  (loop [t-m tot-moves
         t   turn]
    (println (str "player " (name t) "'s turn"))
    (println "enter the position : ")
    (let [pos-string        (read-line)
          position          (keyword pos-string)
          check-invalid?    ((complement contains?) #{:a1 :a2 :a3 :b1 :b2 :b3 :c1 :c2 :c3} position)
          check-duplicate?  (contains? (set (flatten (vals t-m))) position)]
      (cond
        check-invalid?   (do
                           (println "invalid input, enter a valid position.")
                           (recur t-m t))
        check-duplicate? (do
                           (println "duplicate position, enter a valid value.")
                           (recur t-m t))
        :valid-input     position))))

(defn main
  []
  (loop [turn      :x
         tot-moves {:x [], :o []}]
    (let [position          (get-position turn tot-moves)
          [tot-moves check] (helper-pather turn position tot-moves)
          result            (check-result turn tot-moves check)]
      (case result
        :x-win "player x won"
        :o-win "player o won"
        :draw  "the game is a draw"
        :next  (recur (next-turn turn) tot-moves)))))


