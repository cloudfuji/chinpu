(ns chinpu.core
  (:require [clojure.string :as string]
            [clj-http.client :as client]
            [cheshire.core :as json]))

(def ^:dynamic debug false)

(defn ->mailchimp-name
  "Converts from a clojure method name to a Mailchimp method name (CapitalizedCamelCase)"
 [clojure-method-name]
  (-> (string/replace clojure-method-name #"(?:^|-)(\p{Alpha})" (fn [[_ char]] (string/upper-case char)))
      (string/replace-first #".{1}" string/lower-case)))

(defn ->clojure-name
  "Converts from a MailchimpStyleMethodName to a clojure-style-method-name"
 [mailchimp-method-name]
  (string/replace mailchimp-method-name #"[a-z][A-Z]" (fn [[a b]] (str a "-" (string/lower-case b)))))

(def mailchimp-methods
  "List of all Mailchimp methods to generate"
  [; Campaign Methods
   "campaignUnschedule" "campaignSchedule" "campaignScheduleBatch"
   "campaignResume" "campaignPause" "campaignSendNow" "campaignSendTest"
   "campaignSegmentTest" "campaignCreate" "campaignUpdate" "campaignReplicate"
   "campaignDelete" "campaigns" "campaignStats" "campaignClickStats"
   "campaignEmailDomainPerformance" "campaignMembers" "campaignHardBounces"
   "campaignSoftBounces" "campaignUnsubscribes" "campaignAbuseReports"
   "campaignAdvice" "campaignAnalytics" "campaignGeoOpens"
   "campaignGeoOpensForCountry" "campaignEepUrlStats" "campaignBounceMessage"
   "campaignBounceMessages" "campaignEcommOrders" "campaignShareReport"
   "campaignContent" "campaignTemplateContent" "campaignOpenedAIM"
   "campaignNotOpenedAIM" "campaignClickDetailAIM" "campaignEmailStatsAIM"
   "campaignEmailStatsAIMAll" "campaignEcommOrderAdd"

                                        ; List methods
   "lists" "listMergeVars" "listMergeVarAdd" "listMergeVarUpdate"
   "listMergeVarDel" "listMergeVarReset" "listInterestGroupings"
   "listInterestGroupAdd" "listInterestGroupDel" "listInterestGroupUpdate"
   "listInterestGroupingAdd" "listInterestGroupingUpdate"
   "listInterestGroupingDel" "listWebhooks" "listWebhookAdd" "listWebhookDel"
   "listStaticSegments" "listStaticSegmentAdd" "listStaticSegmentReset"
   "listStaticSegmentDel" "listStaticSegmentMembersAdd"
   "listStaticSegmentMembersDel" "listSubscribe" "listUnsubscribe"
   "listUpdateMember" "listBatchSubscribe" "listBatchUnsubscribe" "listMembers"
   "listMemberInfo" "listMemberActivity" "listAbuseReports" "listGrowthHistory"
   "listActivity" "listLocations" "listClients"

                                        ; Template methods
   "templates" "templateInfo" "templateAdd" "templateUpdate" "templateDel"
   "templateUndel"

                                        ; Helper methods
   "getAccountDetails" "getVerifiedDomains" "generateText" "inlineCss"
   "listsForEmail" "campaignsForEmail" "chimpChatter" "searchMembers"
   "searchCampaigns" "apikeys" "apikeyAdd" "apikeyExpire" "login" "ping"
   "ecommOrders" "ecommOrderAdd" "ecommOrderDel"

                                        ; Folder methods
   "folders" "folderAdd" "folderUpdate" "folderDel"

                                        ; No idea
   "deviceRegister" "deviceUnregister"

                                        ; Golden Monkey methods
   "gmonkeyAdd" "gmonkeyDel" "gmonkeyMembers" "gmonkeyActivity"])

(defn base-url
  "Returns the base API url for methods to work with. Optionally force non-https connection."
  [region & [http?]]
  (string/join region [(if http? "http://" "https://") ".api.mailchimp.com/1.3"]))

(defn request
  "Generic request method used for all higher-level mailchimp calls."
 [method region api-key params & [opts]]
 (let [result (client/get (base-url region) (merge {:query-params (merge params {:method method
                                                                                 :apikey api-key})
                                                    :debug debug} opts))]
    (json/parse-string (:body result))))

(defn- make-mailchimp-command [method-name]
  `(defn ~(symbol (->clojure-name method-name)) [~'region ~'api-key ~'params & [~'opts]]
     (apply request [~(->mailchimp-name method-name) ~'region ~'api-key ~'params ~'opts])))

(defmacro make-mailchimp-commands [names]
  `(do ~@(map make-mailchimp-command names)))

; The duplication here is unfortunate.
(make-mailchimp-commands ["campaignUnschedule" "campaignSchedule" "campaignScheduleBatch"
                          "campaignResume" "campaignPause" "campaignSendNow" "campaignSendTest"
                          "campaignSegmentTest" "campaignCreate" "campaignUpdate" "campaignReplicate"
                          "campaignDelete" "campaigns" "campaignStats" "campaignClickStats"
                          "campaignEmailDomainPerformance" "campaignMembers" "campaignHardBounces"
                          "campaignSoftBounces" "campaignUnsubscribes" "campaignAbuseReports"
                          "campaignAdvice" "campaignAnalytics" "campaignGeoOpens"
                          "campaignGeoOpensForCountry" "campaignEepUrlStats" "campaignBounceMessage"
                          "campaignBounceMessages" "campaignEcommOrders" "campaignShareReport"
                          "campaignContent" "campaignTemplateContent" "campaignOpenedAIM"
                          "campaignNotOpenedAIM" "campaignClickDetailAIM" "campaignEmailStatsAIM"
                          "campaignEmailStatsAIMAll" "campaignEcommOrderAdd"

                          "lists" "listMergeVars" "listMergeVarAdd" "listMergeVarUpdate"
                          "listMergeVarDel" "listMergeVarReset" "listInterestGroupings"
                          "listInterestGroupAdd" "listInterestGroupDel" "listInterestGroupUpdate"
                          "listInterestGroupingAdd" "listInterestGroupingUpdate"
                          "listInterestGroupingDel" "listWebhooks" "listWebhookAdd" "listWebhookDel"
                          "listStaticSegments" "listStaticSegmentAdd" "listStaticSegmentReset"
                          "listStaticSegmentDel" "listStaticSegmentMembersAdd"
                          "listStaticSegmentMembersDel" "listSubscribe" "listUnsubscribe"
                          "listUpdateMember" "listBatchSubscribe" "listBatchUnsubscribe" "listMembers"
                          "listMemberInfo" "listMemberActivity" "listAbuseReports" "listGrowthHistory"
                          "listActivity" "listLocations" "listClients"

                          "templates" "templateInfo" "templateAdd" "templateUpdate" "templateDel"
                          "templateUndel"

                          "getAccountDetails" "getVerifiedDomains" "generateText" "inlineCss"
                          "listsForEmail" "campaignsForEmail" "chimpChatter" "searchMembers"
                          "searchCampaigns" "apikeys" "apikeyAdd" "apikeyExpire" "login" "ping"
                          "ecommOrders" "ecommOrderAdd" "ecommOrderDel"

                          "folders" "folderAdd" "folderUpdate" "folderDel"

                          "deviceRegister" "deviceUnregister"

                          "gmonkeyAdd" "gmonkeyDel" "gmonkeyMembers" "gmonkeyActivity"])

(def metadata-url "https://login.mailchimp.com/oauth2/metadata")

(defn metadata
  "Retrieves metadata for an account given an access token (used for retrieving which region an api-key belongs to)"
 [oauth-access-token]
  (-> (client/get metadata-url {:headers {"Authorization" (str "OAuth " oauth-access-token)}})
      :body
      json/parse-string))
