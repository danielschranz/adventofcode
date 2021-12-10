(ns advent-of-code.core
  (:gen-class)
  (:require [clojure.string :as str]
            [advent-of-code.2021]))

(def project-namespaces (->> (all-ns)
                             (filter #(re-matches #"advent-of-code\.....\.day.*" (str %)))
                             (sort-by str)))

(defn ^:private execute-exercises [executee-ns]
  ((ns-resolve executee-ns 'run)))

(defn -main
  "TODO call and execute sub-namespaces."
  [& args]
  (doall (->>  project-namespaces
               (map execute-exercises))))
