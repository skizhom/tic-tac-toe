(ns ttt-be.state-helpers-test
  (:require [ttt-be.state-helpers :refer :all]
            [clojure.test :refer [is are testing deftest]]
            [better-cond.core :as b]
            [ttt-be.schemas :refer [State Player]]
            [ttt-be.state :refer [initial-state]]
            [malli.core :as m]
            [ttt-be.state :as s]))

(deftest initial-state-test
  (is (m/validate State initial-state)))

(deftest player-adding-test
  (is (m/validate Player (create-player "test")))
  (is (= "test" (-> "test" create-player :name)))
  (is (not= (-> "test" create-player :id)
            (-> "test" create-player :id)))
  (is (not= (-> "test" create-player :token)
            (-> "test" create-player :token)))
  (let [player (create-player "test")
        state' (add-player initial-state player)
        state'' (remove-player state' (:id player))]
    (testing "name collision detection test"
      (is (not (player-name-already-exists? state' "else")))
      (is (player-name-already-exists? state' "test")))
    (is (m/validate State state'))
    (is (m/validate State state''))
    (is (= initial-state state''))))

(deftest auth-test
  (is (nil? (verify-player-data {:players {:a {:token :b}}} :a :other)))
  (is (nil? (verify-player-data {:players {:a {:token :b}}} :other :b)))
  (is (nil? (verify-player-data {:players {:a {:token :b}}} :other :other)))
  (is (= {:token :b, :name "c", :id :a}
         (verify-player-data {:players {:a {:token :b :name "c" :id :a}}} :a :b))))

(deftest queue-manipulation-test
  (is (= {:id1 {:connection :conn-thing}} (:waiting (add-player-to-waiting-list initial-state :id1 :conn-thing))))
  (is (= {} (-> initial-state (add-player-to-waiting-list :id1 :conn-thing)
                (remove-player-from-waiting-list :id1)
                :waiting))))
