## myretail-service

myretail-service aggregates product information from Target's RedSky API along with price information from a cassandra data store and return it as JSON to the caller. It also allows updating price for a product that exists in RedSky. 

 ## Tech Stack
   - Java 1.8 
   - SpringBoot
   - Cassandra
   - Google Gauava - for caching product details read from RedSky API
   - Gradle
   - Sonarqube
   - Docker
 
 ## Running the application
  
Step 1: Execute the following command to clone the repo
```
git clone  git@github.com:lekshmynair/myretail-service.git
```           
Step 2: Setup Cassandra environment
          
        If you don't have Cassandra installed, please install it locally.
        Update 'application.yml' to point to the cluster. 
        
        
 
  


  
 
