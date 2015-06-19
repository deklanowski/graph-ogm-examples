package de.jotschi.example;

import com.syncleus.ferma.AbstractEdgeFrame;

public class Knows extends AbstractEdgeFrame {

	public User getKnowingPerson() {
		return inV().next(User.class);
	}

	public User getKnownPerson() {
		return outV().next(User.class);
	}

	public int getSinceYear() {
		Integer since = getProperty("since");
		if (since == null) {
			return 0;
		} else {
			return since;
		}
	}

	public void setSinceYear(int year) {
		setProperty("since", year);
	}
}
