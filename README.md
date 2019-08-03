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

# Running the application
	1 - In the application path "gestor-processos-judiciais" run command mvn package.
	2 - In the application path "gestor-processos-judiciais\target" run the command java -jar gestor-processos-judiciais-0.0.1-SNAPSHOT.jar
	3 - The aplication runa at port 8080

#Environment Information
JAVA 1.8
MAVEN 3.5
Environment variables:
SMTP_PROPERTIES_PATH --> Ex.: C:\smtp.properties or ~/smtp.properties

### Requisitos não entregues
	Tests
	Find JudicialProcess between dates
	Find JudicialProcess by responsable(JPQL)

### Melhorias a serem executadas.:	
	Implementar interface para comunicação entre camada exposta e camada de serviço
	Melhorar retorno de erros de banco de dados
	
	