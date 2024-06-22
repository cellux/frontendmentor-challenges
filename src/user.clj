(ns user
  (:import [java.io File]
           [java.nio.file
            Files
            FileSystems
            SimpleFileVisitor
            FileVisitResult
            Path
            StandardWatchEventKinds
            WatchEvent$Kind
            ClosedWatchServiceException])
  (:require [clojure.java.io :as jio]
            [clojure.string :as str]
            [clj-reload.core :as reload]))

(def projects
  #{"qr-code-component-main"})

(def project-root
  (-> (jio/resource "user.clj")
      (jio/as-file)
      (.getParentFile)
      (.getParentFile)))

(def build-root
  (File. project-root "docs"))

(defn project-namespace
  [project-name]
  (-> "io.github.cellux.frontendmentor-challenges.%s.core"
      (format project-name)
      symbol))

(defn load-project-namespace
  [project-name]
  (require (project-namespace project-name)))

(defn build
  [project-name]
  (let [resource-prefix (-> "io.github.cellux.frontendmentor-challenges.%s"
                            (format project-name)
                            namespace-munge
                            (str/replace "." "/"))
        project-ns (-> "io.github.cellux.frontendmentor-challenges.%s.core"
                       (format project-name)
                       symbol)
        manifest-var (ns-resolve project-ns 'manifest)
        build-dir (File. build-root project-name)]
    (.mkdirs build-dir)
    (doseq [[op & args :as rule] @manifest-var]
      (case op
        :copy (let [[src & [dst]] args
                    input (cond (fn? src) (src)
                                (string? src) (-> (str resource-prefix "/" src)
                                                  jio/resource
                                                  jio/file)
                                :else (throw (ex-info "invalid rule"
                                                      {:rule rule})))
                    output (cond (string? dst) (File. build-dir dst)
                                 (nil? dst) (if (instance? java.io.File input)
                                              (File. build-dir (.getName input))
                                              (throw (ex-info "invalid rule"
                                                              {:rule rule}))))]
                (jio/copy input output))
        (throw (ex-info "invalid rule" {:rule rule}))))))

(defn build-all
  []
  (doseq [p projects]
    (load-project-namespace p)
    (build p)))

(defonce watch-service (atom nil))

(defn watch
  []
  (let [old-ws @watch-service]
    (when-not old-ws
      (reload/init {:dirs [(str (File. project-root "src"))]}))
    (when old-ws
      (.close old-ws))
    (let [fs (FileSystems/getDefault)
          ws (.newWatchService fs)
          root-path (.getPath fs (str project-root) (into-array String []))
          watched-subdirs ["src" "resources"]
          all-watch-event-kinds (into-array WatchEvent$Kind
                                            [StandardWatchEventKinds/ENTRY_CREATE
                                             StandardWatchEventKinds/ENTRY_MODIFY
                                             StandardWatchEventKinds/ENTRY_DELETE])
          log (fn [& args] (apply println "[watch]" (map str args)))]
      (doseq [path (map #(.resolve root-path %) watched-subdirs)]
        (Files/walkFileTree
         path
         (proxy [SimpleFileVisitor] []
           (postVisitDirectory [^Path dir exc]
             (.register dir ws all-watch-event-kinds)
             FileVisitResult/CONTINUE))))
      (reset! watch-service ws)
      (future
        (try
          (loop [key (.take ws)]
            (let [dir-path (.watchable key)]
              (doseq [event (.pollEvents key)]
                (let [file-path (.resolve dir-path (.context event))
                      project-name (.. file-path getParent getFileName toString (str/replace \_ \-))]
                  (log (.kind event) file-path project-name)
                  (when (contains? projects project-name)
                    (try
                      (load-project-namespace project-name)
                      (log "reloading namespaces")
                      (reload/reload)
                      (build project-name)
                      (log "rebuild succeeded:" project-name)
                      (catch Throwable e
                        (log "rebuild failed:" project-name))))))
              (.reset key)
              (recur (.take ws))))
          (catch ClosedWatchServiceException e)))
      (log (if old-ws "restarted" "started")))))
