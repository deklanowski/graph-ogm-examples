package de.jotschi.examples;

import java.util.List;
import java.util.NoSuchElementException;

import org.jglue.totorom.FramedVertex;
import org.jglue.totorom.TEdge;

public class Person extends FramedVertex {

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
			person.addEdge("KNOWS", this, Knows.class);
		}
	}

	public List<Person> getFriends() {
		try {
			return in("KNOWS").toList(Person.class);
		} catch (NoSuchElementException e) {
			return null;
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
			return inE("KNOWS").filter((TEdge edge) -> {
				return person.getId() == edge.outV().next().getId();
			}).next(Knows.class);
		} catch (NoSuchElementException e) {
			System.out.println("No Element Found");
			return null;
		}
	}

	public void addFriend(Person person, int year) {
		Knows knows = person.addEdge("KNOWS", this, Knows.class);
		knows.setSinceYear(year);
	}

}
