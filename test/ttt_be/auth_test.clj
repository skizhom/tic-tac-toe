(ns ttt-be.auth-test
  (:require [ttt-be.auth :as sut]
            [io.pedestal.interceptor.chain :as chain]
            [clojure.test :refer :all]))

(def id-1 #uuid "00000000-0000-0000-0000-000000000001")
(def id-2 #uuid "00000000-0000-0000-0000-000000000002")
(def token-1 #uuid "00000000-0000-0000-0000-0000000000a1")
(def token-2 #uuid "00000000-0000-0000-0000-0000000000a2")
(def player-1 {:id id-1 :token token-1 :name "1"})
(def state {:players {id-1 player-1}})

(defn make-context [state id token]
  {:request {:state state}
   :headers {"Player-Id" (str id)
             "Player-Token" (str token)}})

(deftest verify-player-test
  (is (= 401 (->> {:state :state :headers {}}
                  sut/verify-player
                  :response
                  :status)))
  (is (= 401 (->> (make-context state "uwu" "owo")
                  sut/verify-player
                  :response
                  :status)))
  (is (= 401 (->> (make-context state "71351\\" "91397351+[=+]")
                  sut/verify-player
                  :response
                  :status)))
  (is (= 401 (->> (make-context state id-1 token-2)
                  sut/verify-player
                  :response
                  :status)))
  (is (= 401 (->> (make-context state id-2 token-1)
                  sut/verify-player
                  :response
                  :status)))
  (is (= {:id id-1, :token token-1 :name "1"}
         (->> (make-context state id-1 token-1)
              sut/verify-player
              :player))))

(deftest register-player-test
  (is (= 400 (->> {:request {:state state :body-params {:name "1"}}}
                  sut/register-player
                  :response
                  :status)))
  (is (= 200 (->> {:request {:state state :body-params {:name "test"}}}
                  sut/register-player
                  :response
                  :status)))
  (is (= "test" (->> {:request {:state state :body-params {:name "test"}}}
                     sut/register-player
                     :tx-data
                     second
                     :name)))
  (is (= "test" (->> {:request {:state state :body-params {:name "test"}}}
                     sut/register-player
                     :response
                     :body
                     :name))))
