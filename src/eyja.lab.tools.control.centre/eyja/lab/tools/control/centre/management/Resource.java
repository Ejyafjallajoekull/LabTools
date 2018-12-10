package eyja.lab.tools.control.centre.management;

/**
 * The abstract Resource class defines serialisation and ID mapping of resources.
 * 
 * @author Planters
 *
 */
public abstract class Resource {

	private ResourceID id = null;
	
	/**
	 * Serialise the resource to an array of bytes.
	 * 
	 * @return the byte representation of this resource
	 */
	public abstract byte[] serialise();
	
	/**
	 * 
	 */
//	public abstract dereferenceSelf();
	
	/**
	 * Get the resource's ID unique inside its origin.
	 * 
	 * @return the ID of this resource
	 */
	public ResourceID getID() {
		return this.id;
	}
	
	/**
	 * Set the resource's ID unique inside its origin.
	 * 
	 * @param id - the new ID of this resource
	 */
	public void setID(ResourceID id) {
		this.id = id;
	}
	
	/**
	 * Create a specific reference to this resource, which can be used to identify this resource 
	 * even after serialisation and deserialisation. A reference can only be created if the resource is 
	 * part of a valid origin.
	 * 
	 * @return a reference to this resource
	 * 
	 * @throws NullPointerException if the resource has not been assigned to any origin yet or the 
	 * origin is invalid
	 */
	public ResourceReference getReference() {
		return new ResourceReference(this.getID());
	}
	
	/**
	 * Create a specific, cached reference to this resource, which can be used to identify this resource 
	 * even after serialisation and deserialisation. A reference can only be created if the resource is 
	 * part of a valid origin.
	 * 
	 * @return a cached reference to this resource
	 * 
	 * @throws NullPointerException if the resource has not been assigned to any origin yet or the 
	 * origin is invalid
	 */
	public CachedReference getCachedReference() {
		return new CachedReference(this.getID());
	}

	@Override
	public String toString() {
		ResourceID id = this.getID();
		if (id != null) {
			return id.toString();
		} else {
			return null;
		}
	}
	
	@Override
	public int hashCode() {
		return (this.id == null) ? 0 : this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			Resource comp = ((Resource) obj);
			if (this.id == null && comp.id == null) {
				return true;
			} else if (this.id != null) {
				return this.id.equals(comp.id);
			}
		}
		return false;
	}
	
}
