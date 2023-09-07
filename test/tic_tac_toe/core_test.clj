(ns tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.core :as t]))

(deftest printing-test
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
           (with-out-str (t/print-board data))))))


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
    (let [expected-output (#{:a2 :a3 :a1} #{:a2 :a3 :a1})]
      (is (= expected-output (t/check-set [:a1 :a3] :a2))))))

(deftest check-set-functionality-4th-move
  (testing "check if the function returns two same sets after the 4th move of the player"
    (let [expected-output (#{:a2 :a3 :a1} #{:a3 :a1 :c1} #{:a2 :a3 :a1} #{:a2 :a3 :c1} #{:a3 :a1 :c1} #{:a2 :a3 :c1})]
      (is (= expected-output (t/check-set [:a1 :a2 :c1] :a3))))))

(deftest check-set-functionality-final-move
  (testing "check if the function returns two same sets after the 4th move of the player"
    (let [expected-output (#{:a2 :b2 :b3}
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
      (is (= expected-output (t/check-set [:b2 :a2 :c1 :b1] :b3))))))



(deftest helper-pather-basic-functionality-1st-move
  (testing "check if the function returns two same sets after the 1st move of the :x player"
    (let [expected-output [{:x [:b2], :o []} ()]]
      (is (= expected-output (t/helper-pather :x :b2 {:x [], :o []}))))))

(deftest helper-pather-basic-functionality-2nd-move
  (testing "check if the function returns the following vector on the 2nd move of the :x player"
    (let [expected-output [{:x [:b2 :c3], :o [:a1]} ()]]
      (is (= expected-output (t/helper-pather :x :c3 {:x [:b2], :o [:a1]}))))))

(deftest helper-pather-basic-functionality-3rd-move
  (testing "check if the function returns two same sets as the second element on the returnned vector in the 3rd move of the :o player"
    (let [expected-output [{:x [:a1 :b2 :a3], :o [:c1 :c3 :c2]} (#{:c3 :c2 :c1} #{:c3 :c2 :c1})]]
      (is (= expected-output (t/helper-pather :o :c2 {:x [:a1 :b2 :a3], :o [:c1 :c3]}))))))
