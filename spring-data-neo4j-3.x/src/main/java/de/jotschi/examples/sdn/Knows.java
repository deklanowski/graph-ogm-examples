package de.jotschi.examples.sdn;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
public class Knows extends AbstractPersistable {

	private static final long serialVersionUID = -3733090621104615743L;

	public int year;

	@StartNode
	public User knownUser;

	@EndNode
	public User knowingUser;

	public User getKnowingUser() {
		return knowingUser;
	}

	public void setKnowingUser(User knowingUser) {
		this.knowingUser = knowingUser;
	}

	public User getKnownUser() {
		return knownUser;
	}

	public void setKnownUser(User knownUser) {
		this.knownUser = knownUser;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
