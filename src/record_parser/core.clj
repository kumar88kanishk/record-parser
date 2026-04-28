(ns record-parser.core
  (:require [clojure.string as str]
            [clojure.java.io as io])
  (:import [java.time.format DateTimeFormatter]
           [java.time LocalDate]))


(defn load-records [file delimeter]
  (with-open [rdr (io/reader file)]
    (doseq [line (line-seq rdr)]
      (println line))))

(def -main [& args]
  (load-records "input/commas.txt" ","))
