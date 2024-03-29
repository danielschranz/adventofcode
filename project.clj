(defproject advent-of-code "0.1.0-SNAPSHOT"
  :description "Advent of code puzzles in Clojure."
  :url ""
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot advent-of-code.core
  :test-paths ["src"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[lambdaisland/kaocha "1.71.1119"]]}}
  :aliases {"kaocha" ["run" "-m" "kaocha.runner"]
            "test"  ["do" "kaocha"]})
