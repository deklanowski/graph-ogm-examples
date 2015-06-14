package de.jotschi.examples;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.configuration.ServerConfigurator;

import com.syncleus.ferma.DelegatingFramedGraph;
import com.syncleus.ferma.FramedGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;

public class FermaGraphApp {

	protected final static String DB_LOCATION = "target/graphdb";

	public static void main(String[] args) throws IOException, InterruptedException {
		FileUtils.deleteDirectory(new File(DB_LOCATION));
		new FermaGraphApp().start();
	}

	public void start() throws InterruptedException, IOException {
		System.out.println("Ferma");
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

		// Setup ferma
		FramedGraph fg = new DelegatingFramedGraph(neo4jBlueprintGraph, true, false);

		long t = System.currentTimeMillis();

		// Setup the graph using ferma
		Job job = fg.addFramedVertex(Job.class);
		job.setName("Developer");

		Person peter = fg.addFramedVertex(Person.class);
		peter.setName("Peter");

		Person klaus = fg.addFramedVertex(Person.class);
		klaus.setName("Klaus");

		Person matthias = fg.addFramedVertex(Person.class);
		matthias.setName("Matthias");

		Person johannes = fg.addFramedVertex(Person.class);
		johannes.setName("Johannes");
		johannes.setJob(job);
		johannes.addFriends(klaus, peter);

		for (int i = 0; i < 5000000; i++) {
			Person p = fg.addFramedVertex(Person.class);
			p.setName("Person_" + i);
			johannes.addFriend(p, i);
			if (i % 10000 == 0) {
				neo4jBlueprintGraph.commit();
			}
		}

		neo4jBlueprintGraph.commit();

		long dur = System.currentTimeMillis() - t;
		t = System.currentTimeMillis();
		System.out.println("Create duration:" + dur);
		//
		// Knows relationship = johannes.getRelationshipTo(peter);
		// relationship.setSinceYear(2001);
		//
		// relationship = johannes.getRelationshipTo(klaus);
		// relationship.setSinceYear(2002);
		//
		// johannes.addFriend(matthias, 1998);
		// System.out.println("\n\n\n");

		// System.out.println("Name: " + johannes.getName());
		// System.out.println("Job: " + johannes.getJob().getName());

		// (Set via Gremlin Untyped) Read duration: 1334,
		// (Set via Gremlin Typed) Read duration: 9731,11334,9987 = 10350
		// (List via Gremlin Typed) Read duration: 9211,9140,9372,8717 = 9110 - -12%
		// Native Blueprint API + Framing: Read duration: 7540,7841,8128 = 7836 - -16%

		for (Person person : johannes.getFriends()) {
			// System.out.println(johannes.getName() + " knows " + person.getName() + " since " + johannes.getRelationshipTo(person).getSinceYear());
			// System.out.println(johannes.getName() + " knows " + person.getName());
		}

		dur = System.currentTimeMillis() - t;
		t = System.currentTimeMillis();

		System.out.println("Read duration:" + dur);
		neo4jBlueprintGraph.commit();

		// Don't terminate
		System.in.read();
	}
}
