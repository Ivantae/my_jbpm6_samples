Create Invoice
=============

This is an example web application embedding jBPM 6 into Java EE6, using EJB3 CMT to manage the transaction. 
Right now the business process has nothing to do with the rewards-basic process. 
It is seller creating invoice, seller checking invoice and buyer accepting invoice. The last two steps are configured as 
automatic task or manual task based on some evaluations.


It was created by forking jsvitak/jbpm-6-examples/rewards-basic which in turn forking the rewards-basic application by Toshiya Kobayashi. Also refer to <jBPM5 Web Application Example> by Shobhit Tyagi.

- Jiri Svitak:		https://github.com/jsvitak/jbpm-6-examples
- Toshiya Kobayashi:	https://github.com/tkobayas/jbpm5example/tree/master/rewards-basic
- Shobhit Tyagi:		https://community.jboss.org/people/roxy1987/blog/2013/01/22/jbpm5-web-application-example


This simple example aims to provide an example usage of:
- Human tasks
- Persistence
- Transactions
- Singleton session manager
- Context dependency injection
- Maven

### Steps to run
- Make sure you have at least Java 6 and Maven 3 installed
- Download somewhere JBoss EAP 6.1 (was tested on JBoss EAP 6.1, other versions should work too)
- Start the application server (default datasource is ExampleDS, the same as in EAP, so the example works out of the box):
 - configure H2 datasource
 	<datasource jta="true" jndi-name="java:jboss/datasources/invoiceDS" pool-name="invoiceDS" enabled="true" use-java-context="true">
        <connection-url>jdbc:h2:mem:invoice;DB_CLOSE_DELAY=-1</connection-url>
        <driver>h2</driver>
        <security>
            <user-name>sa</user-name>
            <password>sa</password>
        </security>
    </datasource>
 - cd <jboss-home>/bin
 - ./standalone.sh

- Build and deploy the example application:
 - cd createInvoice
 - Rename createInvoice.bpmn2 to createInvoice.bpmn2.bak under folder src/main/resources/<bp-paraller> and <bp-sequential> to src/main/resources, and then compile
 - mvn clean package
 - mvn jboss-as:deploy
- Visit http://localhost:8080/invoice/ with a web browser
 - [Start creating invoice Process] is to start a new process clicking "CREATE INVOICE" menu
 - [john's Task] is to list john's tasks and create or update invoice
 - [mary's Task] is to list mary's tasks and approve invoice (it might be automatic)
 - [smith's task] is to list smith's tasks and accept invoice (it might be automatic)
