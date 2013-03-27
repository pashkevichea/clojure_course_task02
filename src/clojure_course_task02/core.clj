(ns clojure-course-task02.core
  (:gen-class)
  (:require [clojure.java.io :as io]))

(defn files-list [dir]
  (cons dir (mapcat deref (map #(future (files-list %1)) (.listFiles dir)))))


(defn find-files [file-name file]
  "Parallel Search for a file using his name as a regexp."
  (->> file 
    io/file
    files-list
    (pmap #(.getName %1))
    (filter #(re-matches (re-pattern file-name) %1))))

(defn usage [] (println "Usage: $ run.sh file_name path"))

(defn -main [file-name file]
  (if (or (nil? file) (nil? file-name))
    (usage)
    (do
      (println "Searching for " file-name " in " file "...")
      (dorun (map println (find-files file-name file)))
    (shutdown-agents))))
