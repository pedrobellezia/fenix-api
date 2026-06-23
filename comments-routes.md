# Documentação das Rotas de Comentários (Comments)

Este documento detalha o uso das rotas de comentários disponíveis na API, com exemplos de como enviar requisições. O sistema agora suporta a extração automática do usuário autenticado via JWT (JSON Web Token), além de oferecer uma rota intuitiva para listar os comentários vinculados a uma postagem específica.

---

## 1. Criar um Comentário

Esta rota permite que um usuário autenticado crie um novo comentário em um post. **Não é necessário** passar o ID do usuário no corpo da requisição, pois a API o extrai automaticamente a partir do token JWT enviado no cabeçalho.

- **Método:** `POST`
- **URL:** `/api/comments`
- **Autenticação Obrigatória:** Sim (Bearer Token no Header `Authorization`)

### Formato da Requisição (Header)
```http
Authorization: Bearer <seu-token-jwt>
```

### Formato do Body (JSON)
O corpo da requisição precisa conter, no mínimo, o texto do comentário (`body`) e a referência do Post (`post.id`) que está sendo comentado.

```json
{
  "body": "Este é um comentário muito legal!",
  "post": {
    "id": "123e4567-e89b-12d3-a456-426614174000"
  }
}
```

### Exemplo de Resposta de Sucesso (`200 OK`)
```json
{
  "id": "987e6543-e21b-34d1-b654-123414174999",
  "body": "Este é um comentário muito legal!",
  "post": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    ...
  },
  "user": {
    "id": "456e1234-a89b-45c3-a456-996614174111",
    "email": "usuario@email.com",
    ...
  },
  "active": true,
  "createdAt": "2026-06-23T14:00:00",
  "updatedAt": "2026-06-23T14:00:00"
}
```

---

## 2. Listar Todos os Comentários de um Post

Esta rota permite buscar a lista completa de comentários associados a um determinado post.

- **Método:** `GET`
- **URL:** `/api/post/{id}/comments`  *(Também suporta `/api/posts/{id}/comments`)*
- **Parâmetros de Rota:**
  - `id` (UUID): O identificador único do Post.
- **Autenticação Obrigatória:** Sim (se o restante do `PostController` estiver protegido por segurança global).

### Exemplo de Requisição
```http
GET /api/post/123e4567-e89b-12d3-a456-426614174000/comments
```

### Exemplo de Resposta de Sucesso (`200 OK`)
Retorna uma lista contendo os comentários:

```json
[
  {
    "id": "987e6543-e21b-34d1-b654-123414174999",
    "body": "Este é um comentário muito legal!",
    "user": {
      "id": "456e1234-a89b-45c3-a456-996614174111",
      "name": "Nome do Usuário",
      "email": "usuario@email.com"
    },
    "active": true,
    "createdAt": "2026-06-23T14:00:00"
  },
  {
    "id": "111e2222-e33b-44d4-b555-555514174888",
    "body": "Concordo totalmente!",
    "user": {
      "id": "777e8888-a99b-00c0-a111-226614174333",
      "name": "Outro Usuário",
      "email": "outro@email.com"
    },
    "active": true,
    "createdAt": "2026-06-23T14:05:00"
  }
]
```

---

## 3. Responder a um Comentário (Reply)

O banco de dados e a API possuem suporte nativo para respostas (comentários recursivos). Para responder a um comentário existente, basta utilizar a mesma rota de criação de comentário (`POST /api/comments`), adicionando a propriedade `parentComment` apontando para o ID do comentário pai.

### Formato do Body (JSON) para uma Resposta
```json
{
  "body": "Essa é a minha resposta ao seu comentário!",
  "post": {
    "id": "123e4567-e89b-12d3-a456-426614174000"
  },
  "parentComment": {
    "id": "987e6543-e21b-34d1-b654-123414174999"
  }
}
```

### Lidando com as Respostas no Frontend

Quando você utiliza o endpoint `GET /api/post/{id}/comments`, a API retorna uma lista "plana" (flat) contendo **todos** os comentários (tanto os pais quanto as respostas) daquele Post.

Para renderizar corretamente a árvore no Frontend (Vue/React/etc.), você precisará agrupar ou filtrar pela propriedade `parentComment`:
- **Comentários Principais (Raiz):** Serão aqueles em que a propriedade `parentComment` vem como `null`.
- **Respostas (Replies):** Serão aqueles em que a propriedade `parentComment` vem preenchida. Para listar as respostas de um comentário principal, você filtra o array onde `parentComment.id === id_do_comentario_pai`.

---

## Resumo das Mudanças Implementadas

1. **Assinatura de JWT Automática:** Você não precisa mais enviar detalhes do autor na hora de comentar. O próprio backend lê o `Authorization Bearer Token`, decodifica o e-mail, busca o usuário no banco de dados e vincula ao comentário criado de forma segura.
2. **Rota Semântica de Leitura:** Em vez de manter a leitura de comentários em uma rota isolada, ela foi integrada aos endpoints de *Posts* sob a semântica de "Um Post tem muitos Comentários" (`/api/post/{id}/comments`), sendo intuitivo para o frontend.
3. **Datas e Tempos Gerados pelo Banco:** Agora os campos `createdAt` e `updatedAt` dos comentários são devidamente registrados e incluídos instantaneamente na resposta de criação através da integração fina entre as anotações do Hibernate (`@CreationTimestamp`).
