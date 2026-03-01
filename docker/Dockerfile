FROM eclipse-temurin:17

WORKDIR /app

RUN apt-get update && apt-get install -y wget
RUN wget https://jdbc.postgresql.org/download/postgresql-42.6.0.jar

COPY App.java .

RUN javac -cp postgresql-42.6.0.jar App.java

CMD ["java", "-cp", ".:postgresql-42.6.0.jar", "App"]