package de.jotschi.examples;

import org.jglue.totorom.FramedVertex;

public class Job extends FramedVertex {

	public String getName() {
		return getProperty("name");
	}

	public void setName(String name) {
		setProperty("name", name);
	}
}
