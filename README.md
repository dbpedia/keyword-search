# Keyword Search on DBpedia

Keyword Search is a scalable search engine on structured resources provided by DBpedia. It allows user to enter simple queries (like on Google) and then generate results in response to that queries.

### Features

1. A google user interface like web application.
2. Returns the results in decreasing order of relevancy.

### Installation
1. Install [Glassfish EE7 Full Platform](https://glassfish.java.net/download.html) server.
2. Install [Keyword Search](https://github.com/dbpedia/keyword-search) bundle.
   
     mvn clean install
3. Copy the server properties file from conf folder in the Keyword Search bundle to Glassfish Domain config folder. Normally it is /home/glassfish-4.0/glassfish/domains/domain1/config.
4. Modify the paths in the properties file to where you have kept the dictionaries and resources.

### Deploying the application
1. Use asadmin domain to initialize the Glassfish Server at port 4848. The general fom of the command is:

   as-install/bin/asadmin start-domain
2. Use asadmin deploy to deploy the application to [http://localhost:8080/KeywordSearch-1.0.0](http://localhost:8080/KeywordSearch-1.0.0). The general form of the command is:

   as-install/bin/asadmin deploy KeywordSearch-1.0.0.war
    
  
