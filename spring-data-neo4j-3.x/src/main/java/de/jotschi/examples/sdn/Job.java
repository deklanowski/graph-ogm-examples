package de.jotschi.examples.sdn;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Job extends AbstractPersistable {

	private static final long serialVersionUID = 800399484079553142L;

	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
