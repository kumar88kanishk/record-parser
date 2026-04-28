(ns record-parser.core
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [java.time.format DateTimeFormatter]
           [java.time LocalDate]))

(def date-formatter (DateTimeFormatter/ofPattern "M/d/yyyy"))

(def output-date-formatter (DateTimeFormatter/ofPattern "MM/dd/yyyy"))

(defn parse-date 
  [date-str]
  (-> date-str
      str/trim
      (str/replace #"-" "/")
      (LocalDate/parse date-formatter)))

(defn parse-record 
  [line delimiter]
  (let [fields (map str/trim (str/split line delimiter))]
    (zipmap [:last-name :first-name :gender :color :dob]
            [(nth fields 0)
             (nth fields 1)
             (nth fields 2)
             (nth fields 3)
             (parse-date (nth fields 4))])))

(defn load-records [filename delimiter]
  (with-open [rdr (io/reader filename)]
    (doall (map #(parse-record % delimiter)
                (remove str/blank? (line-seq rdr))))))

(defn -main 
  []
  (let [comma-records (load-records "input/comma.txt" #",")
        pipe-records (load-records "input/pipe.txt" #"\|")
        space-records (load-records "input/space.txt" #"\s+")
        all-records (concat comma-records pipe-records space-records)]
    all-records))

(comment
  (-main)
  )

