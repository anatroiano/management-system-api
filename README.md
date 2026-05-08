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

---

## 🏗️ Arquitetura

O projeto segue arquitetura em camadas:

```txt
Controller -> Service -> Repository -> Database
```

## ▶️ Como executar o projeto

### Pré-requisitos

- Java 21+
- Maven
- PostgreSQL

---

### Configuração do banco

Configure o `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/management_system
spring.datasource.username=postgres
spring.datasource.password=sua_senha
```

---

### Executar aplicação

```bash
mvn spring-boot:run
```

---

### Executar testes

```bash
mvn test
```

---

## 📚 Swagger

Disponível em:

```txt
http://localhost:8080/swagger-ui/index.html
```

## 🚀 Próximos passos

- Desenvolvimento do front-end com Angular
- Dockerização da aplicação
- Pipeline CI/CD
- Dashboard e relatórios

---

## 👩‍💻 Autora

Ana Carolina Troiano