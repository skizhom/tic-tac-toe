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

;;  functions for manipulating state  are state -> {:state, :fx}
