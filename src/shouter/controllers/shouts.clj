(ns shouter.controllers.shouts
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [shouter.views.shouts :as view]
            [shouter.models.shout :as model]))

(defn index []
  (view/index (model/all)))

(defn create
  [shout]
  (when-not (or (str/blank? shout)
                (> (count shout) 512))
    (model/create shout))
  (ring/redirect "/"))

(defroutes routes
  (GET  "/" [] (index))
  (POST "/" [shout] (create shout)))

;; define page component
;; the use of a symbol emphasizes where the input is expected,
;; provides completion, and eliminates the need to also specify the
;; name of the input field.
;; (defpcomp shout-form [shout]
;;   (form 
;;    (view (label "What do you want to SHOUT?"
;;                 (text-area shout))
;;          (submit "SHOUT!"))
;;    (action [POST "/"]
;;            (when-not (or (str/blank? shout)
;;                          (> (count shout) 512))
;;              (model/create shout))
;;            (ring/redirect "/"))))

;; (defpcomp shouts-display []
;;   (view (div {:class "shouts sixteen columns alpha omega"}
;;              (map
;;               (fn [shout] [:h2 {:class "shout"} (h (:body shout))])
;;               (model/all)))))

