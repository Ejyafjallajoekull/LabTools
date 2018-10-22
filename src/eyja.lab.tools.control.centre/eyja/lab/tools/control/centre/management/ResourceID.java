package eyja.lab.tools.control.centre.management;

/**
 * The ResourceID class represents the ID consisting of its origin and its 
 * individual ID.
 * 
 * @author Planters
 *
 */
public class ResourceID {

	private Origin origin = null;
	private long id = 0;
	
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
		return String.format("%s:%s", this.getOrigin(), this.getID());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceID other = (ResourceID) obj;
		if (id != other.id)
			return false;
		if (origin != other.origin) {
			return false;
		}
		return true;
	}

	
}
