(ns tic-tac-toe.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [tic-tac-toe.core :as t]))

#_(deftest printing-test
  (let [expected-output
        (str "o|o| \n"
             "-+-+-\n"
             "x|x| \n"
             "-+-+-\n"
             " | | \n")
        data {:board
              [[:o :o nil]
               [:x :x nil]
               [nil nil nil]
               :turn :o]}]
    (is (= expected-output
           (with-out-str ((t/print-board) data))))))


(deftest check-set-basic-functionality-1st-move
  (testing "check if the function returns empty set after the 1st move of the player"
    (let [expected-output ()]
      (is (= expected-output (t/check-set [] :a1))))))

(deftest check-set-functionality-2nd-move
  (testing "check if the function returns empty set after the 2nd move of the player"
    (let [expected-output ()]
      (is (= expected-output (t/check-set [:a1] :a2))))))

(deftest check-set-functionality-3rd-move
  (testing "check if the function returns two same sets after the 3rd move of the player"
    (let [expected-output '(#{:a2 :a3 :a1} #{:a2 :a3 :a1})]
      (is (= (set expected-output) (set (t/check-set [:a1 :a3] :a2)))))))

(deftest check-set-functionality-4th-move
  (testing "check if the function returns two same sets after the 4th move of the player"
    (let [expected-output '(#{:a2 :a3 :a1} #{:a3 :a1 :c1} #{:a2 :a3 :a1} #{:a2 :a3 :c1} #{:a3 :a1 :c1} #{:a2 :a3 :c1})]
      (is (= (set expected-output) (set (t/check-set [:a1 :a2 :c1] :a3)))))))

(deftest check-set-functionality-final-move
  (testing "check if the function returns two same sets after the 4th move of the player"
    (let [expected-output '(#{:a2 :b2 :b3}
                            #{:b2 :c1 :b3}
                            #{:b2 :b1 :b3}
                            #{:a2 :b2 :b3}
                            #{:a2 :c1 :b3}
                            #{:a2 :b1 :b3}
                            #{:b2 :c1 :b3}
                            #{:a2 :c1 :b3}
                            #{:b1 :c1 :b3}
                            #{:b2 :b1 :b3}
                            #{:a2 :b1 :b3}
                            #{:b1 :c1 :b3})]
      (is (= (set expected-output) (set (t/check-set [:b2 :a2 :c1 :b1] :b3)))))))



(deftest helper-pather-basic-functionality-1st-move
  (testing "check if the function returns two same sets after the 1st move of the :x player"
    (let [expected-output [{:x [:b2], :o []} '()]]
      (is (= expected-output (t/helper-pather :x :b2 {:x [], :o []}))))))

(deftest helper-pather-basic-functionality-2nd-move
  (testing "check if the function returns the following vector on the 2nd move of the :x player"
    (let [expected-output [{:x [:b2 :c3], :o [:a1]} '()]]
      (is (= expected-output (t/helper-pather :x :c3 {:x [:b2], :o [:a1]}))))))

(deftest helper-pather-basic-functionality-3rd-move
  (testing "check if the function returns two same sets as the second element on the returnned vector in the 3rd move of the :o player"
    (let [expected-output [{:x [:a1 :b2 :a3], :o [:c1 :c3 :c2]} '(#{:c3 :c2 :c1} #{:c3 :c2 :c1})]]
      (is (= expected-output (t/helper-pather :o :c2 {:x [:a1 :b2 :a3], :o [:c1 :c3]}))))))



(deftest get-postion-1st-move
  (testing "testing 'get-position' on player-one's first move"
    (let [inv-pos   "zd"
          val-pos  "a1"
          turn      :x
          tot-moves {:x [], :o []}]
      (is (= 
           (keyword val-pos) 
           (with-in-str (str inv-pos "\n" val-pos) (t/get-position turn tot-moves)))))))

(deftest get-postion-2nd-move
  (testing "testing 'get-position' on player-two's first move"
    (let [inv-pos   "zd"
          dup-pos   "a1"
          val-pos   "b2"
          turn      :o
          tot-moves {:x [:a1], :o []}]
      (is (=
           (keyword val-pos)
           (with-in-str (str inv-pos "\n" dup-pos "\n" val-pos) (t/get-position turn tot-moves)))))))

(deftest get-postion-3rd-move
  (testing "testing 'get-position' on player-one's second move"
    (let [inv-pos   "zd"
          dup-pos   "b2"
          val-pos   "c1"
          turn      :x
          tot-moves {:x [:a1], :o [:b2]}]
      (is (=
           (keyword val-pos)
           (with-in-str (str inv-pos "\n" dup-pos "\n" val-pos) (t/get-position turn tot-moves)))))))
