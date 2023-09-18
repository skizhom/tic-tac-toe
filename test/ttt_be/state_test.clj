(ns ttt-be.state-test
  (:require [ttt-be.state :as s]
            [malli.core :as m]
            [io.pedestal.interceptor.chain :as chain]
            [clojure.test :refer [deftest is use-fixtures]]
            [io.pedestal.interceptor :as interceptor]))
(use-fixtures :each (fn [f]
                      (reset! s/state s/initial-state)
                      (f)
                      (reset! s/state s/initial-state)))
(deftest initial-state-is-valid
  (is (m/validate s/State s/initial-state)))

(deftest transactions
  (let [test-chain #(map interceptor/interceptor
                         [s/state-interceptor
                          {:name :test
                           :enter (fn [context]
                                    (assoc context :tx-data [(constantly %)]))}])]
    (chain/execute {} (test-chain :val))
    (is (= :val @s/state))
    (chain/execute {} (test-chain :val2))
    (is (= :val2 @s/state))))
