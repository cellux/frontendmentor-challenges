(ns io.github.cellux.frontendmentor-challenges.qr-code-component-main.core
  (:import [java.io File])
  (:require
   [clojure.java.io :as jio]
   [clojure.string :as str]
   [hiccup.page :refer [html5]]
   [garden.core :refer [css style]]
   [garden.color :refer [hsl]]
   [garden.selectors :as s]))

(defn stylesheet
  []
  (css [:body {:background-color (hsl 212 45 89)
               :box-sizing :border-box
               :font-family "\"Outfit\", sans-serif"
               :font-size "min(15.5px, 2vh)"}]
       [:#container {:display :flex
                     :flex-direction :column
                     :justify-content :center
                     :align-items :center
                     :height "100dvh"}]
       [:#card {:background-color (hsl 0 0 100)
                :max-width "18.5em"
                :padding "16px"
                :margin "16px 32px"
                :text-align "center"
                :border-radius "20px"
                :box-shadow "0 30px 30px #4441"}]
       [(s/> :#card :img) {:width "100%"
                           :border-radius "10px"}]
       [(s/> :#card :div) {:width "90%"
                           :margin "1.2lh auto"}]
       [(s/descendant :#card :h1) {:font-weight 700
                                   :font-size "140%"
                                   :letter-spacing "0.1px"
                                   :color (hsl 218 44 22)
                                   :filter "blur(0.4px)"
                                   :line-height "1.3em"}]
       [(s/descendant :#card :p) {:color (hsl 0 0 55)
                                  :line-height "1.2em"}]))

(defn page
  []
  (html5 {:lang "en"}
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:title "Frontend Mentor | QR code component"]
     [:link {:rel "icon" :type "image/png" :href "favicon-32x32.png" :sizes "32x32"}]
     [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
     [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
     [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css2?family=Outfit:wght@400;700&display=swap" :crossorigin true}]
     [:link {:rel "stylesheet" :href "../normalize.css"}]
     [:style (stylesheet)]]
    [:body
     [:div#container
      [:div#design
       {:style (style {:position :absolute
                       :z-index 1
                       :visibility :hidden})}
       #_[:img {:src "desktop-design.jpg" :alt "Original design JPG"}]
       #_[:img {:src "mobile-design.jpg" :alt "Original design JPG"}]]
      [:main#card
       [:img {:src "image-qr-code.png" :alt "QR code"}]
       [:div
        [:h1 "Improve your front-end skills by building projects"]
        [:p "Scan the QR code to visit Frontend Mentor and take your coding skills to the next level"]]]]]))

(def manifest
  [[:copy page "index.html"]
   [:copy "images/favicon-32x32.png"]
   [:copy "images/image-qr-code.png"]
   [:copy "design/desktop-design.jpg"]
   [:copy "design/mobile-design.jpg"]])
