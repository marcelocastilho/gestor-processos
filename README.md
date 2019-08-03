# gestor-processos-judiciais
A project to manage judicial process

# BankSlips Controler
	This REST application provides methods to create, find, and delete Judicial Process.
	This REST application provides methods to create, find, and delete Judicial Process Responsables.

# Rest Methods

###Create a Judicial Process
POST /judicialprocess/

###Get all Judicial Process
GET /judicialprocess/

###Get Specific Judicial Process detail
GET /judicialprocess/{id}

###Delete a Judicial Process
DELETE /judicialprocess/{id}
		
###Create Person
POST /person/

###Get all Person
GET /person/

###Get Specific Person
GET /person/{id}

###Delete a Person
DELETE /person/{id}

###Link person into Judicial Process (add responsable)
POST /judicialprocess/responsable

###Remove person from Judicial Process (remove responsable)
POST /judicialprocess/responsable

##Properties file

###Email server configuration properties is located in src/main/resources/smtp.properties
E-mail send block code is commented 

###Data base configuration properties is located in src/main/resources/application.properties

### Data structure examples
Data examples are located in file src/main/resources/JudicialProcess-soapui-project.xml. It is a SoapUi project that must be imported in SoapUi.

# Utils Links

##GitHub address
	https://github.com/marcelocastilho/gestor-processos-judiciais.git

##Swagger address
	/swagger-ui.html

##H2 Data Base address
	http://localhost:8080/h2

# Running the application on host
	1 - In the application path "gestor-processos-judiciais" run command: "mvn package".
	2 - Create a system variable whith the path to file "gestor-processos-judiciais/src/main/resources/smtp.properties"
		Ex.windows:  SMTP_PROPERTIES_PATH C:\smtp.properties 
		Ex.linux:  SMTP_PROPERTIES_PATH ~/smtp.properties
	2 - In the application path "gestor-processos-judiciais\target" run the command: "java -jar gestor-processos-judiciais-0.0.1-SNAPSHOT.jar"
	3 - Aplication runs at port 8080 !!!

# Running the application on docker ubuntu openjdk8
	1 - Unzip the app file, or download on gitHub
	2 - Get in the projeto folder
	3 - Run command "docker build -t jpm-openjdk8:latest .
	4 - Run commando "docker run -p 8080:8080 jpm-openjdk8:latest"

#Environment Information
JAVA 1.8
MAVEN 3.5

#System variables:


### Requisitos não entregues
	Tests
	Find JudicialProcess between dates
	Find JudicialProcess by responsable(JPQL)

### Melhorias a serem executadas.:	
	Implementar interface para comunicação entre camada exposta e camada de serviço
	Melhorar retorno de erros de banco de dados
	
	
