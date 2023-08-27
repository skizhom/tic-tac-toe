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
