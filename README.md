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

### Data examples
Data examples are located in file src/main/resources/JudicialProcess-soapui-project.xml

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

### Requisitos não entregues
	Rodar em docker
	Testes
	Buscar JudicialProcess por data
	Buscar JudicialProcess por envolvido(usar JPQL)

### Melhorias a serem executadas.:	
	Implementar interface de comunicação entre camada exposta e camada de serviço
	
	