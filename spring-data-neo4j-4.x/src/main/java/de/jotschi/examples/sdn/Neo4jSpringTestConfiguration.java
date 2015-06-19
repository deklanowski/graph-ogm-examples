package de.jotschi.examples.sdn;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.InProcessServer;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories("de.jotschi.examples.sdn")
@ComponentScan(basePackages = { "de.jotschi.examples.sdn" })
@EnableTransactionManagement
public class Neo4jSpringTestConfiguration extends Neo4jConfiguration {

	@Override
	public SessionFactory getSessionFactory() {
		
		return new SessionFactory("org.neo4j.example.domain");
	}

	@Override
	public Neo4jServer neo4jServer() {
		//For production:
		//return new RemoteServer("http://localhost:7474");
		
		//For testing:
		return new InProcessServer();
	}

}
