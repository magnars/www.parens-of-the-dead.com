(ns parens.pages
  (:require [clojure.string :as str]
            [parens.episodes :as ep]))

(defn no-widows
  "Avoid typographic widows by adding a non-breaking space between the last two words."
  [s]
  (str/replace s #" ([^ ]{1,6})$" "&nbsp;$1"))

(defn- render-episode [episode]
  [:a {:class (str "box episode-box " (:color episode))
       :href (ep/get-url episode)}
   [:h2 (no-widows (ep/get-name episode))]
   (:description episode)])

(defn index [content]
  {:body
   (list
    [:div.illustration]
    [:div.content
     (map render-episode (:episodes (first (remove :old? (:seasons content)))))
     [:p.next
      "Next episode? Follow "
      [:a {:href "https://twitter.com/parensofthedead"}
       "@parensofthedead"]]
     [:p.nnext
      "Looking for the old episodes? "
      [:a {:href "/s1"}
       "Season one"]]])
   :color "red"})

(defn- insert-disqus-thread [html episode]
  (-> html
      (str/replace #":episode-identifier" (str "episode_" (-> episode :prefixes :disqus) (:number episode)))
      (str/replace #":episode-link" (ep/get-url episode))))

(defn- episode-page [season episode next-episode content]
  {:body
   (list
    [:div.episode
     [:div.content
      [:div.video-embed (ep/embed-video episode)]
      [:div.box
       [:h2 (no-widows (or (:title episode)
                           (:name episode)))]
       (:description episode)]
      [:div.box
       [:h3 "Done watching?"]
       [:ul
        (if next-episode
          [:li "Check out " [:a {:href (ep/get-url next-episode)} (ep/get-name next-episode)] "."]
          [:li "Follow " [:a {:href "https://twitter.com/parensofthedead"}
                          "@parensofthedead"] " to be notified when the next episode is ready."])
        [:li "Peruse the " [:a {:href (ep/get-code-url season episode)} "code on GitHub"] "."]
        [:li "Take a look at the " [:a {:href "/"} "episode overview"] "."]
        (if (:enable-disqus? season)
          [:li "Leave your comments or questions below. Underground."]
          [:li "Questions? Ask away in either the "
           [:a {:href (str "https://www.youtube.com/watch?v=" (:youtube episode))}
            "YouTube comments"]
           " or " [:a {:href "https://twitter.com/parensofthedead"}
                   "@parensofthedead"]
           "."])]]]]
    (when (:enable-disqus? season)
      [:div.comments.content
       [:div.box
        (insert-disqus-thread (:disqus-html content) episode)]]))
   :color (:color episode)})

(defn create-season-episode-pages [content season]
  (->> (:episodes season)
       (partition-all 2 1)
       (map (fn [[episode next-episode]]
              [(ep/get-url episode)
               (episode-page season episode next-episode content)]))))

(defn create-episode-pages [content]
  (into {} (mapcat #(create-season-episode-pages content %) (:seasons content))))

(defn create-season-pages [content]
  (->> (for [season (:seasons content)]
         [(:season-url season)
          {:body
           (list
            [:div.illustration]
            [:div.content
             (map render-episode (:episodes season))
             [:p.next
              "Next episode? Follow "
              [:a {:href "https://twitter.com/parensofthedead"}
               "@parensofthedead"]]])
           :color "red"}])
       (into {})))

(defn get-pages [content]
  (merge
   {"/" (index content)}
   (create-episode-pages content)
   (create-season-pages content)))
