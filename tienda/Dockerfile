# --- ETAPA 1: Compilar la aplicación ---
# Usamos una imagen oficial de Maven con Java 21 para compilar el código fuente
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el archivo de configuración de dependencias
COPY pom.xml .

# Copiamos todo el código fuente y recursos del proyecto
COPY src ./src

# Compilamos el proyecto y generamos el archivo .jar saltándonos los tests para agilizar
RUN mvn clean package -DskipTests

# --- ETAPA 2: Entorno de ejecución de la App ---
# Usamos una imagen de Java 21 optimizada y liviana para correr la app en producción
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copiamos el archivo .jar generado en la etapa anterior y lo renombramos a app.jar
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto estándar en el que corre Spring Boot
EXPOSE 8080

# Comando para arrancar la aplicación de Spring Boot
# Modifica únicamente la última línea para activar el perfil de producción
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]