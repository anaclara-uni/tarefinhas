# ============================================================
#  Imagem Docker da API
#  Etapa 1: compila o projeto com Maven (gera o arquivo .jar)
#  Etapa 2: roda o .jar numa imagem menor, só com o Java
# ============================================================

# --- Etapa 1: compilar ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Baixa as dependências primeiro (fica em cache e acelera as próximas vezes)
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
# Copia o código e gera o .jar
COPY src ./src
RUN mvn -B -q -DskipTests package

# --- Etapa 2: rodar ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
