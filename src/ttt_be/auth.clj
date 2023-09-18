(ns ttt-be.auth
  (:require [ttt-be.logic :as logic]
            [ring.util.response :as ring-resp]
            [better-cond.core :as b]))

(def RegisterPlayer [:map [:name :string]])

(defn register-player [context]
  (b/cond
    :let [state (:state context)
          name (-> context :body-params :name)]
    (logic/player-name-already-exists? state name)
    (assoc context :response
           {:status 400 :body "duplicate player name"})
    :let [player (logic/create-player name)
          state' (logic/add-player state player)]
    (assoc context
           :state state'
           :response {:status 200 :body "player added"})))

(defn verify-player [context]
  (b/cond
    :let [id (-> context :headers (get "Player-Id") str parse-uuid)
          token (-> context :headers (get "Player-Token") str parse-uuid)]
    (not (and id token)) (assoc context :response
                                {:status 401 :body "needs player auth headers"})
    :let [state (:state context)
          auth (logic/verify-player-data state id token)]
    (nil? auth) (assoc context :response
                       {:status 403 :body "invalid id/token"})
    auth (assoc context :player auth)))

(def verify-player-interceptor
  {:enter verify-player})

(def register-player-interceptor
  {:enter register-player})
