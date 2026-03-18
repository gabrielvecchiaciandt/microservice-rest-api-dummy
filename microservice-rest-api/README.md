# Template de Microsserviço Rest Api com Spring Boot

## Descrição do Projeto

O projeto utiliza **Java 17**, **Spring Boot** e já vem com bibliotecas de abstração do Cofre de senhas e de Log de Aplicação. 
Além disso, o projeto segue o Clean Architecture, e utiliza o plugin Openapi Generator para gerar a especificação dos
endpoints e os dtos da aplicação, a partir da especificação openapi (swagger).

## Exemplos no Projeto

Para exemplificar o uso da aplicação, a utilização dos frameworks e arquitetura, há algumas classes 
de exemplos, simulando uma aplicação Rest que acessa um banco de dados em memória fictício.

## Requisitos

- **Java 17**
- **Gradle 8+**

## Estrutura

```
├── gradle
│   ├── wrapper
│   └── plugins
├── gradlew
├── gradlew.bat
├── gradle.properties
├── build.gradle
├── settings.gradle
├── openApiSpec.yaml
└── src
```

> **Pontos importantes da estrutura:**
> 1. Os códigos do microsserviço ficam em `src/main/java`;
> 2. As dependências do microsserviço ficam no `build.gradle`;
> 3. As versões das dependências ficam no arquivo `gradle.properties`, na raiz do projeto;
> 4. Os **testes unitários** ficam em `src/test/java`;
> 5. Os **testes integrados** ficam em `src/test/java` no pacote `application/integration`;
> 6. Configurações de plugins ficam em `gradle/plugins`;
> 7. Configuração de repositórios de dependências ou plugins estão no `settings.gradle`, na raiz do projeto;
> 8. O arquivo `openApiSpec.yaml` é utilizado para gerar as interfaces e dtos da aplicação;
> 9. **Os testes presentes em `src/test/java` no pacote `application/architecture` garantem
     que o Clean Architecture está sendo seguido corretamente, modificá-los podem desviar a aplicação deste padrão.**

### Sobre o Plugin Openapi Generator
 É o Plugin utilizado para gerar as classes Java com base na especificação openapi (swagger).

> **Explicação de como as classes são gerados e com utilizá-las**
> 1. Ao realizar o Build da aplicação `./gradlew clean build` serão geradas as classes Java, a partir da
    especificação `openApiSpec.yml` (presente na raiz do projeto);
> 2. Para verificar as classes Geradas basta ir na raiz do projeto em `build/generated/src/main/java/...`:
>    1. Neste diretório existirá dois pacotes `api` e `dto` com as classes Java geradas.
> 3. Para utilizar as classes geradas basta importá-las normalmente (o projeto já está configurado para reconhecê-las).
>
> 
> **IMPORTANTE:**
> - Realizar o Build ou Assemble da Aplicação para as classes serem geradas;
> - Deve ser feita implementação das interfaces presentes em `api` nos controllers da aplicação.
>   - Esses controllers devem ter a anotação de controller do Spring, como no exemplo presente no pacote `entrypoints/rest`.

### Divisão de pacotes seguindo Clean Architecture

```
├── configuration
├── core
    ├── dataprovider
    ├── entity
    └── usecase
├── dataproviders
└── entrypoints
```

> **Explicação dos pacotes**
> 1. `configuration`: neste pacote é onde ficam localizadas configurações relacionadas à aplicação, como a escolha e
     criação de beans, e configurações relacionadas à aplicação e frameworks;
> 2. `core`: neste pacote é onde ficam as entidades e regras de negócio, interfaces para comunicação com dataproviders e
     os casos de uso do negócio estão aqui. **Esse pacote deve ser independente de frameworks e bibliotecas.**
>    1. `core/entity`: neste pacote é onde fica os modelos de domínio da aplicação;
>    2. `core/dataprovider`: neste pacote é onde ficam interfaces (contratos), que serão implementados pelos
      dataproviders, para assim possibilitar os casos de uso (usecase) chamarem os provedores (dataproviders) sem conhecer sua implementação;
>    3. `core/usecase`: neste pacote é onde ficam os casos de uso, ou seja, as regras de negócio;
> 3. `dataproviders`: neste pacote é onde ficam implementações das interfaces definidas pelo `core/dataprovider`, e é
      onde ficam os items relacionados a infraestrutura, como banco de dados (ORM também deve ficar aqui), chamadas para
      apis externas, chamada/envio de mensagem para filas, buckets, chamada para sistemas de arquivos etc.
      Aqui também ficam as bibliotecas externas utilizadas no `core`;
> 4. `entrypoints`: neste pacote é onde ficam as formas de acesso da aplicação (endpoints),
      aqui fica a api rest, consumo de fila, grpc, jobs etc.

> Ler mais sobre [aqui](https://alelo.helpjuice.com/padr%C3%B5es-t%C3%A1ticos/padr%C3%A3o-de-desenvolvimento-clean-architecture?from_search=155281677)

## Configurações da Aplicação

As configurações da aplicação ficam no arquivo `application.yml` localizado em `src/main/resources`.

## Build & Run

Antes de realizar o build do projeto, verifique se você tem permissão para executar o comando ./gradlew. Caso tenha
algum problema, execute o comando abaixo para liberar a permissão:

```shell
chmod +x ./gradlew
```

### Build da aplicação

```shell
./gradlew clean build
```

### Rodando a aplicação

```shell
./gradlew bootRun
```

## Sugestões e Reportes

Tem alguma sugestão de melhoria ou quer reportar algum erro?
[Clique aqui](https://dev.azure.com/alelo/Plataformas)

Quer contribuir com o projeto?
[Clique aqui](https://dev.azure.com/alelo/Plataformas/_git/template-ms-rest-api-spring-boot)

## Referências

- [Alelo - Api First](https://alelo.helpjuice.com/arquitetura/api-first?from_search=166360775)
- [Alelo - Clean Architecture](https://alelo.helpjuice.com/padr%C3%B5es-t%C3%A1ticos/padr%C3%A3o-de-desenvolvimento-clean-architecture?from_search=155281677)
- [Cofre de senhas](https://alelo.helpjuice.com/como-utilizar/como-utilizar-a-biblioteca-de-cofre-de-senhas?from_search=155931448)
- [Log de Aplicação](https://alelo.helpjuice.com/pt_BR/alelo-system-logger-log-de-aplicacao/biblioteca-de-log-de-aplicacao-application-logger)
- [Openapi Generator](https://openapi-generator.tech/)