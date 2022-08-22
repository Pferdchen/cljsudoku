(ns cljsudoku.sudoku
  (:require [clojure.string :as str]))

(defn bar
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(require '[clojure.java.io :as jio])
(with-open [rdr (jio/reader "resources/puzzles/puzzle1.txt")]
  (doseq [line (line-seq rdr)]
    (println line)))

(defn parse-int
  "Converts string s to its corresponding number, if not return nil."
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
    nil))

(defn- str-to-vec
  "Converts a string to a vector of number."
  [s]
  (mapv (fn [x] (parse-int x)) (str/split s #"")))

(defn puzzle
  "Formatts a puzzle file to one vector."
  ([] (puzzle "resources/puzzles/puzzle1.txt"))
  ([path]
   (with-open [rdr (jio/reader path)]
     (str-to-vec (apply str (line-seq rdr))))))

(def V [1 2 4 8 16 32 64 128 256 511])

(defn- dec-to-bin-str
  "Returns binary form of an integer, i.e. 2 -> 10."
  [dec]
  (Integer/toString dec 2))

(defn dec-to-9bin-str
  "Returns 9-bit binary form of an integer, i.e. 2 -> 000000010."
  [dec]
  (str/replace (format "%9s" (dec-to-bin-str dec)) #" " "0"))

(def V-BIN
  (mapv dec-to-9bin-str V))

(defn- count-1-of-value
  "Calculates how many 1s are in a positive binary integer.
  See https://www.cnblogs.com/grenet/archive/2011/06/10/2077228.html"
  [value]
  (loop [v value, c 0]
    (if (== v 0)
      c
      (recur (bit-and v (- v 1)) (inc c)))))
