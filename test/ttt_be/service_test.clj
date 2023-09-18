(ns ttt-be.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
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
  (is (= 200 (:status (response-edn service :post "/register-player" {:name "test1"})))))
