package de.jotschi.example.test;

import static javax.swing.SortOrder.DESCENDING;
import static javax.swing.SortOrder.UNSORTED;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.SortOrder;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.syncleus.ferma.VertexFrame;
import com.syncleus.ferma.WrapperFramedTransactionalGraph;
import com.tinkerpop.blueprints.TransactionalGraph;

import de.jotschi.example.Group;
import de.jotschi.example.InvalidArgumentException;
import de.jotschi.example.Job;
import de.jotschi.example.Knows;
import de.jotschi.example.Page;
import de.jotschi.example.Permissions;
import de.jotschi.example.Product;
import de.jotschi.example.Role;
import de.jotschi.example.User;

public abstract class FermaGraphTest<T extends TransactionalGraph> {

	public WrapperFramedTransactionalGraph<T> fg;

	public abstract WrapperFramedTransactionalGraph<T> setupGraph();

	protected final static String DB_LOCATION = "target/graphdb";

	@Before
	public void setup() throws IOException {
		FileUtils.deleteDirectory(new File(DB_LOCATION));
		fg = setupGraph();

	}

	@After
	public void tearDown() {
		fg.close();
	}

	@Test
	public void testGraph() throws InvalidArgumentException {

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
		klaus.setJob(jobHR);

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
		}
		fg.commit();
		System.out.println("Duration: " + (System.currentTimeMillis() - t));

		for (int i = 0; i < 25; i++) {
			User person = fg.addFramedVertex(User.class);
			person.setName("User HR " + i);
			person.setJob(jobHR);

			User person2 = fg.addFramedVertex(User.class);
			person2.setName("User QA " + i);
			person2.setJob(jobQA);
		}
		fg.commit();

		Knows relationship = johannes.getRelationshipTo(peter);
		relationship.setSinceYear(2001);

		relationship = johannes.getRelationshipTo(klaus);
		relationship.setSinceYear(2002);

		johannes.addFriend(matthias, 1998);

		System.out.println("Name: " + johannes.getName());
		System.out.println("Job: " + johannes.getJob().getName());
		System.out.println(johannes.getName() + " has " + johannes.getFriends().size() + " friends.");
		t = System.currentTimeMillis();

		// Iterate over all friends and print some information
		for (User user : johannes.getFriends()) {
			Knows knows = johannes.getRelationshipTo(user);
			int since = knows == null ? 0 : knows.getSinceYear();
			System.out.println(johannes.getName() + " knows " + user.getName() + " since " + since);
			System.out.println(johannes.getName() + " knows " + user.getName() + " type " + user.getFermaType());
		}

		System.out.println("Duration: " + (System.currentTimeMillis() - t));

		System.out.println("\n\nHR Persons:");
		int pageSize = 2000;
		int page = 1;
		Page<? extends User> personPage = getUsersPerJob(jobHR, "name", DESCENDING, page, pageSize);

		for (User person : personPage) {
			System.out.println(person.getName());
		}

//		int clampedSize = pageSize >= 25 ? 25 : pageSize;
//		assertEquals(25, personPage.getTotalElements());
//		assertEquals(page, personPage.getNumber());
//		assertEquals(clampedSize, personPage.getSize());
//		assertEquals(clampedSize, personPage.getNumberOfElements());

		for (User user : fg.v().toList(User.class)) {
			System.out.println(user.getName() + " " + user.getFermaType());
		}

		fg.commit();
		// Don't terminate
		// System.in.read();
	}

	/**
	 * This method demonstrates a crude way for a paging implementation.
	 * 
	 * @param job
	 * @param sortBy
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws InvalidArgumentException
	 */
	public Page<? extends User> getUsersPerJob(Job job, String sortBy, SortOrder order, int page, int pageSize) throws InvalidArgumentException {

		if (page < 1) {
			throw new InvalidArgumentException("The page must always be positive");
		}
		if (pageSize < 1) {
			throw new InvalidArgumentException("The pageSize must always be positive");
		}

		// Internally we start with page 0
		page = page - 1;

		int low = page * pageSize;
		int upper = low + pageSize - 1;

		long count = job.in("HAS_JOB").count();
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

	@Test
	public void testACLGraph() throws IOException {

		// 1. Setup user, roles and groups
		User adminUser = fg.addFramedVertex(User.class);
		adminUser.setName("Administrator");

		Role role = fg.addFramedVertex(Role.class);
		role.setName("Admin role");

		Role guestRole = fg.addFramedVertex(Role.class);
		guestRole.setName("Guest role");

		Group group = fg.addFramedVertex(Group.class);
		group.setName("Admin group");
		group.addMember(adminUser);
		group.addRole(role);
		group.addRole(guestRole);

		// 2. Add a object to which we want to add permissions
		Product product = fg.addFramedVertex(Product.class);
		product.setName("Awesome Gadget");

		// 3. Add read and create permissions for the given role on the object
		role.addPermissions(product, Permissions.READ, Permissions.CREATE);

		// 4. Determine read permissions on object for all members of one group
		for (User user : group.getMembers()) {
			System.out.println("Person: " + user.getName());
			String perm = user.hasReadPermission(product) ? "true" : "false";
			System.out.println("Has Permission: " + perm);
		}

		// 5. Determine read permissions using a specific role
		for (Role r : group.getRoles()) {
			System.out.println("Role: " + r.getName());
			String perm = r.hasReadPermission(product) ? "true" : "false";
			System.out.println("Has Permission: " + perm);
		}

		fg.commit();
		// System.in.read();

	}

}
