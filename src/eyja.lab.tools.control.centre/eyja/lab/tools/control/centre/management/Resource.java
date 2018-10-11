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
		ResourceID id = this.getID();
		return 31 * 1 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Resource) {
			ResourceID ownID = this.getID();
			ResourceID compID = ((Resource) obj).getID();
			if (ownID == null && compID == null) {
				return true;
			} else if (ownID != null) {
				return ownID.equals(compID);
			}
		}
		return false;
	}
	
}
