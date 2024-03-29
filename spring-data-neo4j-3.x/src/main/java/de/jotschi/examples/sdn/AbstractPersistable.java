package de.jotschi.examples.sdn;

import java.io.Serializable;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * Abstract class for all node entities.
 * 
 * @author jotschi
 *
 */
public abstract class AbstractPersistable implements Serializable {
	private static final long serialVersionUID = -3244769429406745303L;

	/**
	 * The mandatory neo4j graph id for this object.
	 */
	@GraphId
	private Long id;

	/**
	 * Return the id.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Check whether the object was not yet saved.
	 * 
	 * @return true, when the object was not yet saved. Otherwise false.
	 */
	public boolean isNew() {
		return null == getId();
	}

	@Override
	public int hashCode() {
		int hashCode = 17;

		hashCode += isNew() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		AbstractPersistable that = (AbstractPersistable) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
