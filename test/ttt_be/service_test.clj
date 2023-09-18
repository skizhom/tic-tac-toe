(ns ttt-be.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [cheshire.core :as json]
            [clojure.edn :as edn]
            [ttt-be.state :as s]
            [io.pedestal.http :as bootstrap]
            [ttt-be.service :as service]))

(use-fixtures :once (fn [f]
                      (reset! s/state s/initial-state)
                      (f)
                      (reset! s/state s/initial-state)))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))

(defn response-edn  [service type path body]
  (response-for service type path
                :headers {"Content-Type" "application/edn"}
                :body (pr-str body)))

(deftest register-player-test
  (let [resp1 (response-edn service :post "/register-player" {:name "test1"})
        body (-> resp1 :body edn/read-string)
        resp2 (response-edn service :post "/register-player" {:name "test1"})]
    (is (= 200 (:status resp1)))
    (is (= "test1" (:name body)))
    (is (uuid? (:id body)))
    (is (uuid? (:token body)))
    (is (= 400 (:status resp2)))
    (is (= 1 (count (:players @s/state))))))
