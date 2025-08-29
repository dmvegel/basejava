FROM eclipse-temurin:23
WORKDIR /opt

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.13/bin/apache-tomcat-10.1.13.tar.gz \
    && tar xzf apache-tomcat-10.1.13.tar.gz \
    && mv apache-tomcat-10.1.13 tomcat

COPY out/artifacts/resumes_war_exploded /opt/tomcat/webapps/ROOT/

EXPOSE 8080

CMD ["/opt/tomcat/bin/catalina.sh", "run"]