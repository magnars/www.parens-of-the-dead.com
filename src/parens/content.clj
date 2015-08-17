(ns parens.content
  (:require [clojure.java.io :as io]
            [helpful-loader.edn :as edn-loader]))

(defn- add-seasons-info-to-each-episode [season]
  (update-in season [:episodes]
             #(mapv (fn [e color] (assoc e
                                         :prefixes (:prefixes season)
                                         :color color))
                    % (cycle ["green" "blue" "red"]))))

(defn load-content []
  {:seasons (->> (edn-loader/load-one "episodes.edn")
                 (mapv add-seasons-info-to-each-episode))
   :disqus-html (slurp (io/resource "disqus.html"))
   :footer (slurp (io/resource "footer.html"))
   :header (slurp (io/resource "header.html"))
   :ga (slurp (io/resource "public/scripts/ga.js"))})
