# Todos Eventos

## Descrição
O projeto **Todos Eventos** é um sistema de gerenciamento de eventos que permite aos usuários cadastrar, consultar, atualizar e excluir eventos. Além disso, o sistema permite a participação em eventos e envia email os usuários sobre eventos que escolheu participar, passando as informações do evento.

## Documentação da Historia e cards de desenvolvimento do Sistema
- link:https://willianssantos227.atlassian.net/jira/software/projects/SPE/list?atlOrigin=eyJpIjoiMzk0YzVlMWRiNzYyNDAzNGExNTcyMTE3OGUyNzBkNDEiLCJwIjoiaiJ9

## Funcionalidades
- Cadastro de usuários
- Consulta de usuários
- Cadastro de eventos
- Consulta de eventos
- Participação em eventos
- Manipulção da base de dados por functions
- Cancelamento de participação em eventos
- Consumo de APIs externas para obtenção de endereço e envio de e-mails


## Tecnologias Utilizadas
- Java 11
- Spring Boot 2.5.4
- Spring Security
- JdbcTamplate
- PostgreSQL
- Maven
- Swagger (OpenAPI 3)
- JavaMailSender (para envio de e-mails)

## Configurações de Banco de Dados
O sistema está configurado para usar um banco de dados PostgreSQL. As configurações de conexão estão no arquivo `application.yaml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todos_eventos_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
````
## Configuração do Swagger
O Swagger está configurado para documentar e testar as APIs do sistema. A documentação do Swagger pode ser acessada em: http://localhost:27031/swagger-ui.html.

## Como Executar o Projeto
 1. Clone o repositório:
  - git clone <url-do-repositorio>
 2. Configure o banco de dados PostgreSQL e atualize as credenciais no application.properties.
 3. Execute o projeto usando Maven: 
  - mvn spring-boot:run 

## Scrip da base de dados e as functions

- Link: https://bitbucket.org/spe-todos-ventos/script_e_fuctions_basededados/src/master/

- Observção: Realizar a execução do script da base de dados primeiro, e logo apos realizar a execução dos scripts das functions
