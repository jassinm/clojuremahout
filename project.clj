(defproject clojuremahout "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.apache.mahout/mahout-core "0.7"]
                 [org.apache.mahout/mahout-math "0.7"]
                 [org.apache.mahout/mahout-utils "0.5"]
                 ; [org.apache.mahout/mahout-core "0.8-SNAPSHOT"]
                 ; [org.apache.mahout/mahout-math "0.8-SNAPSHOT"]
                 [org.apache.hadoop/hadoop-core "0.20.2-cdh3u3"]
                 [org.apache.hadoop.thirdparty/guava "r09"]
                 ]
  :jvm-opts ["-Xmx10g"]
  :main clojuremahout.core)

