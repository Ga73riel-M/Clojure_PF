
(ns todo.frontend.core
  (:require [reagent.core :as r]
            [reagent.dom.client :as rdom]
            [clojure.string :as str]))

(defonce app-state
  (r/atom {:todos []
           :new-title ""}))

;; Atualiza o texto digitado
(defn update-new-title [event]
  (swap! app-state assoc
         :new-title (.. event -target -value)))

;; Adiciona uma tarefa
(defn add-todo []
  (let [title (str/trim (:new-title @app-state))]
    (when-not (str/blank? title)
      (swap! app-state
             (fn [state]
               (-> state
                   (update :todos conj
                           {:id (random-uuid)
                            :title title
                            :completed false})
                   (assoc :new-title "")))))))

;; Alterna entre concluída e pendente
(defn toggle-todo [id]
  (swap! app-state update :todos
         (fn [todos]
           (mapv (fn [todo]
                   (if (= (:id todo) id)
                     (update todo :completed not)
                     todo))
                 todos))))

;; Exclui uma tarefa
(defn delete-todo [id]
  (swap! app-state update :todos
         (fn [todos]
           (filterv #(not= (:id %) id) todos))))

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
     (if (empty? todos)
       [:p "Nenhuma tarefa cadastrada."]
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
     [:p "Organize suas tarefas!"]

     [todo-form]

     [:div.todo-counter
      [:span "Total: " (count todos)]
      [:span "Pendentes: " pending]
      [:span "Concluídas: " completed]]

     [todo-list]]))

;; Inicialização
(defn ^:export init []
  (println "Frontend Todo iniciado...")
  (let [root (rdom/create-root
              (js/document.getElementById "app"))]
    (.render root (r/as-element [app]))))
