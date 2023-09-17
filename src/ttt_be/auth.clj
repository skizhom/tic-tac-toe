(ns ttt-be.auth
  (:require [ttt-be.logic :as logic]
            [ring.util.response :as ring-resp]
            [better-cond.core :as b]))

(defn verify-player [context]
  (b/cond
    :let [id (-> context :headers (get "Player-Id") str parse-uuid)
          token (-> context :headers (get "Player-Token") str parse-uuid)]
    (not (and id token)) (assoc context :response
                                {:status 401 :body "needs player auth headers"})
    :let [state (:state context)
          auth (logic/confirm-player state id token)]
    (nil? auth) (assoc context :response
                       {:status 403 :body "invalid id/token"})
    auth (assoc context :player auth)))

(def verify-player-interceptor
  {:enter verify-player})