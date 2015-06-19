package de.jotschi.examples.sdn;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class User extends AbstractPersistable {

	private String name;

	@Relationship(type = "KNOWS", direction = Relationship.OUTGOING)
	private Set<User> friends = new HashSet<>();

	@Relationship(type = "HAS_JOB", direction = Relationship.OUTGOING)
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
