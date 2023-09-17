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
            (-> "test" create-player :token)))
  (let [state' (:state (add-player initial-state "test"))
        player-id (-> state' :players ffirst)
        state'' (:state (remove-player state' player-id))]
    (is (m/validate State state'))
    (is (m/validate State state''))
    (is (= initial-state state''))))

(deftest auth-test
  (is (nil? (confirm-player {:players {:a {:token :b}}} :a :other)))
  (is (nil? (confirm-player {:players {:a {:token :b}}} :other :b)))
  (is (nil? (confirm-player {:players {:a {:token :b}}} :other :other)))
  (is (= {:token :b, :name "c", :id :a}
         (confirm-player {:players {:a {:token :b :name "c" :id :a}}} :a :b))))
