(ns chinpu.export
  (:require [chinpu.core :as mc]
            [clojure.string :as string]
            [clj-http.client :as client]
            [cheshire.core :as json])
  (:import (java.io StringReader BufferedReader)))

(defn base-url [region method & [http?]]
  (str (string/join region [(if http? "http://" "https://") ".api.mailchimp.com/export/1.0"]) "/" method))

(defn request [method region api-key params & [opts]]
  (let [result (client/get (base-url region method) (merge {:query-params (merge params {:apikey api-key})}
                                                    opts))]
    (json/parsed-seq (BufferedReader. (StringReader. (:body result))))))

(defn list
  "Exports all lists and subscribers. WARNING: Potentially very high memory usage."
 [region api-key id & [status segment since opts]]
  (let [[keys & values] (request "list" region api-key (merge {:id id}
                                                              (when status  {:status  status})
                                                              (when segment {:segment segment})
                                                              (when since   {:since   since})) opts)]
    (map (partial zipmap keys) values)))

(defn capaign-subscriber-activity [region api-key id include-empty? & [opts]]
  "Exports all lists and subscribers. WARNING: Potentially VERY high memory usage."
  (request "campaignSubscriberActivity" region api-key {:id            id
                                                        :include_empty include-empty?} opts))
