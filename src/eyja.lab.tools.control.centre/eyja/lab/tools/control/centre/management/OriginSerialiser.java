package eyja.lab.tools.control.centre.management;

/**
 * The OriginSerialiser interface represents the functionality to convert an Origin to binary 
 * data. This is mainly used for serialising resources belonging to the origin.
 * 
 * @author Planters
 *
 */
public interface OriginSerialiser {
	
	/**
	 * Serialise an origin into binary data.
	 *
	 * @param origin - the origin to serialise
	 *
	 * @return the binary representation of the origin
	 */
	public byte[] serialise(Origin origin);

}
