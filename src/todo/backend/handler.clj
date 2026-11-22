
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
