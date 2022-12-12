(ns advent-of-code.2022.day09-ropebridge
  (:require [clojure.string :as str]
            [clojure.test :as t]
            [clojure.java.io :as io]))

(defn ^:private parse-input
  ([input]
   (->> (str/split input #"\n")
        (map (fn [line] (map #(identity {:height (bigint (str %)) :visible false}) line)))))
  ([]
   (parse-input (slurp (io/resource "2022/day08.txt")))))

(defn transpose
  [grid]
  (apply map #(identity %&) grid))

(defn mark-visibility
  [series index]
  (let [before (take index series)
        current (nth series index)
        after (drop (inc index) series)
        max-height-before (apply max (cons -1 (map :height before))) ;; cons -1 to make sure there is always one element for max to work with
        max-height-after (apply max (cons -1 (map :height after)))]
    (if (or (> (:height current) max-height-before)
            (> (:height current) max-height-after))
      (merge current {:visible true})
      current)))

(defn mark-series-visibility
  [series]
  (map-indexed (fn [i _x] (mark-visibility series i)) series))

(defn day08a
  [input]
  (->> input
       (map mark-series-visibility)
       transpose
       (map mark-series-visibility)
       flatten
       (filter :visible)
       count))

(defn get-lanes
  "get an ordered seq of tree heights for each of the four directions - starting with nearest to furthest"
  [grid x y]
  (let [rows-above (take y grid)
        rows-below (drop (inc y) grid)
        row (nth grid y)]
    (vector
     (->> row (take x) reverse)
     (->> row ((partial drop (inc x))))
     (->> rows-above (map #(nth % x)) reverse)
     (->> rows-below (map #(nth % x))))))

(defn calc-lane-scenic-score
  [height lane]
  (reduce (fn [dist next-height]
            (if (> height next-height)
              (inc dist)
              (reduced (inc dist))))
          0
          lane))

(defn calc-scenic-score
  [grid x y]
  (let [current-height (-> grid (nth y) (nth x) :height)]
    (->> (get-lanes grid x y)
         (map #(map :height %))
         (map (partial calc-lane-scenic-score current-height))
         (reduce *))))

(defn day08b
  [input]
  (->> input
       (map-indexed (fn [y row]
                      (map-indexed (fn [x _col] (calc-scenic-score input x y)) row)))
       flatten
       (apply max)))

;;;;;;;;;;;;;;;;;;;;;;;;;; Tests
(t/deftest day08a-test
  (t/is (= 1693 (day08a (parse-input)))))

(t/deftest  day08b-test
  (t/is (= 422059 (day08b (parse-input)))))

(def ^:dynamic *testvar* nil)

(defn xla
  []
  (prn *testvar*))

(binding [*testvar* "outer"]
  (xla)
  (binding [*testvar* "inner"]
    (xla))
  (xla))
