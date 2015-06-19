package de.jotschi.example;

import com.syncleus.ferma.DelegatingFramedTransactionalGraph;
import com.syncleus.ferma.WrapperFramedTransactionalGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientTransactionalGraph;

import de.jotschi.example.test.FermaGraphTest;

public class OrientDBFermaGraphTest extends FermaGraphTest<OrientTransactionalGraph> {

	public WrapperFramedTransactionalGraph<OrientTransactionalGraph> setupGraph() {

		System.out.println("Ferma");

		OrientTransactionalGraph memoryGraph = new OrientGraph("memory:tinkerpop");

		// Add some indices
		// memoryGraph.createKeyIndex("name", Vertex.class);
		// memoryGraph.createKeyIndex("ferma_type", Vertex.class);
		// memoryGraph.createKeyIndex("ferma_type", Edge.class);

		// Setup ferma
		WrapperFramedTransactionalGraph<OrientTransactionalGraph> fg = new DelegatingFramedTransactionalGraph<>(memoryGraph, true, false);
		return fg;
	}
}
