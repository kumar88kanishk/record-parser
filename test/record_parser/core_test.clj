(ns record-parser.core-test
  (:require [clojure.test :refer :all]
            [record-parser.core :refer :all]
            [clojure.java.io :as io]))

;; --- Unit Tests for Parsing ---

(deftest test-parse-date
  (testing "Standard slashes"
    (is (= "05/05/1990" (parse-date "5/5/1990"))))

  (testing "Hyphen conversion"
    (is (= "12/01/1980" (parse-date "12-1-1980"))))

  (testing "Trimming whitespace"
    (is (= "01/01/2000" (parse-date "  1/1/2000  ")))))

(deftest test-parse-record
  (testing "Comma delimited parsing"
    (let [record (parse-record "Smith, John, Male, Blue, 1/1/1980" #",")]
      (is (= "Smith" (:last-name record)))
      (is (= "01/01/1980" (:dob record)))))

  (testing "Pipe delimited parsing with extra spaces"
    (let [record (parse-record "Doe | Jane | Female | Red | 5-5-1995" #"\|")]
      (is (= "Jane" (:first-name record)))
      (is (= "Female" (:gender record)))
      (is (= "05/05/1995" (:dob record))))))

;; --- Unit Tests for Sorting ---

(deftest test-sorting-logic
  (let [data [{:last-name "Zebra" :gender "Male" :dob "01/01/1990"}
              {:last-name "Apple" :gender "Male" :dob "01/01/1980"}
              {:last-name "Candle" :gender "Female" :dob "01/01/1985"}]]

    (testing "Sort by Gender (Females first) then Last Name"
      (let [sorted (sort-by sb-gender data)]
        (is (= "Candle" (:last-name (first sorted))))
        (is (= "Apple" (:last-name (second sorted))))
        (is (= "Zebra" (:last-name (last sorted))))))

    (testing "Sort by Birth Date Ascending"
      (let [sorted (sort-by sb-birth-date data)]
        (is (= "01/01/1980" (:dob (first sorted))))
        (is (= "01/01/1990" (:dob (last sorted))))))

    (testing "Sort by Last Name Descending"
      (let [sorted (sort-by sb-last-name-desc desc-compare data)]
        (is (= "Zebra" (:last-name (first sorted))))
        (is (= "Apple" (:last-name (last sorted))))))))