(ns parens.episodes)

(defn get-url [episode]
  (str "/" (-> episode :prefixes :url) (:number episode) ".html"))

(defn get-name [episode]
  (or (:title episode)
      (str "Episode " (:number episode) ": " (:name episode))))

(defn get-code-url [season episode]
  (str "https://github.com/magnars/" (-> season :prefixes :github) "/tree/episode-" (:number episode)))

(defn video-url [episode]
  (str "https://www.youtube.com/embed/" (:youtube episode) "?hd=1"))

(defn embed-video [episode]
  [:iframe {:src (video-url episode)
            :frameborder 0
            :allowfullscreen true}])
