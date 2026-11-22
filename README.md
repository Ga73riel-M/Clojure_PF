# Todo List — Aplicação Full Stack em Clojure

Projeto acadêmico de desenvolvimento de uma aplicação **Todo List (lista de tarefas)** utilizando Clojure no backend e ClojureScript no frontend. A aplicação permite cadastrar, visualizar, concluir e excluir tarefas, mantendo os dados salvos em um banco de dados SQLite.

## Integrantes

- **ANTONIO NETO DE MOURA MELO**
- **GABRIEL MESQUITA TORRES**

## Sobre o projeto

O objetivo deste projeto é desenvolver uma aplicação web full stack utilizando o ecossistema Clojure, integrando uma API REST ao frontend para gerenciar tarefas.

A aplicação foi desenvolvida incrementalmente, começando pela configuração do ambiente e implementação do servidor, passando pela criação da API e interface, até a integração com banco de dados persistente.

## Funcionalidades

- **Cadastrar tarefas:** permite adicionar novas tarefas à lista.
- **Listar tarefas:** exibe todas as tarefas cadastradas.
- **Concluir tarefas:** permite marcar e desmarcar tarefas como concluídas.
- **Excluir tarefas:** permite remover tarefas existentes.
- **Persistência de dados:** mantém as tarefas armazenadas no banco SQLite, mesmo após reiniciar o servidor.
- **Contadores:** exibe a quantidade total de tarefas, tarefas pendentes e concluídas.

## Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Clojure | Desenvolvimento do backend |
| ClojureScript | Desenvolvimento do frontend |
| Reagent | Construção da interface reativa |
| React | Renderização da interface |
| Ring | Infraestrutura HTTP do backend |
| Jetty | Servidor web |
| Reitit | Roteamento da API REST |
| SQLite | Persistência dos dados |
| JDBC | Comunicação com o banco de dados |
| Shadow CLJS | Compilação e execução do frontend |
| Git e GitHub | Versionamento e hospedagem do projeto |

## Estrutura do projeto

```text
Clojure/
│
├── src/
│   └── todo/
│       ├── backend/
│       │   ├── core.clj
│       │   ├── handler.clj
│       │   └── store.clj
│       │
│       └── frontend/
│           └── core.cljs
│
├── resources/
│   └── public/
│       ├── index.html
│       ├── css/
│       │   └── style.css
│       └── js/
│
├── deps.edn
├── shadow-cljs.edn
├── package.json
├── package-lock.json
├── .gitignore
└── README.md
```

**Observação:** o arquivo `todo.db`, responsável pelo armazenamento local das tarefas, é gerado durante a execução da aplicação e não precisa ser versionado no Git.

## Requisitos para execução

Antes de executar o projeto, é necessário instalar:

- Java (JDK).
- Clojure CLI.
- Node.js e npm.
- Git (opcional para executar, necessário para versionamento).

## Como executar

### 1. Clonar o repositório

```bash
git clone https://github.com/Ga73riel-M/Clojure_PF.git
cd Clojure_PF
```

### 2. Instalar as dependências do frontend

Na pasta raiz do projeto, execute:

```bash
npm install
```

### 3. Iniciar o backend

Em um terminal, execute:

```bash
clojure -M:run
```

O servidor será iniciado na porta 3000.

A API ficará disponível em:

```text
http://localhost:3000
```

### 4. Iniciar o frontend

Em outro terminal, execute:

```bash
npx shadow-cljs watch app
```

A interface ficará disponível em:

```text
http://localhost:8000
```

Acesse o endereço no navegador para utilizar a aplicação.

## API REST

A aplicação disponibiliza os seguintes endpoints:

| Método HTTP | Endpoint | Descrição |
|---|---|---|
| GET | `/api/todos` | Lista todas as tarefas |
| POST | `/api/todos` | Cadastra uma nova tarefa |
| PATCH | `/api/todos/:id/toggle` | Alterna o estado de conclusão |
| DELETE | `/api/todos/:id` | Exclui uma tarefa |

### Exemplo de cadastro

Requisição:

```http
POST /api/todos
Content-Type: application/json
```

Corpo:

```json
{
  "title": "Estudar Clojure"
}
```

### Exemplo de resposta

```json
{
  "id": 1,
  "title": "Estudar Clojure",
  "completed": false
}
```

## Banco de dados

O projeto utiliza SQLite para armazenar as tarefas.

A tabela `todos` possui os seguintes campos:

| Campo | Tipo | Descrição |
|---|---|---|
| id | INTEGER | Identificador único da tarefa |
| title | TEXT | Descrição da tarefa |
| completed | INTEGER | Estado de conclusão da tarefa |

O banco é inicializado automaticamente pelo backend quando o servidor é iniciado.

## Referências

- **Tutorial utilizado:** [LambdaGeo — Tutorial de Clojure](https://lambdageo-edu.github.io/lambdageo-tutorials/clojure/)
- **Repositório GitHub:** https://github.com/Ga73riel-M/Clojure_PF
- **Vídeo demonstrativo:** [INSERIR LINK DO VÍDEO]

## Considerações finais

O desenvolvimento deste projeto proporcionou a aplicação prática dos conceitos da linguagem Clojure e de seu ecossistema, envolvendo programação funcional, desenvolvimento de APIs REST, integração entre frontend e backend, manipulação de dados e persistência em banco de dados.

A aplicação representa uma solução simples e funcional para gerenciamento de tarefas, demonstrando a utilização integrada de diferentes tecnologias em um projeto full stack.
