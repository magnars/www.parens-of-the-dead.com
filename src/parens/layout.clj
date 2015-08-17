(ns parens.layout
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]))

(defn- serve-to-media-query-capable-browsers [tag]
  (list "<!--[if (gt IE 8) | (IEMobile)]><!-->" tag "<!--<![endif]-->"))

(defn- serve-to-media-query-clueless-browsers [tag]
  (list "<!--[if (lte IE 8) & (!IEMobile)]>" tag "<![endif]-->"))

(defn render-page [page content request]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    (serve-to-media-query-capable-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/responsive.css")}])
    (serve-to-media-query-clueless-browsers
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/unresponsive.css")}])
    [:link {:href "/atom.xml" :rel "alternate" :title "Parens of the Dead" :type "application/atom+xml"}]
    [:title (if-let [title (:title page)]
              (str title " | " "Parens of the Dead")
              "Parens of the Dead")]]
   [:body {:class (:color page)}
    [:script (:ga content)]
    (:header content)
    (:body page)
    (:footer content)]))
