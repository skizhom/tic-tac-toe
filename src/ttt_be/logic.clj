(ns ttt-be.logic
  (:require [better-cond.core :as b]
            [malli.core :as m]))

(def Player
  "schema for player, both waiting in queue and in general"
  [:map
   [:name [:string {:min 2 :max 50}]]
   [:id :uuid]
   [:token :uuid]]) ; no reason for better security

(def MetaGame
  [:map
   :players [:map
             ; probably abstract connection?
             [:x :uuid]
             [:o :uuid]]
   :current-game any?                   ; game state goes here
   :score [:map
           [:x :int]
           [:o :int]]])

(def State
  "schema for application state"
  [:map
   ;; players by uuid
   [:players [:map-of :uuid Player]]
   ;;
   [:waiting
    [:map-of :uuid [:map
                    [:connection any?]]]]
   [:watching-queue
    [:map-of :uuid [:map
                    [:connection any?]]]]
   ;; games by their uuid
   [:games [:map-of :uuid any?]]])

(def initial-state {:players {}
                    :waiting {}
                    :watching-queue {}
                    :games {}})

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
