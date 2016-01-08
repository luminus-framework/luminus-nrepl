(ns luminus.repl-server
  (:require [clojure.tools.nrepl.server :as nrepl]
            [clojure.tools.logging :as log]
            [mount.core :refer [defstate]]            
            [environ.core :refer [env]]))

(defonce nrepl-server (atom nil))

(defn stop []
  (when-let [server @nrepl-server]
    (nrepl/stop-server server)
    (reset! nrepl-server nil)
    (log/info "nREPL server stopped")))

(defn start
  "Start a network repl for debugging when the :nrepl-port is set in the environment."
  [port]
  (if @nrepl-server
    (log/error "nREPL is already running!")
    (try
        (reset! nrepl-server (nrepl/start-server :port port))
        (log/info "nREPL server started on port" port)
        (catch Throwable t
          (log/error t "failed to start nREPL")))))

