(ns record-parser.api-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [record-parser.api :refer [app records-db]]
            [cheshire.core :as json]))

;; --- Fixtures ---

(defn reset-db-fixture [f]
  ;; Clear the in-memory DB before every test case
  (reset! records-db [])
  (f))

(use-fixtures :each reset-db-fixture)

;; --- Helper ---

(defn parse-json [body]
  (json/parse-string body true))

;; --- Tests ---

(deftest test-post-records
  (testing "POST /records with pipe format"
    (let [payload {:record "Smith | John | Male | Blue | 1/1/1980"}
          response (app (-> (mock/request :post "/records")
                            (mock/json-body payload)))]
      (is (= 201 (:status response)))
      (is (= "John" (:first-name (parse-json (:body response)))))
      (is (= 1 (count @records-db)))))

  (testing "POST /records with comma format"
    (let [payload {:record "Hopper, Grace, Female, Green, 12/9/1906"}
          response (app (-> (mock/request :post "/records")
                            (mock/json-body payload)))]
      (is (= 201 (:status response)))
      (is (= "Grace" (:first-name (parse-json (:body response))))))))

(deftest test-get-endpoints
  ;; Seed the atom with data for sorting tests
  (reset! records-db
          [{:last-name "Zebra" :gender "Male" :dob "01/01/1990"}
           {:last-name "Apple" :gender "Female" :dob "01/01/1950"}])

  (testing "GET /records/gender"
    (let [response (app (mock/request :get "/records/gender"))
          body (parse-json (:body response))]
      (is (= 200 (:status response)))
      (is (= "Female" (:gender (first body))))
      (is (= "Apple" (:last-name (first body))))))

  (testing "GET /records/birthdate"
    (let [response (app (mock/request :get "/records/birthdate"))
          body (parse-json (:body response))]
      (is (= "01/01/1950" (:dob (first body))))))

  (testing "GET /records/name"
    (let [response (app (mock/request :get "/records/name"))
          body (parse-json (:body response))]
      (is (= "Apple" (:last-name (first body))))
      (is (= "Zebra" (:last-name (second body)))))))

(deftest test-routing-errors
  (testing "404 on unknown route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= 404 (:status response))))))