(ns ttt-be.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.params :as params]
            [better-cond.core :as b]
            ;; [io.pedestal.interceptor.helpers :as interceptor]
            [malli.core :as m]
            [malli.transform :as mt]
            [malli.error :as me]
            [ttt-be.auth :as auth]
            [ttt-be.state :as state]
            ;; [io.pedestal.http.ring-middlewares :as middleware]
            [ring.util.response :as ring-resp]))

(def unify-body-params
  {:enter (fn [{:keys [request] :as context}]
            (assoc-in context [:request :body-params] (or (:edn-params  request) (:json-params request))))})
(defn echo
  [request]
  (ring-resp/response (pr-str request)))

(defn verify-body [schema]
  {:name ::verify-body
   :enter (fn [context]
            (let [path [:request :body-params]
                  body (get-in context path)]
              (println body)
              (m/coerce schema body mt/json-transformer
                        (fn [value] (assoc-in context path value))
                        (fn [error]
                          (println error)
                          (->> error
                               :explain
                               me/humanize
                               pr-str
                               ring-resp/bad-request
                               (assoc context :response))))))})

;; Defines "/" and "/about" routes with their associated :get handlers.
;; The interceptors defined after the verb map (e.g., {:get home-page}
;; apply to / and its children (/about).
(def common-interceptors  [(body-params/body-params) unify-body-params state/state-interceptor])

(defn interceptors [& specific] (vec (concat common-interceptors specific)))

;; Tabular routes
(def routes
  #{["/register-player" :post
     (interceptors (verify-body auth/RegisterPlayer)
                   auth/register-player-interceptor)]})

;; Map-based routes
;(def routes `{"/" {:interceptors [(body-params/body-params) http/html-body]
;                   :get home-page
;                   "/about" {:get about-page}}})

;; Terse/Vector-based routes
;(def routes
;  `[[["/" {:get home-page}
;      ^:interceptors [(body-params/body-params) http/html-body]
;      ["/about" {:get about-page}]]]])

;; Consumed by ttt-be.server/create-server
;; See http/default-interceptors for additional options you can configure
(def service {:env :prod
              ;; You can bring your own non-default interceptors. Make
              ;; sure you include routing and set it up right for
              ;; dev-mode. If you do, many other keys for configuring
              ;; default interceptors will be ignored.
              ;; ::http/interceptors []
              ::http/routes routes

              ;; Uncomment next line to enable CORS support, add
              ;; string(s) specifying scheme, host and port for
              ;; allowed source(s):
              ;;
              ;; "http://localhost:8080"
              ;;
              ;;::http/allowed-origins ["scheme://host:port"]

              ;; Tune the Secure Headers
              ;; and specifically the Content Security Policy appropriate to your service/application
              ;; For more information, see: https://content-security-policy.com/
              ;;   See also: https://github.com/pedestal/pedestal/issues/499
              ;;::http/secure-headers {:content-security-policy-settings {:object-src "'none'"
              ;;                                                          :script-src "'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:"
              ;;                                                          :frame-ancestors "'none'"}}

              ;; Root for resource interceptor that is available by default.
              ::http/resource-path "/public"

              ;; Either :jetty, :immutant or :tomcat (see comments in project.clj)
              ;;  This can also be your own chain provider/server-fn -- http://pedestal.io/reference/architecture-overview#_chain_provider
              ::http/type :jetty
              ;;::http/host "localhost"
              ::http/port 8080
              ;; Options to pass to the container (Jetty)
              ::http/container-options {:h2c? true
                                        :h2? false
                                        ;:keystore "test/hp/keystore.jks"
                                        ;:key-password "password"
                                        ;:ssl-port 8443
                                        :ssl? false
                                        ;; Alternatively, You can specify your own Jetty HTTPConfiguration
                                        ;; via the `:io.pedestal.http.jetty/http-configuration` container option.
                                        ;:io.pedestal.http.jetty/http-configuration (org.eclipse.jetty.server.HttpConfiguration.)
                                        }})
