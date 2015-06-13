package de.jotschi.examples.sdn;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Job extends AbstractPersistable {

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
