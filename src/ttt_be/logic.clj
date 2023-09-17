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
   [:waiting [:map-of :uuid [:map
                             [:connection any?]
                             [:time inst?]]]]
   ;; games by their uuid
   [:games [:map-of :uuid any?]]])

(def initial-state {:players {}
                    :waiting {}
                    :games {}})

(defn create-player [name]
  {:name name
   :id (random-uuid)
   :token (random-uuid)})

;;  functions for manipulating state  are state -> map containing :state, optional :error and :fx keys
;;  should it force players to have different names?
(defn add-player [state name]
  (let [player (create-player name)
        state (update state :players assoc (:id player) player)]
    {:state state}))

(defn remove-player [state id]
  {:state (-> state
              (update :players dissoc id)
              (update :waiting dissoc id))})

(defn confirm-player [state id token]
  (when-let [player (get (:players state) id)]
    (when (= (:token player) token)
      player)))

