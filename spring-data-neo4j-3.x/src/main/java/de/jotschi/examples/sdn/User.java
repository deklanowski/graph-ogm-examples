package de.jotschi.examples.sdn;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class User extends AbstractPersistable {

	@Fetch
	private String name;

	@Fetch
	@RelatedTo(type = "KNOWS", direction = Direction.OUTGOING, elementClass = User.class)
	private Set<User> friends = new HashSet<>();

	@RelatedTo(type = "HAS_JOB", direction = Direction.OUTGOING, elementClass = Job.class)
	private Job job;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void addFriend(User user) {
		friends.add(user);
	}

	public Set<User> getFriends() {
		return friends;
	}
}
