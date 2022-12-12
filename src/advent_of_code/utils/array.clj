(ns advent-of-code.utils.array
  (:require [clojure.test :as t]))

(def ^:dynamic *height* 2)
(def ^:dynamic *width* 2)

(defn array-get
  [x y grid]
  (nth grid (+ x (* *width* y))))

(def test-data [:a :b :c :d :e :f :g :h :i :j :k :l])

(t/deftest test-array-get
  (binding [*height* 3
            *width* 4]
    (t/is (= :f (array-get 1 1 test-data)))
    (t/is (= :j (array-get 1 2 test-data))))
  (binding [*height* 4
            *width* 3]
    (t/is (= :e (array-get 1 1 test-data)))
    (t/is (= :h (array-get 1 2 test-data)))))

#_(defn array-update
  ())
