package de.jotschi.examples;

import java.util.List;
import java.util.NoSuchElementException;

import de.jotschi.examples.perm.Permissions;

public class User extends BaseVertex {

	public static final String KNOWS = "KNOWS";

	public String getFermaType() {
		return getProperty("ferma_type");
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

		//		return getVertices("KNOWS", Direction.IN, Person.class);
		try {
			//return in("KNOWS").toListExplicit(Person.class);
			return v().has(User.class).toListExplicit(User.class);
			//return in("KNOWS").has("ferma_type", Person.class.getName()).toList(Person.class);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	//	private <T> Iterable<T> getVertices(String label, Direction direction, Class<T> classOfT) {
	//		Iterable<T> facadeIterator = Iterables.transform(getElement().getVertices(direction, label), new Function<Vertex, T>() {
	//			@Override
	//			public T apply(Vertex input) {
	//				//				return (T) new Person(getGraph(), input);
	//				boolean frameElement = true;
	//				if (!frameElement) {
	//
	//					T frame;
	//					try {
	//						frame = classOfT.newInstance();
	//						((AbstractElementFrame) frame).init(getGraph(), input);
	//						return frame;
	//
	//					} catch (InstantiationException | IllegalAccessException e) {
	//						// TODO Auto-generated catch block
	//						e.printStackTrace();
	//					}
	//					//				
	//					// 
	//					return null;
	//				} else {
	//					return getGraph().frameElement(input, classOfT);
	//				}
	//			}
	//		});
	//		return facadeIterator;
	//	}

	public Job getJob() {
		try {
			return out(Job.HAS_JOB).next(Job.class);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public Knows getRelationshipTo(User person) {

		try {
			//					return inE("KNOWS").filter((EdgeFrame edge) -> {
			//						return person.getId() == edge.outV().next().getId();
			//					}).next(Knows.class);
			return inE(KNOWS).mark().outV().retain(person).back().next(Knows.class);
		} catch (NoSuchElementException e) {
			System.out.println("No Element Found");
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
		return "Person: " + getName() + " json: " + toJson();
	}

}
