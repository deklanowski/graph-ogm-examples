package de.jotschi.examples;

import java.io.IOException;

import org.junit.Test;

public class FramesGraphApp extends AbstractFramesGraphTest {

	@Test
	public void testFrames() throws InterruptedException, IOException {

		User johannes = fg.addVertex(null, User.class);
		johannes.setName("Johannes");

		Job job = fg.addVertex(null, Job.class);
		job.setName("Developer");
		johannes.setJob(job);

		fg.commit();
	}
}
