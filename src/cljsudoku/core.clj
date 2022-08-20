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

(defn str-to-vec
  "Converts a string to a vector of number."
  [s]
  (mapv (fn [x] (parse-int x)) (str/split s #"")))

(defn puzzle
  "Formatts a puzzle file to a vector of vector."
  ([] (puzzle "resources/puzzles/puzzle1.txt"))
  ([path]
   (with-open [rdr (jio/reader path)]
     (mapv (fn [line] (str-to-vec line)) (line-seq rdr)))))

(defn cell-item
  "Returns the item (number or nil) in the cell."
  [p i j]
  (nth (nth p i) j))

(defn vertical-axis-set
  [p i]
  (set (filter integer? (nth p i))))

(defn horizontal-axis-set
  [p j]
  (set (for [m (range 9)
             :let [ci (cell-item p m j)]
             :when (integer? ci)]
         ci)))

(defn region-set
  [p i j]
  (set (for [m (range (quot i 3) (+ (quot i 3) 3))
             n (range (quot j 3) (+ (quot j 3) 3))
             :let [ci (cell-item p m n)]
             :when (integer? ci)]
         ci)))

(defn nil-set
  "Returns a set, that contains all possible numbers for the empty cell."
  [p i j]
  (into (into (vertical-axis-set p i)
              (horizontal-axis-set p j))
        (region-set p i j)))

(defn cell-set
  [p i j]
  (if (nil? (cell-item p i j))
    #{1 2 3}
    #{}))

(defn solve
  "Solves a puzzle"
  [p]
  (println p))
