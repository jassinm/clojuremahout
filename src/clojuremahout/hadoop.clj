(ns clojuremahout.hadoop
  (:import [org.apache.hadoop.conf Configuration]
           [org.apache.hadoop.fs FileSystem Path]))

;; Haddop Configuration
(def *fs* nil)
(def *conf* nil)

(defn path
  ([path-str] (Path. path-str)))

(defn configuration
  ([] (Configuration.))
  ([path-str & others]
     (let [conf (Configuration.)]
       (.addResource conf (path path-str))
       (loop [pstr others]
         (if (empty? others)
           conf
           (do (.addResource conf (path (first pstr)))
               (recur (rest pstr))))))))

(defn file-system
  ([] (FileSystem/get *conf*)))

(defn set-file-system!
  ([]
     (let [fs (file-system)]
       (alter-var-root #'*fs* (constantly fs)))))

(defn set-config!
  ([] (let [conf (configuration)]
        (alter-var-root #'*conf* (constantly conf))))
  ([& paths] (let [conf (apply configuration paths)]
               (alter-var-root #'*conf* (constantly conf)))))

(defn bootstrap!
  ([] (do (println "default bootstrap")
          (set-config!)
          (set-file-system!)
          :ok))
  ([& config-files]
     (if (or (empty? config-files) (nil? (first config-files)))
       (bootstrap!)
       (do (apply set-config! config-files)
           (set-file-system!)
           :ok))))

(defn seq-file-reader
  ([path-str]
     (println (str "FS:" *fs*))
     (println (str "PATH:" path))
     (println (str "CONF:" *conf*))
     (org.apache.hadoop.io.SequenceFile$Reader. *fs* (path path-str) *conf*)))

