package de.jotschi.examples;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;

public interface User {

	@Property("name")
	public String getName();

	@Property("name")
	public void setName(String string);

	@Adjacency(label = "KNOWS")
	public Iterable<User> getFriends();

	@Adjacency(label = "KNOWS")
	public void addFriend(final User user);

	@Adjacency(label = "HAS_JOB")
	public Job getJob();

	@Adjacency(label = "HAS_JOB")
	public void setJob(Job job);

}
