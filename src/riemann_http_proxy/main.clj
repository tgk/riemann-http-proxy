(ns riemann-http-proxy.main
  (:require [riemann-http-proxy.api :as api]
            [clojure.tools.cli      :refer [parse-opts]]
            [clojure.string         :as string])
  (:gen-class))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 8123
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-s" "--server HOST" "Riemann server hostname"
    :default "localhost"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Usage: lein run [options]"
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn error-msg
  [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit
  [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]
  (let [{:keys [options errors summary]} (parse-opts args cli-options)]
    (cond
     (:help options) (exit 0 (usage summary))
     errors          (exit 1 (error-msg errors)))
    (println "Starting proxy on port" (:port options)
             "to Riemann server on"   (:server options))
    (println "Type \"X\" to quit")
    (let [stop-fn (api/start-server (:port options) (:server options))]
      (loop []
        (print "> ")
        (flush)
        (let [in (read-line)]
          (if (= in "X")
            (do (stop-fn :timeout 10000)
                (exit 0 "stopped"))
            (recur)))))))
