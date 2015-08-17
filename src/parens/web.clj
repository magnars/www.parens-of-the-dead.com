(ns parens.web
  (:require [clojure.string :as str]
            [optimus.assets :as assets]
            [optimus.assets.load-css :refer [external-url?]]
            [optimus.export]
            [optimus.link :as link]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [parens.content :refer [load-content]]
            [parens.layout :refer [render-page]]
            [parens.pages :as pages]
            [parens.rss :as rss]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis]))

(defn get-assets []
  (assets/load-assets "public" ["/styles/responsive.css"
                                "/styles/unresponsive.css"
                                #"/img/.+\..+"]))

(defn- use-optimized-assets [html request]
  (str/replace html "/img/header-logo.png"
               (link/file-path request "/img/header-logo.png")))

(defn- prepare-page [page content request]
  (-> page
      (render-page content request)
      (use-optimized-assets request)))

(defn update-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn get-pages []
  (let [content (load-content)]
    (-> content
        pages/get-pages
        (update-vals #(partial prepare-page % content))
        (merge {"/atom.xml" (rss/atom-xml (:seasons content))}))))

(def optimize optimizations/all)

(def app (-> (stasis/serve-pages get-pages)
             wrap-exceptions
             (optimus/wrap get-assets optimize serve-live-assets)
             wrap-content-type))

(def export-directory "./build")

(defn- load-export-dir []
  (stasis/slurp-directory export-directory #"\.[^.]+$"))

(defn export []
  (let [assets (optimize (get-assets) {})
        old-files (load-export-dir)]
    (stasis/empty-directory! export-directory)
    (optimus.export/save-assets assets export-directory)
    (stasis/export-pages (get-pages) export-directory {:optimus-assets assets})
    (println)
    (println "Export complete:")
    (stasis/report-differences old-files (load-export-dir))
    (println)))
