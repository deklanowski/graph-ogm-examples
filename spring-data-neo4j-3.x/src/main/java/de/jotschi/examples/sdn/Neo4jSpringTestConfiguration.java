package de.jotschi.examples.sdn;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories("de.jotschi.examples.sdn")
@EnableTransactionManagement
@ComponentScan(basePackages = { "de.jotschi.examples.sdn" })
public class Neo4jSpringTestConfiguration extends Neo4jConfiguration {

	public static final String DBPATH = "target/graphdb";

	public Neo4jSpringTestConfiguration() {
		setBasePackage("de.jotschi.examples.sdn");
	}

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DBPATH).newGraphDatabase();
	}

}
