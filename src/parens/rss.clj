(ns parens.rss
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup.core :refer [html]]
            [optimus.assets.creation :refer [last-modified]]
            [parens.episodes :refer [get-url video-url]])
  (:import java.text.SimpleDateFormat
           java.util.Date))

(defn to-id-str
  "Replaces all special characters with dashes, avoiding leading,
   trailing and double dashes."
  [str]
  (-> (.toLowerCase str)
      (str/replace #"[^a-zA-Z0-9]+" "-")
      (str/replace #"-$" "")
      (str/replace #"^-" "")))

(defn- entry [episode]
  [:entry
   [:title (:name episode)]
   [:updated (str (:date episode) "T00:00:00+02:00")]
   [:author [:name "Magnar Sveen"]]
   [:link {:href (get-url episode)}]
   [:id (str "urn:parens:feed:episode:" (to-id-str (:name episode)))]
   [:media:content {:url (video-url episode)
                    :type "application/x-shockwave-flash"
                    :isDefault "true"
                    :expression "full"
                    :medium "video"
                    :lang "en"}]
   [:content {:type "html"} (html [:a {:href (video-url episode)}
                                   "See the screencast"])]])

(defn atom-xml [seasons]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"
            :xmlns:media "http://search.yahoo.com/mrss/"}
     [:id "urn:parens:feed"]
     [:updated
      (.format (SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ssXXX")
               (Date. (last-modified (io/resource "episodes.edn"))))]
     [:title {:type "text"} "Parens of the Dead"]
     [:link {:rel "self" :href "http://www.parens-of-the-dead.com/atom.xml"}]
     (->> seasons
          (mapcat :episodes)
          reverse
          (map entry))])))
