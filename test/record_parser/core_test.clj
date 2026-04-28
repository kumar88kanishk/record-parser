(ns record-parser.core-test
  (:require [clojure.test :refer [deftest is testing]]
            [record-parser.core :refer [load-records parse-date parse-record]]))

(deftest parse-date-trims-and-parses
  (testing "parse-date handles leading whitespace and date separators"
    (is (= "1943-02-13" (str (parse-date " 2/13/1943"))))))

(deftest parse-pipe-delimited-record
  (testing "parse-record parses pipe-delimited records correctly"
    (let [record (parse-record "Smith | Steve | Male | Red | 3/3/1985" #"\\|")]
      (is (= "Smith" (:last-name record)))
      (is (= "Steve" (:first-name record)))
      (is (= "3/3/1985" (format "%d/%d/%d" (.getMonthValue (:dob record)) (.getDayOfMonth (:dob record)) (.getYear (:dob record))))))))

(deftest parse-space-delimited-record
  (testing "parse-record parses space-delimited records correctly"
    (let [record (parse-record "Kuni Hiroshi Male Blue 1/5/1970" #"\s+")]
      (is (= "Kuni" (:last-name record)))
      (is (= "Hiroshi" (:first-name record)))
      (is (= "1/5/1970" (format "%d/%d/%d" (.getMonthValue (:dob record)) (.getDayOfMonth (:dob record)) (.getYear (:dob record))))))))

(deftest load-records-skips-blank-lines
  (testing "load-records ignores blank lines in input"
    (let [tmp-file (doto (java.io.File/createTempFile "records" ".txt")
                     (spit "Smith | Steve | Male | Red | 3/3/1985\n\n"))
          records (load-records (.getPath tmp-file) #"\|")]
      (is (= 1 (count records)))
      (.delete tmp-file))))
