
(ns todo.backend.store)

(defonce todos (atom []))
(defonce next-id (atom 0))

(defn get-all []
  @todos)

(defn create! [title]
  (let [todo {:id (swap! next-id inc)
              :title title
              :completed false}]
    (swap! todos conj todo)
    todo))
