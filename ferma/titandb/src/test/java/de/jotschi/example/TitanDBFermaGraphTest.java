package de.jotschi.example;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.syncleus.ferma.DelegatingFramedTransactionalGraph;
import com.syncleus.ferma.WrapperFramedTransactionalGraph;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import de.jotschi.example.test.FermaGraphTest;

public class TitanDBFermaGraphTest extends FermaGraphTest<TitanGraph> {

	private Configuration getBerkleyDBConf() {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend", "berkeleyje");
		conf.setProperty("storage.directory", "/tmp/graph");
		return conf;
	}

	private Configuration getInMemoryConf() {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend", "inmemory");
		return conf;
	}

	private Configuration getCassandraConf() {
		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend", "embeddedcassandra");
		conf.setProperty("storage.cassandra-config-dir", "config/cassandra.yaml");
		conf.setProperty("cache.db-cache", "true");
		conf.setProperty("cache.tx-cache-size", "100000");
		conf.setProperty("storage.index.search.backend", "elasticsearch");
		conf.setProperty("storage.index.search.directory", "/tmp/searchindex");
		conf.setProperty("storage.index.search.client-only", "false");
		conf.setProperty("storage.index.search.local-mode", "true");

		return conf;
	}

	public WrapperFramedTransactionalGraph<TitanGraph> setupGraph() {
		System.out.println("Ferma");

		// You may use getCassandraConf() or getInMemoryConf() to switch the backend graph db
		TitanGraph graphDb = TitanFactory.open(getBerkleyDBConf());

		// Add some indices
		graphDb.createKeyIndex("name", Vertex.class);
		graphDb.createKeyIndex("ferma_type", Vertex.class);
		graphDb.createKeyIndex("ferma_type", Edge.class);

		// Setup ferma
		WrapperFramedTransactionalGraph<TitanGraph> fg = new DelegatingFramedTransactionalGraph<>(graphDb, true, false);
		return fg;
	}
}
