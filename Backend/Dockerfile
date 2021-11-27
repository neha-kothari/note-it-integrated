FROM maven:3.8.2-jdk-8

WORKDIR /maven-app

RUN mkdir books
RUN mkdir noteBooks

COPY . .
RUN mvn clean install

CMD mvn spring-boot:run