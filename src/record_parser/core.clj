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

(def sb-gender
  (juxt (comp #{"Female" "Male"} :gender) :last-name))

(def sb-birth-date
  :dob)

(def sb-last-name-desc
  :last-name)

(def desc-compare
  (fn [a b]
    (compare b a)))

(defn display-view
  [msg records]
  (println (str "\n ---" msg "----"))
  (println records))

(def comma-records (load-records "input/comma.txt" #","))
(def pipe-records (load-records "input/pipe.txt" #"\|"))
(def space-records (load-records "input/space.txt" #"\s+"))
(def all-records (concat comma-records pipe-records space-records))

(def sort-by-gender (sort-by sb-gender all-records))
(def sort-by-birth-date (sort-by sb-birth-date all-records))
(def sort-by-last-name-desc (sort-by sb-last-name-desc all-records))

(defn cmd-line-display
  []
  (display-view "Output 1: By Gender (F first), then Last Name"
                sort-by-gender)
  (display-view "Output 2: By Birth Date (Ascending)"
                sort-by-birth-date)
  (display-view "Output 3: By Last Name (Descending)"
                sort-by-last-name-desc))

(comment
  (cmd-line-display)
  )





