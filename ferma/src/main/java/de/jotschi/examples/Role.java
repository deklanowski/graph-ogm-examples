package de.jotschi.examples;

import java.util.List;

import de.jotschi.examples.perm.Permissions;

public class Role extends BaseVertex {

	public static final String HAS_ROLE = "HAS_ROLE";

	public List<? extends Group> getGroups() {
		return in(HAS_ROLE).has(Group.class).toListExplicit(Group.class);
	}

	public void addPermissions(Product product, Permissions... perms) {

		for (Permissions perm : perms) {
			addFramedEdge(perm.getLabel(), product);
		}
	}

	public boolean hasReadPermission(Product product) {
		return out(Permissions.READ.getLabel()).has(Product.class).hasId(product.getId()).count() > 0;
	}

}
