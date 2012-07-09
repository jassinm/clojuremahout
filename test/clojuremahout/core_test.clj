(ns clojuremahout.core-test
  (:use clojure.test
        [clojuremahout.core :as mahout]
        [clojuremahout.hadoop :only [bootstrap!]])
  (:require [clojure.set :as cset]))


(bootstrap! (str (System/getenv "HADOOP_CONF_DIR") "/core-site.xml"))
(def clusterpath "enron2-kmeans-clusters/clusters-20-final/part-r-00000")
(def dictionarypath "enron2-vectors/dictionary.file-0")
(def pointspath "enron2-kmeans-clusters/clusteredPoints/part-m-00000")

(deftest clusterdump
  (let [data (mahout/dump-clusters clusterpath dictionarypath)]
  (is (not-empty data))
  (is (empty? (cset/difference #{:clusterid :isconverged :terms} (set (keys (first data))))))
  (is (= 20 (count (:terms (first data)))))))

(deftest pointsdump
  (let [data (mahout/dump-clustered clusterpath pointspath)]
  (is (not-empty data))
  (is (empty? (cset/difference #{:clusterid :component :distance} (set (keys (first data))))))))
