package de.jotschi.example;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.lambdazen.bitsy.BitsyGraph;
import com.syncleus.ferma.DelegatingFramedTransactionalGraph;
import com.syncleus.ferma.WrapperFramedTransactionalGraph;

import de.jotschi.example.test.FermaGraphTest;

public class BitsyFermaGraphTest extends FermaGraphTest<BitsyGraph> {

	public WrapperFramedTransactionalGraph<BitsyGraph> setupGraph() {
		new File(DB_LOCATION).mkdirs();
		Path dbPath = Paths.get(DB_LOCATION);
		BitsyGraph myGraph = new BitsyGraph(dbPath);
		// Setup ferma
		WrapperFramedTransactionalGraph<BitsyGraph> fg = new DelegatingFramedTransactionalGraph<>(myGraph, true, false);
		return fg;
	}
}
