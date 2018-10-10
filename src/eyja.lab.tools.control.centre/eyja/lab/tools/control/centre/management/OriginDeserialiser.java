package eyja.lab.tools.control.centre.management;

/**
 * The OriginDeserialiser interface represents the functionality to build an Origin from binary 
 * data. This is mainly used for deserialising resources belonging to the origin.
 * 
 * @author Planters
 *
 */
public interface OriginDeserialiser {
	
	/**
	 * Deserialise binary data into an origin.
	 * 
	 * @param originData - the binary representation of an origin
	 * @param originToBuild - origin to modify by using the deserialiser
	 */
	public void deserialise(byte[] originData, Origin originToBuild);

}
