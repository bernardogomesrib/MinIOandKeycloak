FROM maven:3.6.3-jdk-11 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /build/src/
RUN mvn clean package -DskipTests

FROM amazoncorretto:21
ENV SERVER_PORT=6000




WORKDIR /app
COPY --from=build /build/target/*.jar /app/app.jar

EXPOSE ${SERVER_PORT}


ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://demo_dbPostgressquiz:5432/postgres
ENV SPRING_DATASOURCE_USERNAME=usuario
ENV SPRING_DATASOURCE_PASSWORD=senha1234
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
ENV SPRING_DATASOURCE_PLATFORM=postgres
ENV SPRING_DATASOURCE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
ENV SPRING_DATASOURCE_SHOW_SQL=true
ENV SPRING_DATASOURCE_HBM2DDL_AUTO=update
ENV SPRING_ISSUER_URI=http://localhost:8080/realms/quiz
ENV SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html
ENV SPRING_APPLICATION_NAME=cal
ENV APPLICATION_URL=http://localhost:3000



CMD java -jar -Dapplication.url=${APPLICATION_URL} -Dspring.data.jdbc.dialect=${SPRING_DATASOURCE_DIALECT} -Dspring.jpa.hibernate.ddl-auto=${SPRING_DATASOURCE_HBM2DDL_AUTO} -Dspring.datasource.driver-class-name=${SPRING_DATASOURCE_DRIVER_CLASS_NAME} -Dspring.swagger-ui.path=${SPRINGDOC_SWAGGER_UI_PATH} -Dspring.server.port=${SERVER_PORT} -Dspring.datasource.url=${SPRING_DATASOURCE_URL} -Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME} -Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD} app.jar




# #swagger
# #aplicação
# #banco de dados

# #keycloak
# spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/quiz




















# services:
#     demo_dbPostgressquiz:
#       container_name: demo_dbPostgressquiz
#       image: postgres
#       restart: always
#       ports:
#         - "5432:5432"
#       environment:
#         POSTGRES_PASSWORD: senha1234
#         POSTGRES_USER: usuario
#         POSTGRES_DB: postgres
#       volumes:
#         - ./postgres-data:/var/lib/postgresql/data
  
#     demo_minio:
#       container_name: demo_minio
#       image: quay.io/minio/minio:latest
#       restart: always
#       ports:
#         - "9000:9000"
#         - "9090:9090"
#       environment:
#         MINIO_ACCESS_KEY: minio
#         MINIO_SECRET_KEY: minio123
#         MINIO_VOLUMES: "/data"
#         MINIO_NOTIFY_POSTGRES_ENABLE: "on"
#         MINIO_NOTIFY_POSTGRES_CONNECTION_STRING: "host=demo_dbPostgressquiz user=usuario password=senha1234 dbname=postgres sslmode=disable"
#         MINIO_NOTIFY_POSTGRES_TABLE: "minio"
#         MINIO_NOTIFY_POSTGRES_FORMAT: "namespace"
#       volumes:
#         - ./minio-data:/data
#       command: server --console-address ":9090" /data
#       healthcheck:
#         test: ["CMD", "curl", "-f", "http://localhost:9000/minio/health/live"]
#         interval: 30s
#         timeout: 20s
#         retries: 3
#       depends_on:
#         - demo_dbPostgressquiz
#     demo_keycloak:
#       container_name: demo_keycloak
#       image: quay.io/keycloak/keycloak
#       restart: always
#       hostname: keycloak.quiz.local
#       domainname: quiz.local
#       ports:
#         - "8080:8080"
#       environment:
#         KEYCLOAK_ADMIN: admin
#         KEYCLOAK_ADMIN_PASSWORD: senha1234
#         KC_HEALTH_ENABLED: 'true'
#         KC_METRICS_ENABLED: 'true'
#         KC_DB: postgres
#         KC_DB_URL: jdbc:postgresql://demo_dbPostgressquiz:5432/postgres
#         KC_DB_USERNAME: usuario
#         KC_DB_PASSWORD: senha1234
#         KC_HOSTNAME: localhost
#       depends_on:
#         - demo_dbPostgressquiz
#       command: ["start-dev"]
#       volumes:
#         - ./keycloak-data:/opt/jboss/keycloak/standalone/data
#         - ./keycloak-themes:/opt/jboss/keycloak/themes
#         - ./keycloak-realm:/opt/jboss/keycloak/standalone/configuration/keycloak-realm.json
    
#     # mailserver:
#     #   image: ghcr.io/docker-mailserver/docker-mailserver:latest
#     #   container_name: mailserver
#     #   hostname: mail.quiz.local
#     #   ports:
#     #   - "25:25"
#     #   - "465:465"
#     #   - "587:587"
#     #   - "993:993"
#     #   volumes:
#     #     - ./docker-data/dms/mail-data/:/var/mail/
#     #     - ./docker-data/dms/mail-state/:/var/mail-state/
#     #     - ./docker-data/dms/mail-logs/:/var/log/mail/
#     #     - ./docker-data/dms/config/:/tmp/docker-mailserver/
#     #     - /etc/localtime:/etc/localtime:ro
#     #   environment:
#     #     - ENABLE_RSPAMD=1
#     #     - ENABLE_CLAMAV=1
#     #     - ENABLE_FAIL2BAN=1
#     #   cap_add:
#     #     - NET_ADMIN # For Fail2Ban to work
#     #   restart: always
#     # não funcionou :(
  