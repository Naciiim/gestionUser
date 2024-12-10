# Étape 1 : Utiliser une image de base pour Java
FROM openjdk:17-jdk-slim

# Étape 2 : Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le fichier JAR dans l'image
COPY target/gestionUser-1.0.0.jar app.jar

# Étape 4 : Exposer le port de l'application
EXPOSE 8080

# Étape 5 : Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
