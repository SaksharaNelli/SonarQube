# Bank Management System (SonarQube DevOps demo)

A small Java 17 + Maven application whose purpose is to be analysed by SonarQube inside a Jenkins pipeline.

## Layout
- `src/main/java/com/example/bank` : clean application code (model, repository, service, util, exception)
- `src/main/java/com/example/bank/legacy` : INTENTIONALLY BAD code that SonarQube will flag
- `src/test/java` : JUnit 5 tests (coverage measured by JaCoCo)
- `docker-compose.yml` : SonarQube + PostgreSQL (Jenkins runs directly on your PC)
- `Jenkinsfile` : Build & Test -> SonarQube Analysis -> Quality Gate -> Package

## Quick start
1. `docker compose up -d`
2. SonarQube: http://localhost:9000 (admin / admin), Jenkins: http://localhost:8080
3. Local run without Jenkins:
   `mvn clean verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.token=YOUR_TOKEN`

## Demo flow
1. Scan with the `legacy` package present -> Quality Gate FAILS.
2. Delete `src/main/java/com/example/bank/legacy`, commit, push -> Quality Gate PASSES.
