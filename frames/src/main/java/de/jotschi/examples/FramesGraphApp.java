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

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;

public class FramesGraphApp {

	protected final static String DB_LOCATION = "target/graphdb";

	public static void main(String[] args) throws IOException, InterruptedException {
		FileUtils.deleteDirectory(new File(DB_LOCATION));
		new FramesGraphApp().start();
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

		FramedGraphFactory factory = new FramedGraphFactory(new GremlinGroovyModule()); // (1) Factories should be reused for performance and memory
																						// conservation.

		FramedGraph framedGraph = factory.create(neo4jBlueprintGraph); // Frame the graph.

		User johannes = framedGraph.addVertex(null, User.class);
		johannes.setName("Johannes");

		Job job = framedGraph.addVertex(null, Job.class);
		job.setName("Developer");
		
		johannes.setJob(job);
	}
}
