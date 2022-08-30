(ns cljsudoku.core-test
  (:require [clojure.test :refer :all]
            [cljsudoku.core :refer :all]))

(deftest count1s-of-test
  (testing "count1s-of"
    (is (= 0 (count1s-of 0)))   ; 000000000
    (is (= 1 (count1s-of 1)))   ; 000000001
    (is (= 2 (count1s-of 192))) ; 011000000
    (is (= 9 (count1s-of 511))) ; 111111111
    ))
