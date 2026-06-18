# Universe Galaxy: guia para agentes de IA

Este projeto e a base da Universe Framework. O modulo chama-se **Universe Galaxy** (`artifactId: galaxy`) e e empacotado como **EJB**. Ele fornece duas coisas em simultaneo:

1. Uma biblioteca base de arquitetura para APIs Java EE/JAX-RS com persistencia JPA.
2. Um conjunto inicial de entidades e fluxos de negocio para projetos que precisem nascer com gestao base de utilizadores, empresas, organizacoes, trabalhadores, convites, imagens, features, tokens e email.

Este documento existe para orientar agentes de IA que venham a criar novos projetos ou funcionalidades em cima desta framework.

## Modelo mental

A framework segue uma organizacao em camadas inspirada em **Model / Controller / View REST**:

- **Entity**: modelo persistente JPA, em `eu.lpinto.universe.persistence.entities`.
- **Facade**: acesso a dados e queries JPA, em `eu.lpinto.universe.persistence.facades`.
- **Controller**: regras de negocio, pre-condicoes e permissoes, em `eu.lpinto.universe.controllers`.
- **DTS**: transformacao entre dominio e API, em `eu.lpinto.universe.api.dts`.
- **DTO**: contrato JSON exposto pela API, em `eu.lpinto.universe.api.dto`.
- **Service**: endpoints REST JAX-RS, em `eu.lpinto.universe.api.services`.

O fluxo normal de uma chamada REST e:

```text
HTTP request
  -> AccessTokenValidation / filtros
  -> Service REST
  -> DTS.toDomain(...)
  -> Controller
  -> Facade
  -> EntityManager / Base de dados
  -> Facade
  -> Controller
  -> DTS.toAPI(...)
  -> HTTP response JSON
```

## Classes base

### Entity

A entidade base e `AbstractEntity`, que implementa `UniverseEntity`.

Campos comuns:

- `id`
- `name`
- `creator`
- `created`
- `updater`
- `updated`
- `deleted`
- `full`
- `migrationCode`
- `migrationId`

`AbstractEntity` tambem inclui validacoes simples contra valores nulos/vazios e o helper `buildCode(...)`, usado para gerar nomes/codigos normalizados.

Quando criares uma nova entidade persistente:

- colocar em `eu.lpinto.universe.persistence.entities`;
- anotar com `@Entity`;
- estender `AbstractEntity` sempre que a entidade fizer parte do dominio normal da framework;
- usar referencias lazy (`FetchType.LAZY`) por defeito, como no codigo existente;
- criar construtores leves por `id`, porque os DTS usam frequentemente entidades parciais apenas com identificador.

### Facade

A fachada base e `AbstractFacade<T>`, que implementa `Facade<T>`.

Responsabilidades:

- encapsular o `EntityManager`;
- fazer `find`, `retrieve`, `create`, `edit`, `remove`;
- inicializar auditoria de entidades (`created`, `updated`, `full`) no momento de `create`;
- concentrar queries especificas da entidade.

Cada facade concreta deve:

- ser `@Stateless`;
- injetar `EntityManager` com `@PersistenceContext`;
- chamar `super(EntityClass.class)` no construtor;
- implementar `getEntityManager()`;
- sobrescrever `find(options)` quando a listagem precisar de filtros obrigatorios.

Importante: algumas facades impedem listagens globais por seguranca/semantica. Exemplo: `UserFacade.find(...)` nao permite listar todos os utilizadores sem filtro.

### Controller

A base comum e `AbstractController`. Para CRUD, usar `AbstractControllerCRUD<E extends UniverseEntity>`.

Responsabilidades:

- validar pre-condicoes;
- verificar permissoes;
- aplicar regras de negocio antes/depois da persistencia;
- definir auditoria de `creator` e `updater`;
- orquestrar criacoes relacionadas.

Metodos importantes para customizar:

- `doFind(...)`
- `doRetrieve(...)`
- `doCreate(...)`
- `doUpdate(...)`
- `doDelete(...)`
- `assertPremissionsCreate(...)`
- `assertPremissionsRead(...)`
- `assertPremissionsUpdateDelete(...)`

