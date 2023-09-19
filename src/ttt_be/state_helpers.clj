(ns ttt-be.state-helpers
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

(defn add-player-to-waiting-list [state uuid send-fn]
  (update state :waiting assoc uuid {:connection send-fn}))

(defn remove-player-from-waiting-list [state uuid]
  (update state :waiting dissoc uuid))
