(ns record-parser.api
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [record-parser.core :as core]
            [clojure.string :as str]
            ))

;; In-memory storage
(defonce records-db (atom []))

(defn detect-delimiter [s]
  (cond
    (str/includes? s "|") #"\|"
    (str/includes? s ",") #","
    :else #"\s+"))

(defroutes app-routes
  (POST "/records" {:keys [body]}
    ;; Expects JSON: {"record": "LastName | FirstName | Gender | Color | Date"}
    (let [line (get body "record")
          delim (detect-delimiter line)
          new-record (core/parse-record line delim)]
      (swap! records-db conj new-record)
      {:status 201 :body new-record}))

  (GET "/records/gender" []
    {:status 200 :body (sort-by core/sb-gender @records-db)})

  (GET "/records/birthdate" []
    {:status 200 :body (sort-by core/sb-birth-date @records-db)})

  (GET "/records/name" []
    {:status 200 :body (sort-by :last-name @records-db)})

  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-json-body)
      (wrap-json-response)))

(defn -main [& args]
  (println "Server starting on port 3000...")
  (run-jetty app {:port 3000 :join? false}))

(-main)