(ns tic-tac-toe.model-test
  (:require [tic-tac-toe.model :as t]
            [clojure.spec.alpha :as s]
            [clojure.test :refer [is are deftest testing]]))

(deftest initial-state
  (is (s/valid? ::t/game t/initial-state))
  (is (= [nil] (-> t/initial-state :board distinct)))
  (is (= :x (:turn t/initial-state))))
