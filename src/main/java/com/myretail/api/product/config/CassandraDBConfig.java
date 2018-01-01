package com.myretail.api.product.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * 
 * @author lekshmynair
 * Cassandra Configuration class 
 */

@Configuration
public class CassandraDBConfig {	
	
	private static Logger log = LoggerFactory.getLogger(CassandraDBConfig.class); 
	 
	@Value("${cassandra.node}")
	private String node;
	
	@Value("${cassandra.port}")
	private int port;
	
	@Value("${cassandra.database-name}")
	private String dbName;
	
	@Bean
	public Session cassandraSession() {
		log.info("node = " + node + ", port = " + port + ", dbName = " + dbName);
		Cluster cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
		Session session = cluster.connect(dbName);
		return session;
	}
	
	
}
