# 📦 Management System API

API REST para gerenciamento de produtos, clientes, vendas e estoque, desenvolvida com Java e Spring Boot.

O projeto foi desenvolvido com foco em boas práticas REST, regras de negócio, autenticação JWT e testes automatizados.

---

## ✨ Funcionalidades

- Gerenciamento de produtos
- Gerenciamento de clientes
- Gerenciamento de vendas
- Controle de estoque
- Atualização automática do estoque após vendas
- Cancelamento de vendas com restauração de estoque
- Dashboard para acompanhamento das vendas e estoque
- Autenticação com JWT
- Paginação de endpoints
- Documentação com Swagger/OpenAPI
- Testes automatizados

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Maven
- Swagger/OpenAPI
- JUnit 5 + Mockito
- Lombok
- Docker
- Docker Compose

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```txt
Controller -> Service -> Repository -> Database
```

---

## 🐳 Como executar

### Pré-requisitos

* Docker
* Docker Compose

### Configuração

Copie o arquivo `.env.example` para `.env`:

```bash
cp .env.example .env
```

Em seguida, edite o arquivo .env e configure os valores das variáveis de ambiente.

### Execução

Para executar a aplicação:

```bash
docker compose up --build
```

> Use `--build` na primeira execução e sempre que alterar o código-fonte, o `pom.xml` ou o `Dockerfile`. Nas demais,
> utilize apenas:
> ```bash
> docker compose up
> ```

Para executar o ambiente de desenvolvimento:

```bash
docker compose -f compose-dev.yaml up --build
```

> Use `--build` apenas na primeira execução ou ao alterar o `pom.xml`/`Dockerfile-dev`. Nas demais:
> ```bash
> docker compose -f compose-dev.yaml up
> ```

A API estará disponível em:

```text
http://localhost:8080
```

---

## 📚 Swagger / OpenAPI

Com a aplicação em execução:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 🖥️ Frontend

O frontend da aplicação está disponível no
repositório: [GestãoPro — Frontend](https://github.com/anatroiano/management-system-web)

---

## 👩‍💻 Autora

Ana Carolina Troiano