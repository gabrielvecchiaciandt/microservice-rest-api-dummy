# User Story: Gerenciamento de Catálogo de Produtos

## 📋 Identificação
**ID:** US-001  
**Título:** Gerenciamento de Catálogo de Produtos  
**Épico:** Sistema de Vendas  
**Sprint:** A definir  

---

## 👤 Como Usuário do Sistema

**Eu quero** gerenciar um catálogo de produtos através de uma API REST  
**Para que** eu possa criar, visualizar, atualizar e remover produtos do sistema  
**De modo que** o catálogo esteja sempre atualizado e acessível para consultas

---

## ✅ Critérios de Aceite

### CA1: Listar todos os produtos
**Dado que** existem produtos cadastrados no sistema  
**Quando** eu realizar uma requisição GET para `/api/v1/produtos`  
**Então** o sistema deve retornar:
- Status HTTP 200 (OK)
- Lista com todos os produtos cadastrados
- Cada produto deve conter: id, nome, descrição, preço, categoria, dataCadastro

**E** caso não existam produtos cadastrados  
**Então** o sistema deve retornar lista vazia com status 200

---

### CA2: Buscar produto por ID
**Dado que** existe um produto com ID específico no sistema  
**Quando** eu realizar uma requisição GET para `/api/v1/produtos/{id}`  
**Então** o sistema deve retornar:
- Status HTTP 200 (OK)
- Dados completos do produto

**E** caso o produto não exista  
**Então** o sistema deve retornar:
- Status HTTP 404 (Not Found)
- Mensagem de erro descritiva

---

### CA3: Cadastrar novo produto
**Dado que** eu tenho os dados válidos de um novo produto  
**Quando** eu realizar uma requisição POST para `/api/v1/produtos` com os dados:
```json
{
  "nome": "Notebook Dell",
  "descricao": "Notebook i7 16GB RAM",
  "preco": 4500.00,
  "categoria": "INFORMATICA"
}
```
**Então** o sistema deve:
- Validar todos os campos obrigatórios
- Gerar um ID único para o produto
- Registrar a data/hora do cadastro
- Salvar o produto no banco de dados
- Retornar status HTTP 201 (Created)
- Retornar o produto criado com todos os campos preenchidos
- Incluir header `Location` com a URI do recurso criado

**E** caso os dados sejam inválidos  
**Então** o sistema deve retornar:
- Status HTTP 400 (Bad Request)
- Lista de erros de validação

---

### CA4: Atualizar produto existente
**Dado que** existe um produto cadastrado no sistema  
**Quando** eu realizar uma requisição PUT para `/api/v1/produtos/{id}` com dados atualizados  
**Então** o sistema deve:
- Validar todos os campos obrigatórios
- Atualizar os dados do produto
- Manter o ID e dataCadastro originais
- Retornar status HTTP 200 (OK)
- Retornar o produto atualizado

**E** caso o produto não exista  
**Então** o sistema deve retornar status HTTP 404 (Not Found)

**E** caso os dados sejam inválidos  
**Então** o sistema deve retornar status HTTP 400 (Bad Request)

---

### CA5: Remover produto
**Dado que** existe um produto cadastrado no sistema  
**Quando** eu realizar uma requisição DELETE para `/api/v1/produtos/{id}`  
**Então** o sistema deve:
- Remover o produto do banco de dados
- Retornar status HTTP 204 (No Content)

**E** caso o produto não exista  
**Então** o sistema deve retornar status HTTP 404 (Not Found)

---

## 🔧 Requisitos Técnicos

### Validações de Negócio
- **Nome**: obrigatório, mínimo 3 caracteres, máximo 100 caracteres
- **Descrição**: opcional, máximo 500 caracteres
- **Preço**: obrigatório, deve ser maior que zero, até 2 casas decimais
- **Categoria**: obrigatória, deve ser um valor do enum (INFORMATICA, ELETRONICOS, MOVEIS, LIVROS, OUTROS)

### Stack Técnica
- Java 17 + Spring Boot 3.5+
- Banco de dados H2 (em memória para desenvolvimento)
- Spring Data JPA para persistência
- Arquitetura Hexagonal (Clean Architecture)
- OpenAPI 3.0 para documentação da API
- JUnit Jupiter + AssertJ para testes

### Modelo de Dados
```
Produto
- id: Long (PK, auto-increment)
- nome: String (not null)
- descricao: String (nullable)
- preco: BigDecimal (not null)
- categoria: String/Enum (not null)
- dataCadastro: LocalDateTime (not null)
```

---

## 🎯 Definição de Pronto (DoD)

- [ ] Código implementado seguindo Clean Architecture
- [ ] OpenAPI spec criado e validado
- [ ] Controllers implementados e testados
- [ ] Use Cases de negócio implementados
- [ ] Data Providers implementados com Spring Data JPA
- [ ] Banco H2 configurado e funcional
- [ ] Testes unitários com cobertura mínima de 80%
- [ ] Testes de integração implementados
- [ ] Documentação Swagger/OpenAPI acessível
- [ ] Code review aprovado
- [ ] Build passando sem erros
- [ ] API testada manualmente (Postman/Insomnia)

---

## 📊 Estimativa
**Story Points:** 8 pontos  
**Estimativa de Tempo:** 2-3 dias

---

## 📝 Notas Técnicas

### Estrutura de Pacotes
```
core/
  entity/
    - Produto.java
    - CategoriaEnum.java
  usecase/
    - CriarProdutoUseCase.java
    - ListarProdutosUseCase.java
    - BuscarProdutoPorIdUseCase.java
    - AtualizarProdutoUseCase.java
    - RemoverProdutoUseCase.java
  dataprovider/
    - ProdutoDataProvider.java

dataproviders/
  database/
    entity/
      - ProdutoEntity.java
    repository/
      - ProdutoRepository.java
    - ProdutoDataProviderImpl.java

entrypoints/
  rest/
    - ProdutosController.java
    mapper/
      - ProdutoMapper.java

configuration/
  - ProdutoUseCaseConfiguration.java
```

---

## 🔗 Dependências
- Nenhuma dependência de outras US
- Configuração inicial do projeto já existente

---

## 📚 Referências
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [H2 Database Documentation](http://www.h2database.com)
- [OpenAPI Specification](https://swagger.io/specification/)