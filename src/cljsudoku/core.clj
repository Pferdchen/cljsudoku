(ns cljsudoku.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(require '[clojure.java.io :as jio])
(with-open [rdr (jio/reader "resources/puzzles/puzzles1.txt")]
  (doseq [line (line-seq rdr)]
    (println line)))
