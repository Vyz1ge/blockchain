# Этап сборки: Используем образ Maven с Eclipse Temurin OpenJDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
# Копируем pom.xml отдельно для лучшего кэширования слоев Docker
COPY pom.xml .
# Скачиваем все зависимости, чтобы они кэшировались
RUN mvn dependency:go-offline
# Копируем остальной исходный код
COPY src ./src
# Собираем проект в JAR-файл, пропуская тесты
RUN mvn clean package -DskipTests

# Этап выполнения: Используем легковесный образ OpenJDK 17 (с JDK, как в вашем аналоге)
FROM openjdk:17-jdk-slim
WORKDIR /app
# Копируем скомпилированный JAR-файл из этапа 'builder'.
# Имя файла ТОЧНО соответствует artifactId-version из вашего pom.xml.
COPY --from=builder /app/target/BlockchainPchelincevRybakovKiryak-0.0.1-SNAPSHOT.jar app.jar
# Открываем порт 8080
EXPOSE 8080
# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]