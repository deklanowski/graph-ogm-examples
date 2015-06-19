package de.jotschi.examples.sdn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(classes = { Neo4jSpringTestConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SpringDataNeo4jTest {

	@Autowired
	UserRepository repository;

	@Test
	public void testSDN() {
		User johannes = new User();
		johannes.setName("Johannes");
		repository.save(johannes);

		Job job = new Job();
		job.setName("Developer");

		johannes.setJob(job);

		System.out.println("Adding friends");
		long t = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			User user = new User();
			johannes.addFriend(user);
		}
		repository.save(johannes);
		System.out.println("Creation duration: " + (System.currentTimeMillis() - t));

		System.out.println(johannes.getName() + " job: " + johannes.getJob().getName());

		t = System.currentTimeMillis();
		for (User user : johannes.getFriends()) {
			System.out.println(user.getName());
		}
		System.out.println("Read duration: " + (System.currentTimeMillis() - t));
	}
}
