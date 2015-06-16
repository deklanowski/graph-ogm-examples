package de.jotschi.examples;

import static javax.swing.SortOrder.DESCENDING;
import static javax.swing.SortOrder.UNSORTED;

import java.util.List;

import javax.swing.SortOrder;

import org.junit.Test;
import org.neo4j.server.rest.repr.InvalidArgumentsException;

import com.syncleus.ferma.VertexFrame;

public class FermaGraphTest extends AbstractFermaGraphTest {

	@Test
	public void testGraph() throws InvalidArgumentsException {

		Job jobDev = fg.addFramedVertex(Job.class);
		jobDev.setName("Developer");

		Job jobCTO = fg.addFramedVertex(Job.class);
		jobCTO.setName("Chief Technology Officer");

		Job jobQA = fg.addFramedVertex(Job.class);
		jobQA.setName("Quality Assurance Engineer");

		Job jobHR = fg.addFramedVertex(Job.class);
		jobHR.setName("Human Resource Management");

		User peter = fg.addFramedVertex(User.class);
		peter.setName("Peter");
		peter.setJob(jobCTO);

		User klaus = fg.addFramedVertex(User.class);
		klaus.setName("Klaus");
		//		klaus.setJob(jobHR);

		User matthias = fg.addFramedVertex(User.class);
		matthias.setName("Matthias");
		matthias.setJob(jobDev);

		User johannes = fg.addFramedVertex(User.class);
		johannes.setName("Johannes");
		johannes.setJob(jobDev);
		johannes.addFriends(klaus, peter);

		long t = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			User person = fg.addFramedVertex(User.class);
			person.setName("User_" + i);
			johannes.addFriend(person, i);
			if (i % 10000 == 0) {
				fg.commit();
			}
		}
		System.out.println("Duration: " + (System.currentTimeMillis() - t));

		//		for (int i = 0; i < 25; i++) {
		//			Person person = fg.addFramedVertex(Person.class);
		//			person.setName("User HR " + i);
		//			person.setJob(jobHR);
		//
		//			Person person2 = fg.addFramedVertex(Person.class);
		//			person2.setName("User QA " + i);
		//			person2.setJob(jobQA);
		//		}
		fg.commit();

		Knows relationship = johannes.getRelationshipTo(peter);
		relationship.setSinceYear(2001);

		relationship = johannes.getRelationshipTo(klaus);
		relationship.setSinceYear(2002);

		johannes.addFriend(matthias, 1998);
		//		System.out.println("\n\n\n");

		//		System.out.println("Name: " + johannes.getName());
		//		System.out.println("Job: " + johannes.getJob().getName());
		//		System.out.println(johannes.getName() + " has " + johannes.getFriends().size() + " friends.");
		t = System.currentTimeMillis();
		for (User person : johannes.getFriends()) {
			//System.out.println(johannes.getName() + " knows " + person.getName() + " since " + johannes.getRelationshipTo(person).getSinceYear());
			System.out.println(johannes.getName() + " knows " + person.getName() + " type " + person.getFermaType());
		}

		System.out.println("Duration: " + (System.currentTimeMillis() - t));

		//		System.out.println("\n\nHR Persons:");
		//		int pageSize = 2000;
		//		int page = 1;
		//		Page<? extends Person> personPage = getPersonsPerJob(jobHR, "name", DESCENDING, page, pageSize);
		//
		//		for (Person person : personPage) {
		//			System.out.println(person.getName());
		//		}
		//
		//		int clampedSize = pageSize >= 25 ? 25 : pageSize;
		//		assertEquals(25, personPage.getTotalElements());
		//		assertEquals(page, personPage.getNumber());
		//		assertEquals(clampedSize, personPage.getSize());
		//		assertEquals(clampedSize, personPage.getNumberOfElements());
		//		
		//		System.out.println("---------------------");
		//		for(Person person : fg.v().toList(Person.class)) { 
		//			System.out.println(person.getName() + " " + person.getFermaType());
		//		}
		//
		//		fg.commit();
		// Don't terminate
		//		System.in.read();
	}

	public Page<? extends User> getPersonsPerJob(Job job, String sortBy, SortOrder order, int page, int pageSize) throws InvalidArgumentsException {

		if (page < 1) {
			throw new InvalidArgumentsException("The page must always be positive");
		}
		if (pageSize < 1) {
			throw new InvalidArgumentsException("The pageSize must always be positive");
		}

		// Internally we start with page 0
		page = page - 1;

		int low = page * pageSize;
		int upper = low + pageSize - 1;
		//		System.out.println("Page: " + page);
		//		System.out.println("Range: " + low + " to " + (low + pageSize));

		long count = job.in("HAS_JOB").count();
		//		System.out.println("Found: " + count);
		List<? extends User> list = job.in("HAS_JOB").order((VertexFrame f1, VertexFrame f2) -> {
			if (order == DESCENDING) {
				VertexFrame tmp = f1;
				f1 = f2;
				f2 = tmp;
			} else if (order == UNSORTED) {
				return 0;
			}

			return f2.getProperty(sortBy).equals(f1.getProperty(sortBy)) ? 1 : 0;

		}).range(low, upper).toList(User.class);

		long totalPages = count / pageSize;

		// Internally the page size was reduced. We need to increment it now that we are finished.
		return new Page<User>(list, count, ++page, totalPages, list.size());

	}

}
