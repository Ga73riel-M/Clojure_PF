
(ns todo.backend.handler
  (:require [todo.backend.store :as store]
            [clojure.string :as str]))

(defn hello-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/plain; charset=utf-8"}
   :body "Hello, World!"})

(defn list-todos-handler
  [_request]
  {:status 200
   :body {:todos (store/get-all)}})

(defn create-todo-handler
  [request]
  (let [title (get-in request [:body :title])]
    (if (and (string? title)
             (not (str/blank? title)))
      {:status 201
       :body (store/create! title)}
      {:status 400
       :body {:error "O título da tarefa é obrigatório."}})))

(defn- parse-id [request]
  (try
    (let [id (Long/parseLong
              (get-in request [:path-params :id]))]
      (when (pos? id) id))
    (catch Exception _
      nil)))

(defn toggle-todo-handler
  [request]
  (if-let [id (parse-id request)]
    (if-let [todo (store/toggle! id)]
      {:status 200
       :body todo}
      {:status 404
       :body {:error "Tarefa não encontrada."}})
    {:status 400
     :body {:error "ID inválido."}}))

(defn delete-todo-handler
  [request]
  (if-let [id (parse-id request)]
    (if (store/delete! id)
      {:status 200
       :body {:deleted true
              :id id}}
      {:status 404
       :body {:error "Tarefa não encontrada."}})
    {:status 400
     :body {:error "ID inválido."}}))
