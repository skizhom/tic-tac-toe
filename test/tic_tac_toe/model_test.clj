(ns tic-tac-toe.model-test
  (:require [tic-tac-toe.model :as t]
            [clojure.spec.alpha :as s]
            [tic-tac-toe.result :as r]
            [clojure.test :refer [is are deftest testing]]))

(deftest initial-state
  (is (s/valid? ::t/game t/initial-state))
  (is (= [nil] (-> t/initial-state :board distinct)))
  (is (= :x (:turn t/initial-state))))

(deftest clicking
  (let [s0 t/initial-state
        s1 (r/value (t/click s0 [0 0]))
        r1 (t/click s0 [0 0])
        s2 (r/value (t/click s1 [0 1]))]
    (is (s/valid? ::t/game s1))
    (is (s/valid? ::t/game s2))
    (is (= :o (:turn s1)))
    (is (= :x (:turn s2)))
    (is (= {:x 1 nil 8} (frequencies (:board s1))))
    (is (= :x (get-in [:board 0 0] s1)))
    (is (= [[:x :o nil]
            [nil nil nil]
            [nil nil nil]] (:board s2)))
    (is (r/faliure? r1))
    (is (= (r/error r1) :duplicate-mark))))

(deftest victor-test
  (let [create #(assoc initial-state :board %2 :turn %1)
        none (create :x
                     [[nil nil nil]
                      [nil :x :o]
                      [nil nil nil]])
        o-vert (create :x
                       [[nil :x :o]
                        [nil :x :o]
                        [:x nil :o]])
        x-hori (create :o
                       [[nil :x :o]
                        [nil :x :o]
                        [nil :x nil]])
        x-cross (create :o
                        [[:x nil :o]
                         [nil :x :o]
                         [nil nil :x]])]
    (is (= nil (t/victor none)))
    (is (= :o (t/victor o-vert)))
    (is (= :x (t/victor x-hori)))
    (is (= :x (t/victor x-cross)))
    (testing "victory prevents further moves"
      (let [r (t/click x-cross [2 0])]
        (is (r/faliure? r))
        (is (= :game-ended (r/error r)))))))

(deftest testing-helpers
  (testing "rotation"
    (is (= [[4 1]
            [3 2]]
           (t/rotate90deg [[1 2]
                           [4 3]]))))
  (testing "rotations"
    (is (= #{[[1 2]]
             [[2 1]]
             [[1] [2]]
             [[2] [1]]}
           (set (t/rotations [[1 2]])))))
  (testing "positions"
    (is (= #{#{[0 0] [0 1]}
             #{[1 0] [1 1]}}
           (set (t/positions [[true true]] [2 2]))))
    (is (= #{#{[0 0]}
             #{[1 0]}}
           (set (t/positions [[true false]] [2 2]))))
    (is (= #{#{[0 0]}
             #{[1 0]}
             #{[0 1]}
             #{[1 1]}}
           (set (t/positions [[true]] [2 2]))))
    (is (= #{#{[0 0]}
             #{[0 1]}}
           (set (t/positions [[true]] [1 2]))))))
