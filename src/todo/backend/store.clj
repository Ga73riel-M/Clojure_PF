
(ns todo.backend.store
  (:import [java.sql DriverManager]))

;; Arquivo do banco na raiz do projeto
(def db-url "jdbc:sqlite:todo.db")

;; Abre uma conexão com SQLite
(defn- get-connection []
  (DriverManager/getConnection db-url))

;; Cria a tabela caso ainda não exista
(defn init! []
  (with-open [connection (get-connection)
              statement (.createStatement connection)]
    (.execute
     statement
     "CREATE TABLE IF NOT EXISTS todos (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        title TEXT NOT NULL,
        completed INTEGER NOT NULL DEFAULT 0
      )"))
  (println "Banco SQLite inicializado!"))

;; Retorna todas as tarefas
(defn get-all []
  (with-open [connection (get-connection)
              statement (.createStatement connection)
              result-set (.executeQuery
                          statement
                          "SELECT id, title, completed
                           FROM todos
                           ORDER BY id ASC")]
    (loop [result []]
      (if (.next result-set)
        (recur
         (conj result
               {:id (.getLong result-set "id")
                :title (.getString result-set "title")
                :completed (= 1 (.getInt result-set "completed"))}))
        result))))

;; Cadastra uma nova tarefa
(defn create! [title]
  (with-open [connection (get-connection)]
    (with-open [statement
                (.prepareStatement
                 connection
                 "INSERT INTO todos (title, completed)
                  VALUES (?, 0)")]

      (.setString statement 1 title)
      (.executeUpdate statement))

    (with-open [statement (.createStatement connection)
                result-set (.executeQuery
                            statement
                            "SELECT last_insert_rowid() AS id")]
      (.next result-set)
      {:id (.getLong result-set "id")
       :title title
       :completed false})))
