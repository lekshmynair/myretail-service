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
If you don't have Cassandra installed, please install it locally.  Update application.yml to point to the cluster.
Run the following CQL script to create the keyspace and tables.
```
create keyspace myretail with replication = {'class':'SimpleStrategy','replication_factor' : 1};
use myretail;
create table price(
    product_id bigint PRIMARY KEY,
    price_amount double
    );
insert into price (product_id, price_amount) values(15117729,12.99);
insert into price (product_id, price_amount) values(16696652,22.55);
insert into price (product_id, price_amount) values(13860428,65.01);
```       
Step 3: Run the Code

From the project root, run the following command to build the code.
```
Gradle clean build
```
To run the code, execute the following command from the project root:
```
java -Dspring.profiles.active=LOCAL -Djava.security.egd=file:/dev/./urandom -jar build/libs/myretail-service-0.0.1.jar
```
Step 4: Run as a docker container

 
  


  
 
