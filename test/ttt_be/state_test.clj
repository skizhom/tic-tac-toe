(ns ttt-be.state-test
  (:require [ttt-be.state :as sut]
            [malli.core :as m]
            [clojure.test :refer [deftest is]]))

(deftest initial-state-is-valid
  (is (m/validate sut/State sut/initial-state)))
