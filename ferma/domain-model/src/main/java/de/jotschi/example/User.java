package de.jotschi.example;

import java.util.List;

import com.syncleus.ferma.traversals.EdgeTraversal;
import com.syncleus.ferma.typeresolvers.PolymorphicTypeResolver;

public class User extends BaseVertex {

	public static final String KNOWS = "KNOWS";

	public String getFermaType() {
		return getProperty(PolymorphicTypeResolver.TYPE_RESOLUTION_KEY);
	}

	public void setJob(Job job) {
		setLinkOut(job, Job.HAS_JOB);
	}

	public void addFriends(User... persons) {
		for (User person : persons) {
			person.addFramedEdge(KNOWS, this, Knows.class);
		}
	}

	public List<? extends User> getFriends() {

		// Instead of in(KNOWS).toList() we use .has(User.class).toListExplicit(User.class)
		// This way the type check is handled via the gremlin pipeline and not within the toList type check logic.
		// When dealing with huge lists this can drastically improve the overall performance for retrieving the list.
		return in(KNOWS).has(User.class).toListExplicit(User.class);

	}

	public User getFirstFriend() {
		return in(KNOWS).nextOrDefault(User.class, null);
	}

	public Job getJob() {
		return out(Job.HAS_JOB).nextOrDefault(Job.class, null);
	}

	public Knows getRelationshipTo(User person) {
		// In many cases it is beneficial to check the pipeline with .hasNext before invoking next. Otherwise you would have to deal with the
		// FastNoSuchElementException. For vertex traversals the nextOrDefault method can be used to avoid manual checking using .hasNext. (see above)
		EdgeTraversal<?, ?, ?> traversal = inE(KNOWS).mark().outV().retain(person).back();
		if (traversal.hasNext()) {
			return traversal.nextExplicit(Knows.class);
		} else {
			return null;
		}
	}

	public void addFriend(User person, int year) {
		Knows knows = person.addFramedEdge(KNOWS, this, Knows.class);
		knows.setSinceYear(year);
	}

	public boolean hasReadPermission(Product product) {
		return in(Group.HAS_MEMBER).out(Role.HAS_ROLE).out(Permissions.READ.getLabel()).has(Product.class).retain(product).count() > 0;
	}

	@Override
	public String toString() {
		// The toJson() method is useful for debugging since it prints all the fields for the given element.
		return "User: " + getName() + " json: " + toJson();
	}

}