Nota: o nome dos metodos contem o typo historico `Premissions`; manter o nome existente ao sobrescrever.

O utilizador de sistema/admin e tratado por `isSystemAdmin(userID)`, onde `userID == 0`.

### DTO

A hierarquia REST comeca em `UniverseDTO`, que contem `id`. `AbstractDTO` acrescenta os campos comuns de `AbstractEntity` para a API:

- `name`
- `creator`
- `created`
- `updater`
- `updated`
- `deleted`
- `migrationCode`
- `migrationId`

DTOs sao os contratos JSON. Evita expor entidades JPA diretamente nos Services.

### DTS

DTS significa Data Transformation Service. A base e:

- `DTS<E, D>`
- `UniverseDTS<E extends UniverseEntity, D extends UniverseDTO>`
- `AbstractDTS<E extends AbstractEntity, D extends AbstractDTO>`

Responsabilidades:

- converter DTO para Entity: `toDomain(...)`;
- converter Entity para DTO: `toAPI(...)`;
- converter IDs em referencias de dominio leves;
- decidir se campos relacionados entram completos ou apenas por ID.

O padrao local usa uma instancia singleton publica por DTS:

```java
public static final MinhaEntidadeDTS T = new MinhaEntidadeDTS();
```

Em entidades com relacoes, seguir o estilo de `OrganizationDTS`: quando `entity.isFull()` e verdadeiro, inclui a totalidade dos dados pois é um retrive; quando falso, devolver uma representacao mais leve para usar em listagens.

### Service

Os Services JAX-RS vivem em `eu.lpinto.universe.api.services`.

`AbstractService` fornece:

- construcao de `options`;
- parsing de query params e headers;
- helpers de respostas HTTP;
- logs de request/response;
- tratamento de timeouts e emails de erro.

`AbstractServiceCRUD` fornece endpoints CRUD assincronos:

- `GET /resource`
- `GET /resource/{id}`
- `POST /resource`
- `POST /resource/list`
- `PUT /resource/{id}`
- `DELETE /resource/{id}`

Cada Service concreto deve:

- ter `@Path("...")`;
- estender `AbstractServiceCRUD<Entity, DTO, Controller, DTS>`;
- injetar o controller com `@EJB`;
- passar o DTS no construtor;
- implementar `getController()`.

Exemplo de referencia: `UserService`.

## Autenticacao e autorizacao

O filtro principal e `AccessTokenValidation`.

Comportamento:

- le header `Authorization`;
- espera token no formato `Bearer <token>`;
- usa `UserFacade.getbyToken(...)` para obter o utilizador;
- adiciona headers internos `userID` e `GOD`;
- permite endpoints publicos definidos na DMZ.

Constantes importantes:

- `UniverseFundamentals.AUTH_USER_ID = "userID"`
- `UniverseFundamentals.AUTH_GOD = "GOD"`
- `UniverseFundamentals.REST_BASE_URI = "api"`

Endpoints DMZ base incluem, entre outros:

- `GET /api/about/you`
- `GET /api/features`
- `GET /api/invites/*`
- `POST /api/tokens`
- `POST /api/users`
- `POST /api/workers`
- `POST /api/users/passwordRecovery`
- `PUT /api/emailValidations`

Endpoints adicionais podem ser configurados via propriedade `DMZ`.

## Configuracao

`UniverseFundamentals` carrega configuracao a partir de `universe.properties` no classpath.

Propriedades relevantes:

- `APP_NAME`
- `ENVIROMENT`
- `HOST`
- `VERSION`
- `default_plan`
- `IMPORTS_FOLDER`
- `DATA_STORE_FOLDER`
- `IMAGES_URL`
- configuracao SMTP/email
- `DMZ`
- `do_not_timeout`
- `debug_level`

O recurso `src/main/resources/properties.properties` contem `project.version=${project.version}` e e filtrado pelo Maven. Confirma sempre, no projeto final, como `universe.properties` e fornecido pelo parent, pelo empacotamento ou pelo ambiente.

## Entidades base incluidas

Este modulo ja traz entidades de bootstrap funcional para uma aplicacao:

