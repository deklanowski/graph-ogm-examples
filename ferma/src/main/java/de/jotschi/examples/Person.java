package de.jotschi.examples;

import java.util.NoSuchElementException;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.syncleus.ferma.AbstractVertexFrame;
import com.syncleus.ferma.EdgeFrame;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

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

	public Iterable<? extends Person> getFriends() {

		return getVertices("KNOWS", Direction.IN, Person.class);

		// try {
		// return in("KNOWS").toSet(Person.class);
		// } catch (NoSuchElementException e) {
		// return Collections.emptyIterator();
		// }
	}

	private <T> Iterable<T> getVertices(String label, Direction direction, Class<T> classOfT) {
		Iterable<T> facadeIterator = Iterables.transform(getElement().getVertices(direction, label), new Function<Vertex, T>() {
			@Override
			public T apply(Vertex input) {
				//return getGraph().frameElement(input, classOfT);
				return (T)new Person();
			}
		});
		return facadeIterator;
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
