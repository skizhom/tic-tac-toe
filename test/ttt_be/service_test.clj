(ns ttt-be.service-test
  (:require [clojure.test :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [ttt-be.service :as service]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet service/service)))
(defn response-edn  [service type path body]
  (response-for service type path
                :headers {"Content-Type" "application/edn"}
                :body (pr-str body)))
(deftest test-test
  (is (= nil (response-edn service :post "/test" {:name "kitty"}))))
