FROM gradle:8.13-jdk17 as builder

ENV GRADLE_USER_HOME=/opt/gradle/caches

WORKDIR /opt/app

COPY build.gradle gradlew settings.gradle ./
COPY gradle gradle/

RUN ./gradlew dependencies

COPY . .

# Собираем JAR и удаляем лишние артефакты
RUN ./gradlew clean build -x test && \
    find /opt/app/build/libs/ -name "*-plain.jar" -delete && \
    mv /opt/app/build/libs/*.jar /opt/app/build/libs/app.jar

FROM eclipse-temurin:17-jre-jammy

WORKDIR /opt/app

COPY --from=builder /opt/app/build/libs/app.jar /opt/app/

ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "/opt/app/app.jar"]
