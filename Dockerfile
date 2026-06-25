# --- ESTÁGIO 1: COMPILAÇÃO (BUILD) ---
# Usa uma imagem completa com Maven e JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia o pom.xml e faz o download das dependências offline primeiro
# Isso otimiza o tempo de build, aproveitando o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código-fonte e faz o build gerando o .jar
COPY src ./src
RUN mvn clean package -DskipTests

# --- ESTÁGIO 2: EXECUÇÃO (RUN) ---
# Usa uma imagem mais leve só com o JRE 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia SOMENTE o .jar gerado no estágio anterior (builder)
# O pom.xml está configurado para gerar fenix-latest.jar
COPY --from=builder /app/target/*.jar app.jar

# Define o ponto de entrada da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
