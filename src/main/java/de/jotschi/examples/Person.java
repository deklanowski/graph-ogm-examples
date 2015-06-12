package de.jotschi.examples;

import java.util.List;

import org.jglue.totorom.FramedVertex;

import com.tinkerpop.blueprints.Edge;

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
			linkOut(person, "KNOWS");
		}
	}

	public List<Person> getFriends() {
		return out("KNOWS").toList(Person.class);
	}

	public Job getJob() {
		return out("HAS_JOB").next(Job.class);
	}

	public Knows getRelationshipTo(Person person) {
//		outE("KNOWS").forEach(edge -> {
//			edge.setProperty("java_clas", Knows.class.getName());
//		});
		return outE("KNOWS").next(Knows.class);
	}

	public void addFriend(Person person, int year) {
		Knows knows = addEdge("KNOWS", person, Knows.class);
		knows.setSinceYear(year);
	}

}
