(ns tic-tac-toe.model
  (:require [clojure.spec.alpha :as s]))

(s/def ::field #{:o :x nil})
(s/def ::board (-> ::field
                   (s/coll-of :kind vector? :count 3)
                   (s/coll-of :kind vector? :count 3)))
(s/def ::turn #{:o :x})
(s/def ::game (s/keys :req-un [::board ::turn]))

(def initial-state {})

(defn click [state position])

(defn victor [state])
