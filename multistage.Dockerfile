FROM gradle:8.8-jdk21 AS build

WORKDIR /ClinicApp

COPY build.gradle .

COPY settings.gradle .

COPY gradlew .

COPY gradle gradle

COPY src src

RUN gradle bootJar



FROM openjdk:21-jdk

WORKDIR /ClinicApp

COPY --from=build /ClinicApp/build/libs/petClinicBackend-0.0.1-SNAPSHOT.jar /ClinicApp

CMD ["java", "-jar", "/ClinicApp/petClinicBackend-0.0.1-SNAPSHOT.jar"]