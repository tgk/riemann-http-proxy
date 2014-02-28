(ns riemann-http-proxy.api
  (:require [org.httpkit.server :refer [run-server]]
            [clojure.edn        :as edn]
            [byte-streams       :refer [convert]]
            [riemann.client     :refer [tcp-client send-event]]
            [clojure.pprint :as pprint]))

(defn handler
  [client req]
  (let [body-str (convert (:body req) String)
        event (edn/read-string body-str)]
    (send-event client event)
    {:status 200
     :body   "delivered"}))

(defn start-server
  "Returns a function for stopping the server."
  [port riemann-server]
  (let [client (tcp-client :host riemann-server)]
    (run-server (partial handler client)
                {:port port})))

;; development

(defn debug-handler
  [req]
  (handler (tcp-client :host "localhost") req))

(comment



  (def s (run-server #'debug-handler {:port 8080}))
(s)

  )
