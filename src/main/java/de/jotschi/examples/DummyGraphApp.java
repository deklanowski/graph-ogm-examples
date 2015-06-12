package de.jotschi.examples;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jglue.totorom.FrameFactory;
import org.jglue.totorom.FramedGraph;
import org.jglue.totorom.TypeResolver;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.configuration.ServerConfigurator;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;

public class DummyGraphApp {

	protected final static String DB_LOCATION = "target/graphdb";

	public static void main(String[] args) throws IOException, InterruptedException {
		FileUtils.deleteDirectory(new File(DB_LOCATION));
		new DummyGraphApp().start();
	}

	public void start() throws InterruptedException, IOException {

		GraphDatabaseBuilder builder = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DB_LOCATION);
		GraphDatabaseService graphDatabaseService = builder.newGraphDatabase();
		// Start the neo4j web console - by default it can be accessed using http://localhost:7474. It is handy for development and should not be enabled by
		// default.
		ServerConfigurator webConfig = new ServerConfigurator((GraphDatabaseAPI) graphDatabaseService);
		WrappingNeoServerBootstrapper bootStrapper = new WrappingNeoServerBootstrapper((GraphDatabaseAPI) graphDatabaseService, webConfig);
		bootStrapper.start();

		// Setup neo4j blueprint implementation
		Neo4j2Graph neo4jBlueprintGraph = new Neo4j2Graph(graphDatabaseService);

		// Add some indices
		neo4jBlueprintGraph.createKeyIndex("name", Vertex.class);
		neo4jBlueprintGraph.createKeyIndex("java_class", Vertex.class);
		neo4jBlueprintGraph.createKeyIndex("java_class", Edge.class);

		// Setup totorom
		FramedGraph fg = new FramedGraph(neo4jBlueprintGraph, FrameFactory.Default, TypeResolver.Java);

		// Setup the graph using totorom
		Job job = fg.addVertex(Job.class);
		job.setName("Developer");

		Person peter = fg.addVertex(Person.class);
		peter.setName("Peter");

		Person klaus = fg.addVertex(Person.class);
		klaus.setName("Klaus");

		Person matthias = fg.addVertex(Person.class);
		matthias.setName("Matthias");

		Person johannes = fg.addVertex(Person.class);
		johannes.setName("Johannes");
		johannes.setJob(job);
		johannes.addFriends(klaus, peter);

		Knows relationship = johannes.getRelationshipTo(peter);
		relationship.setSinceYear(2001);

		johannes.addFriend(matthias, 1998);

		System.out.println("\n\n\n");

		// Query the graph
		System.out.println("Name: " + johannes.getName());
		System.out.println("Job: " + johannes.getJob().getName());
		for (Person person : johannes.getFriends()) {
			System.out.println(johannes.getName() + " knows " + person.getName() + " since " + person.getRelationshipTo(person).getSinceYear());
		}
		neo4jBlueprintGraph.commit();

		// Don't terminate
		System.in.read();
	}
}
