package de.jotschi.examples;

import java.io.IOException;

import org.junit.Test;

import de.jotschi.examples.perm.Permissions;

public class ACLGraphTest extends AbstractFermaGraphTest {

	@Test
	public void testACLGraph() throws IOException {

		User adminUser = fg.addFramedVertex(User.class);
		adminUser.setName("Administrator");

		Role role = fg.addFramedVertex(Role.class);
		role.setName("Admin role");

		Role guestRole = fg.addFramedVertex(Role.class);
		guestRole.setName("Guest role");

		Group group = fg.addFramedVertex(Group.class);
		group.setName("Admin group");
		group.addMember(adminUser);
		group.addRole(role);
		group.addRole(guestRole);

		Product product = fg.addFramedVertex(Product.class);
		product.setName("Awesome Gadget");

		role.addPermissions(product, Permissions.READ, Permissions.CREATE);

		System.out.println("--------");

		for (User  user : group.getMembers()) {
			System.out.println("Person: " + user.getName());
			String perm = user.hasReadPermission(product) ? "true" : "false";
			System.out.println("Has Permission: " + perm);
		}

		System.out.println("------");
		for (Role r : group.getRoles()) {
			System.out.println("Role: " + r.getName());
			String perm = r.hasReadPermission(product) ? "true" : "false";
			System.out.println("Has Permission: " + perm);
		}

		fg.commit();
//		System.in.read();

	}
}