- `User`: utilizador da aplicacao, credenciais, avatar, tokens, permissao `god`.
- `Token`: token de autenticacao associado a utilizador.
- `Company`: empresa/agregador.
- `Employee`: ligacao entre `User` e `Company`.
- `Organization`: organizacao operacional dentro de uma empresa.
- `Worker`: trabalhador de uma organizacao, ligado a um `Employee`.
- `Feature` e `OrganizationFeature`: funcionalidades disponiveis e associacao a organizacoes.
- `Invite`: convite.
- `Image`: imagens/avatars.
- `EmailValidation`: validacao de email.
- `Email`, `EmailConfig` e facades/controllers relacionados: infraestrutura de comunicacao.

Nao existe, no codigo atual, uma classe unica chamada `Bootstrap`. O bootstrap e o conjunto destas entidades, endpoints, controllers e regras ja existentes. Projetos derivados devem aproveitar este modulo como base de dados e API inicial, estendendo-o com novas entidades de negocio.

## Regras de negocio base observadas

Alguns comportamentos importantes:

- `UserController.doCreate(...)` pode criar avatar inicial e gerar validacao de email quando `baseUrl` e informado.
- `UserController.doDelete(...)` faz soft delete (`deleted`) e remove tokens, alem de enviar pedido de apagamento de conta.
- `OrganizationController.doCreate(...)` exige empresa existente e que o utilizador seja empregado dessa empresa; depois cria a organizacao e cria automaticamente um `Worker` admin para o utilizador.
- `OrganizationController.doDelete(...)` faz desativacao logica (`enable=false`), nao remocao fisica.
- `WorkerController` cria/reativa workers, pode criar `User`/`Employee` relacionados e tambem usa soft delete via `enable=false`.
- `AbstractControllerCRUD` marca `creator` no create e `updater` no update para entidades que estendem `AbstractEntity`.

## Como adicionar uma nova entidade de negocio

Usar esta sequencia para manter a arquitetura consistente:

1. Criar a Entity em `persistence.entities`.
2. Criar o DTO em `api.dto`.
3. Criar o DTS em `api.dts`.
4. Criar a Facade em `persistence.facades`.
5. Criar o Controller em `controllers`.
6. Criar o Service REST em `api.services`.
7. Adicionar regras de permissao no Controller.
8. Adicionar queries especificas na Facade, nao no Service.
9. Adicionar endpoints customizados no Service apenas quando o CRUD generico nao for suficiente.
10. Validar mapeamentos DTO/Entity nos dois sentidos.
11. Criar o documento SQL da entidade, com o mesmo nome da entidade e extensao `.sql`.

### Documento SQL por entidade

Para todas as entidades deve existir um ficheiro SQL com o mesmo nome da entidade Java e extensao `.sql`.

Exemplos:

- `Organization.java` deve ter `Organization.sql`;
- `Worker.java` deve ter `Worker.sql`;
- `OrganizationCommunicationEmail.java` deve ter `OrganizationCommunicationEmail.sql`.

O ficheiro SQL deve documentar e criar a tabela correspondente, seguindo a estrutura usada pela framework:

- cabecalho com o nome/tipo da entidade;
- bloco de colunas comuns da `AbstractEntity`;
- bloco de colunas especificas da entidade;
- bloco de constraints;
- foreign keys de auditoria para `ApplicationUser`;
- foreign keys especificas da entidade;
- quando a tabela for extensao/intermediaria dependente de outra entidade, usar a foreign key sobre `id` com a regra adequada, por exemplo `ON DELETE CASCADE`.

Modelo de referencia:

