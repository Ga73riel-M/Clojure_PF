
(ns todo.backend.core
  (:require
   [ring.adapter.jetty :as jetty]
   [reitit.ring :as ring]
   [todo.backend.handler :as handler])
  (:gen-class))

;; 1. Definição das rotas
(def app-routes
  (ring/router
   [["/api/hello"
     {:get {:handler handler/hello-handler}}]]))

;; 2. Definição da aplicação
(def app
  (ring/ring-handler
   app-routes
   (ring/create-default-handler)))

;; 3. Inicialização do servidor
(defn start-server
  [port]
  (println (str "Servidor iniciado na porta " port))
  (jetty/run-jetty
   #'app
   {:port port
    :join? false}))

;; 4. Ponto de entrada
(defn -main
  [& args]
  (let [port (Integer/parseInt
              (or (first args) "3000"))]
    (start-server port)))
