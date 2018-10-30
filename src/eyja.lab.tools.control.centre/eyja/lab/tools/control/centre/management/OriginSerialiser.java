package eyja.lab.tools.control.centre.management;

import java.io.IOException;
import java.io.OutputStream;

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
	 * @param originData - the stream to serialise the data to
	 * @param origin - the origin to serialise
	 * @throws IOException - if the serialisation failed
	 */
	public void serialise(OutputStream originData, Origin origin) throws IOException;

}
