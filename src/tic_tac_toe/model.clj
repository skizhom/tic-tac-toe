(ns tic-tac-toe.model
  (:require [clojure.spec.alpha :as s]))

(s/def ::field #{:o :x nil})
(s/def ::board (s/coll-of (s/coll-of ::field :kind vector?) :kind vector?))
(s/def ::turn #{:o :x})
(s/def ::game (s/keys :req-un [::board ::turn]))

(def initial-state {})
