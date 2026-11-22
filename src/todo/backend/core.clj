
(ns todo.backend.core
  (:require
   [ring.adapter.jetty :as jetty]
   [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
   [reitit.ring :as ring]
   [todo.backend.handler :as handler])
  (:gen-class))

(def app-routes
  (ring/router
   [["/api/hello"
     {:get {:handler handler/hello-handler}}]

    ["/api/todos"
     {:get {:handler handler/list-todos-handler}
      :post {:handler handler/create-todo-handler}}]]))

(def app
  (-> (ring/ring-handler
       app-routes
       (ring/create-default-handler))
      (wrap-json-body {:keywords? true})
      (wrap-json-response)))

(defn start-server [port]
  (println (str "Servidor iniciado na porta " port))
  (jetty/run-jetty
   #'app
   {:port port :join? false}))

(defn -main [& args]
  (let [port (Integer/parseInt (or (first args) "3000"))]
    (start-server port)))
