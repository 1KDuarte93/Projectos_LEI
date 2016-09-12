#!/bin/sh
# - - - - - - - -
export TOMCAT_HOME=/home/kduarte/apache-tomcat-8.0.15
export WEBAPP_NAME=Meeto2
# - - - - - - - -
mkdir -p target/WEB-INF/classes
cd src
javac -cp .:$TOMCAT_HOME/lib/*:../WebContent/WEB-INF/lib/* -d ../target/WEB-INF/classes java/cenas/action/*.java
javac -cp .:$TOMCAT_HOME/lib/*:../WebContent/WEB-INF/lib/* -d ../target/WEB-INF/classes java/cenas/service/*.java
javac -cp .:$TOMCAT_HOME/lib/*:../WebContent/WEB-INF/lib/* -d ../target/WEB-INF/classes java/rmiserver/*.java
javac -cp .:$TOMCAT_HOME/lib/*:../WebContent/WEB-INF/lib/* -d ../target/WEB-INF/classes java/ws/*.java
cd ..
cp -r WebContent/* targets
cp web/*.* target/WEB-INF/classes
cd target
jar cf ../$WEBAPP_NAME.war *
cd ..
# deploy the 'primes.war' archive into Tomcat's webapps:
# cp $WEBAPP_NAME.war $TOMCAT_HOME/webapps