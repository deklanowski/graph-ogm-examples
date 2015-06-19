package de.jotschi.examples;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.WrappingNeoServerBootstrapper;
import org.neo4j.server.configuration.ServerConfigurator;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.neo4j2.Neo4j2Graph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;

public abstract class AbstractFramesGraphTest {
	protected final static String DB_LOCATION = "target/graphdb";

	public FramedTransactionalGraph<Neo4j2Graph> fg;

	@Before
	public void setup() throws IOException {
		FileUtils.deleteDirectory(new File(DB_LOCATION));
		fg = setupGraph();
	}

	private FramedTransactionalGraph<Neo4j2Graph> setupGraph() {
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

		// (1) Factories should be reused for performance and memory conservation.
		FramedGraphFactory factory = new FramedGraphFactory(new GremlinGroovyModule());

		FramedTransactionalGraph<Neo4j2Graph> framedGraph = factory.create(neo4jBlueprintGraph); // Frame the graph.
		return framedGraph;
	}

}
