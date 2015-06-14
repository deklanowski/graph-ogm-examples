package de.jotschi.examples;

import com.tinkerpop.frames.Property;

public interface Job {

	@Property("name")
	public String getName();

	@Property("name")
	public void setName(String name);
}
