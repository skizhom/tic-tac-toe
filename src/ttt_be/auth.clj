(ns ttt-be.auth
  (:require [ttt-be.logic :as logic]
            [ring.util.response :as ring-resp]
            [better-cond.core :as b]))

(defn register-player [context]
  (b/cond
    :let [state (get-in context [:request :state])
          name (-> context :request :body-params :name)]
    (logic/player-name-already-exists? state name)
    (assoc context :response (ring-resp/bad-request "duplicate player name"))
    :let [player (logic/create-player name)]
    (assoc context
           :tx-data [logic/add-player player]
           :response (ring-resp/response player))))

(defn unauthorized [b] {:status 401 :headers {} :body b})

(defn verify-player [context]
  (b/cond
    :let [id (-> context :headers (get "Player-Id") str parse-uuid)
          token (-> context :headers (get "Player-Token") str parse-uuid)]
    (not (and id token)) (assoc context :response
                                (unauthorized "needs player auth headers"))
    :let [state (get-in context [:request :state])
          auth (logic/verify-player-data state id token)]
    (nil? auth) (assoc context :response
                       (unauthorized "invalid id/token"))
    auth (assoc context :player auth)))

(def verify-player-interceptor
  {:name ::verify-player
   :enter verify-player})

(def register-player-interceptor
  {:name :register-player
   :enter register-player})
