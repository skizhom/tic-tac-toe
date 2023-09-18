(ns ttt-be.logic
  (:require [better-cond.core :as b]))

(defn create-player [name]
  {:name name
   :id (random-uuid)
   :token (random-uuid)})

(defn add-player [state player]
  (let [state (update state :players assoc (:id player) player)]
    state))

(defn player-name-already-exists? [state name]
  (let [matching (->> state
                      :players
                      vals
                      (filter #(= name (:name %))))]
    (pos? (count matching))))

(defn remove-player [state id]
  (-> state
      (update :players dissoc id)
      (update :waiting dissoc id)))

(defn verify-player-data [state id token]
  (when-let [player (get (:players state) id)]
    (when (= (:token player) token)
      player)))

;; there are three kinds of SSE 1. watch queue 2. subscribe 3. play game
