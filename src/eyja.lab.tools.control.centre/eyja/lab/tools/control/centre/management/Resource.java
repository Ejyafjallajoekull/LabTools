package eyja.lab.tools.control.centre.management;

/**
 * The Resource interface defines serialisation and ID mapping of resources.
 * 
 * @author Planters
 *
 */
public interface Resource {
	
	/**
	 * Serialise the resource to an array of bytes.
	 * 
	 * @return the byte representation of this resource
	 */
	public byte[] serialise();
	
	/**
	 * Get the resource's ID unique inside its origin.
	 * 
	 * @return the ID of this resource
	 */
	public Long getID();

}
