
(ns todo.backend.store
  (:import [java.sql DriverManager]))

(def db-url "jdbc:sqlite:todo.db")

(defn- get-connection []
  (DriverManager/getConnection db-url))

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

(defn- result-set->todo [result-set]
  {:id (.getLong result-set "id")
   :title (.getString result-set "title")
   :completed (= 1 (.getInt result-set "completed"))})

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
        (recur (conj result (result-set->todo result-set)))
        result))))

(defn get-by-id [id]
  (with-open [connection (get-connection)
              statement (.prepareStatement
                         connection
                         "SELECT id, title, completed
                          FROM todos WHERE id = ?")]
    (.setLong statement 1 id)
    (with-open [result-set (.executeQuery statement)]
      (when (.next result-set)
        (result-set->todo result-set)))))

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

(defn toggle! [id]
  (with-open [connection (get-connection)
              statement (.prepareStatement
                         connection
                         "UPDATE todos
                          SET completed = CASE
                            WHEN completed = 0 THEN 1
                            ELSE 0
                          END
                          WHERE id = ?")]
    (.setLong statement 1 id)
    (.executeUpdate statement))
  (get-by-id id))

(defn delete! [id]
  (with-open [connection (get-connection)
              statement (.prepareStatement
                         connection
                         "DELETE FROM todos WHERE id = ?")]
    (.setLong statement 1 id)
    (pos? (.executeUpdate statement))))
