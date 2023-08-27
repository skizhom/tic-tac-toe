(ns tic-tac-toe.model-test
  (:require [tic-tac-toe.model :as t]
            [clojure.spec.alpha :as s]
            [clojure.test :refer [is are deftest testing]]))

(deftest initial-state
  (is (s/valid? ::t/game t/initial-state))
  (is (= [nil] (-> t/initial-state :board distinct)))
  (is (= :x (:turn t/initial-state))))

(deftest clicking
  (let [s0 t/initial-state
        s1 (t/click s0 [0 0])
        s2 (t/click s1 [0 1])]
    (is (s/valid? ::t/game s1))
    (is (s/valid? ::t/game s2))
    (is (= :o (:turn s1)))
    (is (= :x (:turn s2)))
    (is (= {:x 1 nil 8} (frequencies (:board s1))))
    (is (= :x (get-in [:board 0 0] s1)))
    (is (= [[:x :o nil]
            [nil nil nil]
            [nil nil nil]] (:board s2)))))

(deftest victor-test
  (let [none {:board
              [[nil nil nil]
               [nil :x :o]
               [nil nil nil]
               :turn :x]}
        o-vert {:board
                [[nil :x :o]
                 [nil :x :o]
                 [:x nil :o]
                 :turn :x]}
        x-hori {:board
                [[nil :x :o]
                 [nil :x :o]
                 [nil :x nil]
                 :turn :o]}
        x-cross {:board
                 [[:x nil :o]
                  [nil :x :o]
                  [nil nil :x]
                  :turn :o]}]
    (is (= nil (t/victor none)))
    (is (= :o (t/victor o-vert)))
    (is (= :x (t/victor x-hori)))
    (is (= :x (t/victor x-cross)))))
