package de.jotschi.examples;

import java.io.IOException;

import org.junit.Test;

public class TotoromGraphApp extends AbstractTotoromGraphTest {

	@Test
	public void testTotorom() throws InterruptedException, IOException {

		long t = System.currentTimeMillis();

		// Setup the graph using totorom
		Job job = fg.addVertex(Job.class);
		job.setName("Developer");

		Person peter = fg.addVertex(Person.class);
		peter.setName("Peter");

		Person klaus = fg.addVertex(Person.class);
		klaus.setName("Klaus");

		Person matthias = fg.addVertex(Person.class);
		matthias.setName("Matthias");

		Person johannes = fg.addVertex(Person.class);
		johannes.setName("Johannes");
		johannes.setJob(job);
		johannes.addFriends(klaus, peter);

		for (int i = 0; i < 500; i++) {
			Person p = fg.addVertex(Person.class);
			p.setName("Person_" + i);
			johannes.addFriend(p, i);
		}

		fg.tx().commit();

		long dur = System.currentTimeMillis() - t;
		t = System.currentTimeMillis();
		System.out.println("Create duration:" + dur);

		Knows relationship = johannes.getRelationshipTo(peter);
		relationship.setSinceYear(2001);

		relationship = johannes.getRelationshipTo(klaus);
		relationship.setSinceYear(2002);

		johannes.addFriend(matthias, 1998);
		System.out.println("\n\n\n");

		System.out.println("Name: " + johannes.getName());
		System.out.println("Job: " + johannes.getJob().getName());

		for (Person person : johannes.getFriends()) {
			System.out.println(johannes.getName() + " knows " + person.getName() + " since " + johannes.getRelationshipTo(person).getSinceYear());
			System.out.println(johannes.getName() + " knows " + person.getName());
		}

		dur = System.currentTimeMillis() - t;
		t = System.currentTimeMillis();

		System.out.println("Read duration:" + dur);
		fg.tx().commit();

		// Don't terminate - enable this when you want to inspect the graph on http://localhost:7474
		// System.in.read();
	}
}
