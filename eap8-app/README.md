# Sample EAP application using external PostreSQL database.

To run, first start a postgresql database using docker-compose

`docker-compose up`

set EAP_HOME to the location of your EAP installation

mkdir -p $EAP_HOME/modules/org/postgresql/main

Download the postgres jdbc driver

wget https://jdbc.postgresql.org/download/postgresql-42.7.0.jar -P $EAP_HOME/modules/org/postgresql/main

cat << EOF > $EAP_HOME/modules/org/postgresql/main/module.xml
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">
  <resources>
    <resource-root path="postgresql-42.7.0.jar"/>
  </resources>
  <dependencies>
    <module name="javaee.api"/>
    <module name="sun.jdk"/>
    <module name="ibm.jdk"/>
    <module name="javax.api"/>
    <module name="javax.transaction.api"/>
  </dependencies>
</module>
EOF

Run wildfly, from the EAP_HOME folder, run:

`./bin/standalone.sh`


Add the driver and datasource, from the EAP_HOME folder, run:


```

$EAP_HOME/bin/jboss-cli.sh --connect

/subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql)

data-source add --name=postgresql --jndi-name=java:/jdbc/postgresql --driver-name=postgresql --connection-url=jdbc:postgresql://127.0.0.1:5432/postgresDB --user-name=postgresUser --password=postgresPW
```

deploy the application, from the repo location run:


`mvn clean install wildfly:deploy`

curl -X POST http://localhost:8080/subscribers -H 'Content-Type: application/json' -d '{"id": 0}'