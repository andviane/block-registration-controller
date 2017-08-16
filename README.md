This code is the conceptual user registration module, intended to be used with user registration projects. It accepts the PUT form JSON calls from the registrations server running user front end and stored the received data into internal database. There is also a simple web interface that allows administrator to login and review the registrations. 

The Hibernate-driven database currently supports two tables, contracts and accounts (can be multiple accounts per contract). I also added some fields how came to my mind, but all this is temporary and will be replaced by the real database schema, or (if we cannot get in one jump) will eventually converge into the final database schema over few development iterations.

The system only has administrative interface and it does not have the end user web GUI. It accepts JSON calls instead. The current registration message message it accepts looks like:

{
  "token" : "16BQD541.1IWLQ5KK",
  "title" : "Dr John Doe",
  "description" : "My first registration",
  "address" : "CH-8117, Unterdorfwaeg 11, Fallanden, Switzerland",
  "phone" : "+41762614348",
  "eMail" : "me@myserver.com",
  "ipAddress" : "100.200.300.400"
}

The HTTP method must be PUT. I have used the Yet Another Rest Client (YARC) to send the requests and register few accounts. Almost any development platform should be able to send such request rather easily.

By the architecture, the system is the Spring Boot application that uses Spring Data for database, Spring Security for login and ThymeLeaf template engine to generate the administrative web pages. As you will see, there is actually even not so much code in the project.

Little can be changed in administrative page so far, only the registration can be marked as "reviewed". If required, I can easily add the user registration form as well and get "quick and dirty" complete system.


In the folder containing this README, simply type ./gradlew eclipse (Linux) or gradlew eclipes (Windows) to create project setup that can be imported into Eclipse IDE. Use ./gradlew installDist to package the complete ready to use distribution that can be found in the build folder of bMVC project. Under Windows, you may need to configure some permissions to run the script. Gradle supports few other commands, use ./gradlew tasks to see all.

The server can be started by running the Application class (if from Eclipse) or by the startup script in the distribution folder. Recent Spring containers can be self-hosting, we do not need to deploy manually to Tomcat, while we could build also such type of the application no problem (./gradlew war).

The project is partitioned into three modules:

bDB - database entities. Hibernate will create database schema automatically. 
bAPI - JSON API, Java classes representing responses and requests.
bMVC - the server that handles JSON calls, talks to database and also runs the administrative interface.

The project uses Gradle, Spring Boot, Spring Security, Spring Data and ThymeLeaf

You may see some passwords and tokens in configuration. These are all fictional and cannot be used to connect any particular server. Configuration files are added as part of documentation only; the actual configurations must not be part of the source tree.
 

