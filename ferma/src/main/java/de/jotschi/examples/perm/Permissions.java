package de.jotschi.examples.perm;

public enum Permissions {
	CREATE("HAS_CREATE_PERMISSION"), READ("HAS_READ_PERMISSION"), UPDATE("HAS_UPDATE_PERMISSION"), DELETE("HAS_DELETE_PERMISSION");

	private String label;

	Permissions(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
