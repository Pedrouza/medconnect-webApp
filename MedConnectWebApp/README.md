# MedConnect Web (Spring Boot REST + JPA)

Back-end Spring REST com banco MySQL (JPA/Hibernate) e front-end estático (HTML/CSS/JS) em `src/main/resources/static`.

## Requisitos
JDK 17+, Maven (já incluso no NetBeans) e MySQL 8 em `localhost:3306`.

## Executar
1. Inicie o MySQL. O banco `medconnect_web` é criado automaticamente.
2. Se o usuário/senha do MySQL não forem `root` / vazio, defina as variáveis de ambiente `DB_USER` e `DB_PASSWORD`
   (ou edite `src/main/resources/application.properties`).
3. NetBeans: abra a pasta `MedConnectWebApp` > botão direito > Run (ou `mvn spring-boot:run`).
4. Acesse http://localhost:8080 — login: `admin@medconnect.com` / `admin123` (configurável no application.properties).

## Testes
`mvn test` (ou botão direito > Test). Os testes usam H2 em memória; não precisam de MySQL.

## API (exige login por sessão)
`POST /api/login` · `POST /api/logout` · `GET /api/me` · `GET /api/dashboard`
CRUD em `/api/pacientes`, `/api/medicos`, `/api/consultas`, `/api/prontuarios` (GET, GET/{id}, POST, PUT/{id}, DELETE/{id}).
Erros retornam JSON `{"erro": "mensagem"}`.
