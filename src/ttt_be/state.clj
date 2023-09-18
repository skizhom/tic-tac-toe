(ns ttt-be.state)

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

(def state (atom initial-state))

;; (defn state-swap! [f & args]
;;   (swap! state (fn [old]
;;                  (apply f (:state old) args))))

(defn get! [] (:state @state))

(def state-interceptor
  {:enter (fn [context]
            (assoc context :state (get!)))
   :leave (fn [context]
            (reset! state (-> context :state)))})
