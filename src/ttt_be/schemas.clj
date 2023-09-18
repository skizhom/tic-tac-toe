(ns ttt-be.schemas)

(def PlayerName [:string {:min 2 :max 50}])

(def Player
  "schema for player, both waiting in queue and in general"
  [:map
   [:name PlayerName]
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

;; for requests
(def RegisterPlayer [:map [:name PlayerName]])
