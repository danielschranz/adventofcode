(ns advent-of-code.2022.day11-monkeyinthemiddle
  (:require [clojure.string :as str]
            [clojure.test :as t]
            [clojure.java.io :as io]))

(defn ^:private parse-input
  ([input]
   (->> (str/split input #"\n")))
  ([]
   (parse-input (slurp (io/resource "2022/day11.txt")))))

(def manual-input
  [{:items [72 64 51 57 93 97 68]
    :operation (partial * 19)
    :test #(= 0 (mod % 17))
    :true-target 4
    :false-target 7
    :count 0}
   {:items [62]
    :operation (partial * 11)
    :test #(= 0 (mod % 3))
    :true-target 3
    :false-target 2
    :count 0}
   {:items [57 94 69 79 72]
    :operation (partial + 6)
    :test #(= 0 (mod % 19))
    :true-target 0
    :false-target 4
    :count 0}
   {:items [80 64 92 93 64 56]
    :operation (partial + 5)
    :test #(= 0 (mod % 7))
    :true-target 2
    :false-target 0
    :count 0}
   {:items [70 88 95 99 78 72 65 94]
    :operation (partial + 7)
    :test #(= 0 (mod % 2))
    :true-target 7
    :false-target 5
    :count 0}
   {:items [57 95 81 61]
    :operation #(* % %)
    :test #(= 0 (mod % 5))
    :true-target 1
    :false-target 6
    :count 0}
   {:items [79 99]
    :operation (partial + 2)
    :test #(= 0 (mod % 11))
    :true-target 3
    :false-target 1
    :count 0}
   {:items [68 98 62]
    :operation (partial + 3)
    :test #(= 0 (mod % 13))
    :true-target 5
    :false-target 6
    :count 0}])

(def test-data
  [{:items [79 98]
    :operation (partial * 19)
    :test #(= 0 (mod % 23))
    :true-target 2
    :false-target 3
    :count 0}
   {:items [54 65 75 74]
    :operation (partial + 6)
    :test #(= 0 (mod % 19))
    :true-target 2
    :false-target 0
    :count 0}
   {:items [79 60 97]
    :operation #(* % %)
    :test #(= 0 (mod % 13))
    :true-target 1
    :false-target 3
    :count 0}
   {:items [74]
    :operation (partial + 3)
    :test #(= 0 (mod % 17))
    :true-target 0
    :false-target 1
    :count 0}])

(defn do-monkey
  [monkeys index]
  (if (empty? (->> (nth monkeys index) :items))
    monkeys
    (let [{[next-item & remaining-items] :items :keys [operation test true-target false-target] :as current-monkey} (nth monkeys index) ;; TODO
          updated-next-item (bigint (/ (operation next-item) 3))
          target-index (if (test updated-next-item)
                         true-target
                         false-target)
          target-monkey (nth monkeys target-index)]
      (recur (-> monkeys
                 (update-in [target-index :items] #(remove nil? (concat % [updated-next-item])))
                 (assoc-in [index :items] (or remaining-items []))
                 (update-in [index :count] inc))
             index))))

(defn do-round
  [monkeys]
  (reduce do-monkey monkeys (range (count monkeys))))

(defn day11a
  [input]
  (->> (reduce (fn [monkeys _count] (do-round monkeys)) input (range 20))
       (map :count)
       sort
       (take-last 2)
       (reduce *)))

(defn do-monkey-partb
  [monkeys index]
  (if (nil? (->> (nth monkeys index) :items first))
    monkeys
    (let [{[next-item & remaining-items] :items :keys [operation test true-target false-target] :as current-monkey} (nth monkeys index)
          updated-next-item (bigint (mod (operation next-item) (* 17 3 19 7 2 5 11 13)))
          target-index (if (test updated-next-item)
                         true-target
                         false-target)
          target-monkey (nth monkeys target-index)]
      (recur (-> monkeys
                 (update-in [target-index :items] #(concat % [updated-next-item]))
                 (assoc-in [index :items] remaining-items)
                 (update-in [index :count] inc))
             index))))

(defn do-round-partb
  [monkeys]
  (reduce do-monkey-partb monkeys (range (count monkeys))))

(defn day11b
  [input]
  (->> (reduce (fn [monkeys _round] (do-round-partb monkeys)) input (range 10000))
       (map :count)
       sort
       (take-last 2)
       (reduce *)))

;;;;;;;;;;;;;;;;;;;;;;;;;; Tests
(t/deftest day11a-test
  (t/is (= 99852 (day11a manual-input))))

(t/deftest ^:kaocha/pending day11b-test
  (t/is (= 25935263541
           (day11b manual-input))))
