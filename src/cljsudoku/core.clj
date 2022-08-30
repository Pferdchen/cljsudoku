(ns cljsudoku.core
  (:require [clojure.string :as str]))

;; the 1's position corresponds a number between 1 and 9
(def V [1,   ;; V[0]=000000001
        2,   ;; V[1]=000000010
        4,   ;; V[2]=000000100
        8,   ;; V[3]=000001000
        16,  ;; V[4]=000010000
        32,  ;; V[5]=000100000
        64,  ;; V[6]=001000000
        128, ;; V[7]=010000000
        256, ;; V[8]=100000000
        511  ;; V[9]=111111111
        ])

;;(println V)

;; vector-of for better performance for a single primitive type
(def NUM (apply vector-of :int (repeat 81 (last V))))

;;(println NUM)

(defn- index-of
  [row col]
  (+ (* row 9) col))

(defn- position-of
  [index]
  (let [row (quot index 9)
        col (mod index 9)]
    [row col]))

(defn- get-1s-count
  "Gets the count of 1s. Example: (1, 1)=011000000, it meas the cell contains 2
  possible numbers, 7 and 8, get1Count(011000000)=2.
  See https://www.cnblogs.com/grenet/archive/2011/06/10/2077228.html"
  [cell-num] ;; its binary form corresponds possibilities of numbers in a cell
  (loop [value cell-num, c 0]
    (if (== value 0)
      c
      (recur (bit-and value (- value 1)) (inc c)))))

;;(println (get-1s-count 511))

(defn- get-possible-num-in-cell
  [cell-num index-of-possibilities]
  (loop [k 0, index-of-v 0]
    (if (== index-of-v 9)
      -1
      (let [v-num (nth V index-of-v)]
        (if (zero? (bit-and v-num cell-num))
          (recur k (inc index-of-v))
          (if (not= (inc k) index-of-possibilities)
            (recur (inc k) (inc index-of-v))
            (inc index-of-v)))))))

;;(time (println (get-possible-num-in-cell 192 2))) ;; 192=011000000

;; a littel quicker than the first version
(defn- get-possible-num-in-cell2
  [cell-num index-of-possibilities]
  (loop [n cell-num, c 0, r 0]
    (if (== c index-of-possibilities)
      r
      (if (> n 0)
        (if (zero? (mod n 2))
          (recur (quot n 2) c (inc r))
          (recur (quot n 2) (inc c) (inc r)))
        -1))))

;;(time (println (get-possible-num-in-cell2 192 2)))

(defn remove-num
  "Removes a number from the state of a cell."
  [num-vec row col ones-complement]
  (let [index (index-of row col)
        state (nth num-vec index)
        new-state (bit-and state ones-complement)]
    (if (pos? state)
      [new-state (assoc num-vec index new-state)]
      [state num-vec])))

(println (remove-num NUM 0 0 1))

(defn set-num-pri
  "FIXME"
  [num-vec row col index-of-v]
  (let [num-of-v (nth V index-of-v)
        index (index-of row col)
        state (nth num-vec index)]
    (if (zero? (bit-and num-of-v state))
      [false num-vec]
      (do (println "FIXME")))))

;;;; following deprecated

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

(defn- str-to-vec
  "Converts a string to a vector of number."
  [s]
  (mapv (fn [x] (parse-int x)) (str/split s #"")))

(defn puzzle
  "Formatts a puzzle file to a vector of vector."
  ([] (puzzle "resources/puzzles/puzzle1.txt"))
  ([path]
   (with-open [rdr (jio/reader path)]
     (mapv (fn [line] (str-to-vec line)) (line-seq rdr)))))

(def full-set (set (range 1 9)))

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

(defn- start-end
  [index]
  (let [start (* (quot index 3) 3)
        end (+ start 3)]
    [start end]))

(defn region-set
  [p i j]
  (set (for [m (apply range (start-end i))
             n (apply range (start-end j))
             :let [ci (cell-item p m n)]
             :when (integer? ci)]
         ci)))

(defn nil-set
  "Returns a set, that contains all possible numbers for the empty cell."
  [p i j]
  (apply disj (apply disj (apply disj full-set
                                 (vertical-axis-set p i))
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

(defn puzzle-to-vec
  "Formatts a puzzle file to one vector."
  ([] (puzzle-to-vec "resources/puzzles/puzzle1.txt"))
  ([path]
   (with-open [rdr (jio/reader path)]
     (str-to-vec (apply str (line-seq rdr))))))

(defn- find-index
  "Returns an index of vector from a puzzle position."
  [i j]
  (+ (* i 9) j))

(defn find-item
  "Finds an item from puzzle vector."
  [p-vec i j]
  (nth p-vec (find-index i j)))

(defn possibilities
  "Returns a set of possible number."
  [i j]
  ())
