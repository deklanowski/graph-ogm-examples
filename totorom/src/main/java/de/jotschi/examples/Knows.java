package de.jotschi.examples;

import org.jglue.totorom.FramedEdge;

public class Knows extends FramedEdge {

	public Person getKnowingPerson() {
		return inV().next(Person.class);
	}

	public Person getKnownPerson() {
		return outV().next(Person.class);
	}

	public int getSinceYear() {
		Integer since = getProperty("since");
		if(since ==null) {
			return 0;
		} else {
			return since;
		}
	}

	public void setSinceYear(int year) {
		setProperty("since", year);
	}
}
