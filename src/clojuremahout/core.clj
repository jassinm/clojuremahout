(ns clojuremahout.core
  (:import [org.apache.mahout.common.distance SquaredEuclideanDistanceMeasure])
  (:use [clojuremahout.hadoop :only [bootstrap! seq-file-reader]])
  (:require [clojuremahout.helpers :as mh])
  )

(defn pair-to-clustered-vector
  ([[k v] cidcenter]
   (let [pvec (.getVector v)
         clusterid (.get k)
         ccenter (get cidcenter clusterid)]
     {:clusterid clusterid
      :component (.getName pvec)
      :distance (.distance (new SquaredEuclideanDistanceMeasure) ccenter pvec)})))

(defn pair-to-cluster
  [[k c] termdict n]
  (let [clust  (.getValue c)]
           {:clusterid (.getId clust)
            :isconverged (.isConverged clust)
            ; :center (mh/cluster-center clust)
            ; :radius (mh/cluster-radius clust)
            :terms (take n (sort-by :weight > (mh/vector-to-termweight-seq (.getCenter clust) termdict)))
            }))

(defn dump-clusters
  "returns sequence of cluster's"
  ([clusterpath dictionaryfilepath] (dump-clusters clusterpath dictionaryfilepath 20))
  ([clusterspath dictionaryfilepath toptermcount]
    (let [clusters (seq-file-reader clusterspath)
          dictionaryfile (seq-file-reader dictionaryfilepath)
          termdict (mh/pairdict-to-termdict (mh/pairs dictionaryfile))]
      (map #(pair-to-cluster % termdict toptermcount) (mh/pairs clusters)))))



(defn dump-clustered
  "returns sequence of clustered points and their
  associated cluster"
  [clusterpath pointspath]
  (let [clusters (seq-file-reader clusterpath)
        points (seq-file-reader pointspath)
        cidcenter (mh/gen-centermap (mh/pairs clusters))]
    (map #(pair-to-clustered-vector % cidcenter) (mh/pairs points))
    ))
