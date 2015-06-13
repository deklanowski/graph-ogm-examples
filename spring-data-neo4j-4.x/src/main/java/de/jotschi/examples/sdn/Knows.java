package de.jotschi.examples.sdn;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity
public class Knows extends AbstractPersistable {

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
