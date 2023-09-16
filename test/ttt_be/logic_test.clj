(ns ttt-be.logic-test
  (:require [ttt-be.logic :refer :all]
            [clojure.test :refer [is are testing deftest]]
            [better-cond.core :as b]
            [malli.core :as m]))

(deftest initial-state-test
  (is (m/validate State initial-state)))

(deftest player-adding-test
  (is (m/validate Player (create-player "test")))
  (is (= "test" (-> "test" create-player :name)))
  (is (not= (-> "test" create-player :id)
            (-> "test" create-player :id)))
  (is (not= (-> "test" create-player :token)
            (-> "test" create-player :token))))
