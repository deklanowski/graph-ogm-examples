package de.jotschi.examples;

import com.syncleus.ferma.AbstractVertexFrame;

public abstract class BaseVertex extends AbstractVertexFrame {

	public String getName() {
		return getProperty("name");
	}

	public void setName(String name) {
		setProperty("name", name);
	}

}
