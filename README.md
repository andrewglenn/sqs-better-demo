sqs-better-demo
===============

sqs better demo!


Disclaimer: Maven? What? Sure.  This seems to get the job done.  #poc

- git clone https://github.com/andrewglenn/sqs-better-demo.git
- add your aws credentials into BetterSQSDemo.java
- cd sqs-better-demo
- mvn clean
- mnv package
- java -classpath .:aws-java-sdk-1.6.5.jar:target/sqsdemo-1.0-SNAPSHOT.jar:commons-logging-1.1.1.jar:httpcore-4.2.jar:httpclient-4.2.3.jar:commons-lang3-3.1.jar BetterSQSDemo
