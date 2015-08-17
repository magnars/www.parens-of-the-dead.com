(ns parens.episodes)

(defn get-url [episode]
  (str "/" (-> episode :prefixes :url) (:number episode) ".html"))

(defn get-name [episode]
  (str "Episode " (:number episode) ": " (:name episode)))

(defn get-code-url [episode]
  (str "https://github.com/magnars/parens-of-the-dead/tree/episode-" (:number episode)))

(defn video-url [episode]
  (str "http://www.youtube.com/embed/" (:youtube episode) "?hd=1"))

(defn embed-video [episode]
  [:iframe {:src (video-url episode)
            :frameborder 0
            :allowfullscreen true}])