```sql
-- ---------------------------------------------------------------------------------------------------------------------
-- - Intermediary
-- ---------------------------------------------------------------------------------------------------------------------
CREATE TABLE OrganizationCommunicationEmail (

    -- -- -- -- -- --
    --   Columns   --
    -- -- -- -- -- --
    -- abstract entity
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    created TIMESTAMP NULL  DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP NULL  ON UPDATE CURRENT_TIMESTAMP,
    creator_id BIGINT NULL DEFAULT NULL,
    updater_id BIGINT NULL DEFAULT NULL,
    deleted TIMESTAMP NULL DEFAULT NULL,

    -- specific columns
    replyToEmail VARCHAR(50),
    replyToName VARCHAR(50),
    senderName VARCHAR(50),
    SMTP_USER VARCHAR(50),
    SMTP_PASSWORD VARCHAR(100),
    SMTP_ADDR VARCHAR(50),
    SMTP_PORT INT,

    -- -- -- -- -- --
    -- constraints --
    -- -- -- -- -- --
    -- abstract entity
    FOREIGN KEY fk_OrganizationCommunicationEmail_creator (creator_id) REFERENCES ApplicationUser (id) ON DELETE SET NULL,
    FOREIGN KEY fk_OrganizationCommunicationEmail_updater (updater_id) REFERENCES ApplicationUser (id) ON DELETE SET NULL,
    -- specific columns
    FOREIGN KEY fk_OrganizationCommunicationEmail_organization (id) REFERENCES Organization (id) ON DELETE CASCADE
);
```

Esqueleto esperado:

```java
@Entity
public class ProjectThing extends AbstractEntity {
    public ProjectThing() {}
    public ProjectThing(Long id) { super(id); }
}
```

```java
@Stateless
public class ProjectThingFacade extends AbstractFacade<ProjectThing> {
    @PersistenceContext
    private EntityManager em;

    public ProjectThingFacade() {
        super(ProjectThing.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
```

```java
@Stateless
public class ProjectThingController extends AbstractControllerCRUD<ProjectThing> {
    @EJB
    private ProjectThingFacade facade;

    public ProjectThingController() {
        super(ProjectThing.class.getCanonicalName());
    }

    @Override
    protected Facade<ProjectThing> getFacade() {
        return facade;
    }
}
```

```java
@Path("projectThings")
public class ProjectThingService extends AbstractServiceCRUD<ProjectThing, ProjectThingDTO, ProjectThingController, ProjectThingDTS> {
    @EJB
    private ProjectThingController controller;

    public ProjectThingService() {
        super(ProjectThingDTS.T);
    }

    @Override
    protected ProjectThingController getController() {
        return controller;
    }
}
```

## Convencoes para agentes

Ao modificar ou estender o projeto:

- respeitar os pacotes existentes;
- nao colocar regras de negocio no Service;
- nao colocar queries de base de dados no Controller ou no Service;
- usar DTS para qualquer conversao entre DTO e Entity;
- preservar o padrao EJB/JAX-RS/JPA existente;
- manter soft delete quando a entidade ja usa `deleted` ou `enable`;
- manter o mecanismo de `options` para contexto da chamada;
- manter `userID` e `GOD` como headers internos de autorizacao;
- evitar listagens globais quando a entidade e sensivel;
- preferir `PreConditionException`, `PermissionDeniedException` e `UnknownIdException` em vez de erros genericos;
- preservar typos historicos em assinaturas publicas quando necessario para override/compatibilidade;
- verificar se o projeto consumidor fornece `universe.properties`.

## Estrutura principal

```text
src/main/java/eu/lpinto/universe
  api
    bodywriters
    dto
    dts
    filters
    services
    servlets
    util
  controllers
    exceptions
  persistence
    entities
    facades
  util
src/main/resources
```

## Build

O projeto e Maven e herda de:

```xml
<groupId>eu.lpinto.universe</groupId>
<artifactId>spark</artifactId>
<version>2.5.2</version>
```

O modulo atual:

```xml
<artifactId>galaxy</artifactId>
<packaging>ejb</packaging>
<version>2.6.2</version>
```

## Checklist antes de entregar alteracoes

- A nova Entity compila e tem construtor vazio e construtor por `id`.
- Existe um ficheiro `.sql` com o mesmo nome da entidade e a estrutura de tabela correspondente.
- O DTO nao expoe objetos JPA diretamente.
- O DTS cobre `toAPI`, `toDomain(Long id)` e `buildEntity`.
- O Controller define permissoes adequadas.
- A Facade contem queries e filtros necessarios.
- O Service usa `AbstractServiceCRUD` quando possivel.
- Os endpoints publicos, se existirem, foram considerados na DMZ.
- O comportamento de delete e coerente: fisico, `deleted` ou `enable=false`.
- O projeto consumidor tem configuracao `universe.properties`.
