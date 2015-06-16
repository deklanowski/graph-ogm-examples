package de.jotschi.examples;

import java.util.List;

public class Group extends BaseVertex {

	public static final String HAS_MEMBER = "HAS_MEMBER";

	public void addMember(User person) {
		addFramedEdge(HAS_MEMBER, person);
	}

	public List<? extends User> getMembers() {
		return out(HAS_MEMBER).has(User.class).toListExplicit(User.class);
	}

	public void addRole(Role role) {
		addFramedEdge(Role.HAS_ROLE, role);
	}

	public List<? extends Role> getRoles() {
		return out(Role.HAS_ROLE).has(Role.class).toListExplicit(Role.class);
	}

}
