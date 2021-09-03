FROM centos

RUN mkdir /opt/tomcat/ 

WORKDIR /opt/tomcat
RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.0/bin/apache-tomcat-8.5.0.tar.gz  
RUN tar xvfz apache*.tar.gz
RUN mv apache-tomcat-8.5.0/* /opt/tomcat/.
RUN yum -y install java
RUN java -version

WORKDIR /opt/tomcat/webapps
ARG WAR_FILE=dist/PSQLConProject.war
COPY ${WAR_FILE} PSQLConProject.war

EXPOSE 8080 

CMD ["/opt/tomcat/bin/catalina.sh", "run"]   
