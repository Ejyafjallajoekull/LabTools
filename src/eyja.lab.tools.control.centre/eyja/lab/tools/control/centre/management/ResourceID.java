package eyja.lab.tools.control.centre.management;

import java.util.Objects;

/**
 * The ResourceID class represents the ID consisting of its origin and its 
 * individual ID.
 * 
 * @author Planters
 *
 */
public final class ResourceID {

	private final Origin origin;
	private final long id;
	
	/**
	 * Create a new resource ID compromised out of an origin and a origin unique ID.
	 * 
	 * @param origin - the origin this ID belongs to
	 * @param id - the origin unique identifier
	 */
	public ResourceID(Origin origin, long id) {
		this.origin = origin;
		this.id = id;
	}
	
	/**
	 * Get the origin the ID belongs to.
	 * 
	 * @return the origin containing the ID
	 */
	public Origin getOrigin() {
		return this.origin;
	}
	
	/**
	 * Get the origin unique ID of this resource.
	 * 
	 * @return the resource's ID
	 */
	public long getID() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return String.format("[%s:%s]", this.getOrigin(), this.getID());
	}

	@Override
	public int hashCode() {
		return Long.hashCode(this.id) * 31 + Objects.hashCode(this.origin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof ResourceID) {
			ResourceID comp = (ResourceID) obj;
			return Objects.equals(this.origin, comp.origin) && this.id == comp.id;
		}
		return false;
	}

	
}
