# mailchimp

A Clojure library for interacting with Mailchimp's API. In search of a better name.

## Installation

Available from clojar, by adding the following to your `project.clj`:

    [org.clojars.zenbox/mailchimp "0.1.1"]

## Usage

Usage params are explicit - you'll need to pass in your API key, region, and method params for each call.

    (use 'mailchimp.core)
    (ping "us1" "api-key" {})
    (lists-for-email "us1" "api-key" {:email_address "example@gmail.com"})
    (list-unsubscribe "us1" "api-key" {:email_address "example@gmail.com"})

The only command that doesn't require a region or params is the `metadata` command. Use it simply by passing in the API key:

    (metada "api-key")
 
 Will return something similar to:
 
    {"dc" "us5", "login_url" "https://login.mailchimp.com", "api_endpoint" "https://us5.api.mailchimp.com"}
    
Any command listed on http://apidocs.mailchimp.com/api/1.3/ should work in this library

## TODO

Integrate the Mailchimp Documentation into the repl for easier usage. For example, each command should have its description alongside the params specific to it, and expected return type.

Possibly a few higher-level wrappers, for example `(with-mailchimp-api-key "api-key" (sequence of mailchimp commands))`, also for the region, email_address, etc.

## License

Copyright © 2013 Bushido Inc ([Zenbox](https://www.zenboxapp.com))

Distributed under the Eclipse Public License, the same as Clojure.
