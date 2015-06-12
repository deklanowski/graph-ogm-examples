package de.jotschi.examples;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.EdgeFrame;

public class Person extends AbstractVertexFrame {

	public void setName(String name) {
		setProperty("name", name);
	}

	public String getName() {
		return getProperty("name");
	}

	public void setJob(Job job) {
		setLinkOut(job, "HAS_JOB");
	}

	public void addFriends(Person... persons) {
		for (Person person : persons) {
			person.addFramedEdge("KNOWS", this, Knows.class);
		}
	}

	public List<? extends Person> getFriends() {
		try {
			return in("KNOWS").toList(Person.class);
		} catch (NoSuchElementException e) {
			return Collections.emptyList();
		}
	}

	public Job getJob() {
		try {
			return out("HAS_JOB").next(Job.class);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public Knows getRelationshipTo(Person person) {
		try {
			// Object obj = inE("KNOWS").as("x").outV().retain(person).back("x");
			// System.out.println(obj.getClass().getName());

			return inE("KNOWS").filter((EdgeFrame edge) -> {
				return person.getId() == edge.outV().next().getId();
			}).next(Knows.class);
		} catch (NoSuchElementException e) {
			System.out.println("No Element Found");
			return null;
		}
	}

	public void addFriend(Person person, int year) {
		Knows knows = person.addFramedEdge("KNOWS", this, Knows.class);
		knows.setSinceYear(year);
	}

}
