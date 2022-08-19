(ns cljsudoku.core
  (:require [clojure.string :as str]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(require '[clojure.java.io :as jio])
(with-open [rdr (jio/reader "resources/puzzles/puzzle1.txt")]
  (doseq [line (line-seq rdr)]
    (println line)))

(defn puzzle
  "Formatts a puzzle file to a vector of vector."
  ([] (puzzle "resources/puzzles/puzzle1.txt"))
  ([path]
   (with-open [rdr (jio/reader path)]
     (mapv (fn [line] (extract line)) (line-seq rdr)))))

(defn extract
  "Converts a string to a vector of number."
  [s]
  (mapv (fn [x] (str-to-num x)) (str/split s #"")))

(defn str-to-num
  "Converts string s to its corresponding number, if not return 0."
  [s]
  (case s
    "1" 1
    "2" 2
    "3" 3
    "4" 4
    "5" 5
    "6" 6
    "7" 7
    "8" 8
    "9" 9
    0))

