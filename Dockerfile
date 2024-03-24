FROM maven:3.9.4

WORKDIR /app

COPY . .

RUN mvn clean install

EXPOSE 8080

CMD [ "mvn", "spring-boot:run" ]