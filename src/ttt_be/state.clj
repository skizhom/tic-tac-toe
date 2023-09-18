(ns ttt-be.state)

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
  {:name ::state-interceptor
   :enter (fn [context]
            (assoc context :state (get!)))
   :leave (fn [context]
            (reset! state (-> context :state))
            context)})
