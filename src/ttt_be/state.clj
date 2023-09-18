(ns ttt-be.state)

(def initial-state {:players {}
                    :waiting {}
                    :watching-queue {}
                    :games {}})

(def state (atom initial-state))

(def state-interceptor
  {:name ::state-interceptor
   :enter (fn [context]
            (assoc-in context [:request :state] @state))
   :leave (fn [context]
            (if-let [[op & args] (:tx-data context)]
              (do
                (apply swap! state op args)
                (assoc-in context [:request :database] @state))
              context))})
