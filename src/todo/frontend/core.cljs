
(ns todo.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]))

(def api-url "http://localhost:3000/api/todos")

(defonce app-state
  (r/atom {:todos []
           :new-title ""
           :loading? true
           :error nil}))

;; Converte respostas HTTP em dados Clojure
(defn response->data [response]
  (if (.-ok response)
    (.json response)
    (throw (js/Error. "Erro ao comunicar com a API."))))

;; Carrega as tarefas do backend
(defn load-todos []
  (-> (js/fetch api-url)
      (.then response->data)
      (.then
       (fn [data]
         (let [result (js->clj data :keywordize-keys true)]
           (swap! app-state assoc
                  :todos (:todos result)
                  :loading? false
                  :error nil))))
      (.catch
       (fn [error]
         (swap! app-state assoc
                :loading? false
                :error (.-message error))))))

;; Atualiza o campo de texto
(defn update-new-title [event]
  (swap! app-state assoc
         :new-title (.. event -target -value)))

;; Cadastra uma tarefa na API
(defn add-todo []
  (let [title (str/trim (:new-title @app-state))]
    (when-not (str/blank? title)
      (-> (js/fetch
           api-url
           #js {:method "POST"
                :headers #js {"Content-Type" "application/json"}
                :body (js/JSON.stringify
                       (clj->js {:title title}))})
          (.then response->data)
          (.then
           (fn [_data]
             (swap! app-state assoc
                    :new-title ""
                    :error nil)
             (load-todos)))
          (.catch
           (fn [error]
             (swap! app-state assoc
                    :error (.-message error))))))))

;; Alterna o estado local da tarefa.
;; A persistência desse recurso será implementada na etapa CRUD.
(defn toggle-todo [id]
  (-> (js/fetch
       (str api-url "/" id "/toggle")
       #js {:method "PATCH"})
      (.then response->data)
      (.then (fn [_data]
               (load-todos)))
      (.catch
       (fn [error]
         (swap! app-state assoc
                :error (.-message error))))))

;; Exclui localmente.
;; A exclusão persistente será implementada na etapa CRUD.

(defn delete-todo [id]
  (-> (js/fetch
       (str api-url "/" id)
       #js {:method "DELETE"})
      (.then response->data)
      (.then (fn [_data]
               (load-todos)))
      (.catch
       (fn [error]
         (swap! app-state assoc
                :error (.-message error))))))


;; Formulário
(defn todo-form []
  [:div.todo-input
   [:input
    {:type "text"
     :placeholder "O que precisa ser feito?"
     :value (:new-title @app-state)
     :on-change update-new-title
     :on-key-down
     (fn [event]
       (when (= "Enter" (.-key event))
         (add-todo)))}]

   [:button
    {:on-click add-todo}
    "Adicionar"]])

;; Lista de tarefas
(defn todo-list []
  (let [todos (:todos @app-state)]
    [:div.todo-list
     (cond
       (:loading? @app-state)
       [:p "Carregando tarefas..."]

       (empty? todos)
       [:p "Nenhuma tarefa cadastrada."]

       :else
       [:ul
        (for [todo todos]
          ^{:key (:id todo)}
          [:li.todo-item
           {:class (when (:completed todo) "completed")}

           [:label
            [:input
             {:type "checkbox"
              :checked (:completed todo)
              :on-change #(toggle-todo (:id todo))}]
            [:span (:title todo)]]

           [:button.delete-button
            {:on-click #(delete-todo (:id todo))}
            "Excluir"]])])]))

;; Aplicação principal
(defn app []
  (let [todos (:todos @app-state)
        completed (count (filter :completed todos))
        pending (- (count todos) completed)]
    [:div.todo-app
     [:h1 "Minha Todo List"]
     [:p "Tarefas conectadas ao backend"]

     [todo-form]

     (when-let [error (:error @app-state)]
       [:p {:style {:color "red"}} error])

     [:div.todo-counter
      [:span "Total: " (count todos)]
      [:span "Pendentes: " pending]
      [:span "Concluídas: " completed]]

     [todo-list]]))

;; Inicialização
(defn ^:export init []
  (println "Frontend conectado à API...")
  (let [root (rdom/create-root
              (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))
    (load-todos)))
