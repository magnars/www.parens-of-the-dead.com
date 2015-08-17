(ns parens.episodes)

(defn episode-url [episode]
  (str "/" (-> episode :prefixes :url) (:number episode) ".html"))

(defn episode-name [episode]
  (str "Episode " (:number episode) ": " (:name episode)))

(defn video-url [episode]
  (str "http://www.youtube.com/embed/" (:youtube episode) "?hd=1"))

(defn embed-video [episode]
  [:iframe {:src (video-url episode)
            :frameborder 0
            :allowfullscreen true}])
