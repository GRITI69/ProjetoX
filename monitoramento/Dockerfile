# Usa uma imagem do Java 17 com JRE otimizado
FROM eclipse-temurin:17-jre

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o jar gerado para dentro do container
COPY target/agente-1.0-SNAPSHOT-jar-with-dependencies.jar


# Exponha a porta padrão do Spring Boot
EXPOSE 8081

# Comando que roda o jar
ENTRYPOINT ["java", "-jar", "app.jar"]
