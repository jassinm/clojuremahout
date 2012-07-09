(ns clojuremahout.helpers
  (:import [org.apache.hadoop.io Writable]))

(defn pairs
  ([seq-file-reader]
     (let [key (.newInstance (.asSubclass (.getKeyClass seq-file-reader) Writable))
           val (.newInstance (.asSubclass (.getValueClass seq-file-reader) Writable))]
       (let [exists (.next seq-file-reader key val)]
         (if exists
           (lazy-seq (cons [key val] (pairs seq-file-reader)))
           nil)))))


(defn vector-to-seq
  ([v] (let [elems (iterator-seq (.iterator v))]
         (map (fn [elem] (.get elem)) elems))))


(defn pairdict-to-termdict
  ([v]
   (let [elems (iterator-seq (.iterator v))]
     (into {} (map (fn [elem] [(.get (second elem))
                                (str(.toString (first elem)))
                               ]) elems)))))

(defn vector-to-termweight-seq
  ([v dicseq]
   (let [elems (iterator-seq (.iterateNonZero v))]
     (map (fn [elem] {:weight (.get elem) :term (get dicseq (.index elem))}) elems)
     )))

(defn cluster-center
  ([c] (vector-to-seq (.getCenter c))))

(defn cluster-radius
  ([c] (vector-to-seq (.getRadius c))))

(defn gen-centermap
  ([clusterpair]
   (let [elems (iterator-seq (.iterator clusterpair))]
     (into {} (map (fn [[v c]](let [cluster (.getValue c)] [(.getId cluster) (.getCenter cluster)])) elems))
     )))
