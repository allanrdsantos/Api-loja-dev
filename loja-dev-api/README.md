# Read Me First
The following was discovered as part of building this project:

* The original package name 'loja.loja-dev-api' is invalid and this project uses 'loja.loja_dev_api' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.5/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.5/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.5/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.5/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/3.4.5/reference/io/validation.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# Loja Dev API

API RESTful para gerenciamento de produtos e categorias de uma loja.

## Tecnologias Utilizadas
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- H2/MySQL (dependendo da configuração)
- Swagger/OpenAPI

## Como Rodar o Projeto

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   ```
2. Entre na pasta do projeto:
   ```bash
   cd loja-dev-api
   ```
3. Execute a aplicação (exemplo usando Maven):
   ```bash
   ./mvnw spring-boot:run
   ```

A aplicação estará disponível em:
```
http://localhost:8080
```

## Documentação Swagger

Acesse a documentação interativa da API pelo Swagger UI:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Principais Endpoints

### Categoria
- `POST /categorias` — Cria uma nova categoria
- `PUT /categorias` — Atualiza uma categoria existente
- `DELETE /categorias/{idCategoria}` — Remove uma categoria por ID
- `DELETE /categorias/deleteAll` — Remove todas as categorias
- `GET /categorias/{idCategoria}` — Busca uma categoria por ID
- `GET /categorias/all?idsCategorias=1,2,...` — Busca múltiplas categorias por IDs

### Produto
- `POST /categorias/{idCategoria}/produtos` — Cria um novo produto em uma categoria
- `PUT /categorias/{idCategoria}/produtos` — Atualiza um produto em uma categoria
- `DELETE /categorias/{idCategoria}/produtos/{idProduto}` — Remove um produto por ID
- `DELETE /categorias/{idCategoria}/produtos/deleteAll` — Remove todos os produtos de uma categoria
- `GET /categorias/{idCategoria}/produtos/{idProduto}` — Busca um produto por ID
- `GET /categorias/{idCategoria}/produtos/all?idsProdutos=1,2,...` — Busca múltiplos produtos por IDs

## Exemplos de Requisições e Respostas

### Categoria

#### Criar Categoria
```
curl -X POST http://localhost:8080/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Eletrônicos",
    "descricao": "Produtos eletrônicos em geral"
  }'
```
Resposta:
```json
{
  "id": 1,
  "nome": "Eletrônicos",
  "descricao": "Produtos eletrônicos em geral"
}
```

#### Buscar Categoria por ID
```
curl http://localhost:8080/categorias/1
```
Resposta:
```json
{
  "id": 1,
  "nome": "Eletrônicos",
  "descricao": "Produtos eletrônicos em geral"
}
```

#### Atualizar Categoria
```
curl -X PUT http://localhost:8080/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "nome": "Eletrônicos",
    "descricao": "Eletrônicos e acessórios"
  }'
```
Resposta:
```json
{
  "id": 1,
  "nome": "Eletrônicos",
  "descricao": "Eletrônicos e acessórios"
}
```

#### Deletar Categoria
```
curl -X DELETE http://localhost:8080/categorias/1
```
Resposta: HTTP 204 No Content

---

### Produto

#### Criar Produto
```
curl -X POST http://localhost:8080/categorias/1/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Smartphone",
    "descricao": "Celular Android",
    "preco": 1999.99
  }'
```
Resposta:
```json
{
  "id": 10,
  "nome": "Smartphone",
  "descricao": "Celular Android",
  "preco": 1999.99,
  "categoriaId": 1
}
```

#### Buscar Produto por ID
```
curl http://localhost:8080/categorias/1/produtos/10
```
Resposta:
```json
{
  "id": 10,
  "nome": "Smartphone",
  "descricao": "Celular Android",
  "preco": 1999.99,
  "categoriaId": 1
}
```

#### Atualizar Produto
```
curl -X PUT http://localhost:8080/categorias/1/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "id": 10,
    "nome": "Smartphone",
    "descricao": "Celular Android 5G",
    "preco": 2099.99,
    "categoriaId": 1
  }'
```
Resposta:
```json
{
  "id": 10,
  "nome": "Smartphone",
  "descricao": "Celular Android 5G",
  "preco": 2099.99,
  "categoriaId": 1
}
```

#### Deletar Produto
```
curl -X DELETE http://localhost:8080/categorias/1/produtos/10
```
Resposta: HTTP 204 No Content

## Observações
- Certifique-se de que as portas e configurações do banco estejam corretas no `application.properties`.
- Para mais detalhes dos endpoints, utilize o Swagger UI.
