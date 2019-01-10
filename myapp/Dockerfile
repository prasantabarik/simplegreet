# == Stage 1: Build and package the application
FROM maven:3.3-jdk-8 AS builder
WORKDIR /usr/src/work

# Workaround for sql server jdbc driver not in maven. See https://coderanch.com/t/630637/build-tools/Maven-dependency-sql-jdbc-driver. Fortunately this layer will be cached.
# Ideally, your own Maven repo should be used for this to
COPY mssqljdbc mssqljdbc
RUN mvn install:install-file -Dfile=mssqljdbc/sqljdbc42.jar \
    -DgroupId=com.microsoft.sqlserver \
    -DartifactId=sqljdbc4 \
    -Dversion=6.0 -Dmaven.repo.local=/usr/src/repo \
    -Dpackaging=jar
# Now copy POM and load dependencies. This will be cached by docker as another reusable layer.
COPY pom.xml .
RUN mvn dependency:go-offline -Dmaven.repo.local=/usr/src/repo
# Finally, copy the rest and call the package goal.
COPY . .
RUN mvn package -Dmaven.repo.local=/usr/src/repo

# == Stage 2: Copy the application into the production image
FROM tomcat:8.5-jre8-alpine
WORKDIR /usr/local/tomcat/webapps
COPY --from=builder /usr/src/work/target/myapi.war .
RUN mv myapi.war ROOT.war && rm -rf ROOT
CMD ["catalina.sh", "run"]