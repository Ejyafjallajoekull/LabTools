package eyja.lab.tools.control.centre.management;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * The Origin class represents an enclosed resource environment with its own resource IDs 
 * and functional purpose. An Origin can be serialised containing all its resource information.
 * 
 * @author Planters
 *
 */
public class Origin {

	private File path = null;
	private OriginDeserialiser deserialiser = null;
	private OriginSerialiser serialiser = null;
	private static HashMap<Long, Resource> resourceMap = new HashMap<Long, Resource>();

	public Origin(File path, OriginDeserialiser deserialiser) {
		this.path = path;
		this.deserialiser = deserialiser;
	}
	
	public Origin(File path, OriginDeserialiser deserialiser, OriginSerialiser serialiser) {
		this.path = path;
		this.deserialiser = deserialiser;
		this.serialiser = serialiser;
	}
	
	/**
	 * Serialise the origin and all its resources to a file.
	 */
	public void write() {
//		ArrayList<Byte> binaryData = new ArrayList<>

	}
	
	/**
	 * Deserialise the origin and all its resources from a file.
	 * 
	 * @throws IOException if the file this origin represents does not exist
	 * @throws NullPointerException if the deserialiser of this origin is null
	 */
	public void read() throws IOException {
		File readLocation = this.getFile();
		// only allow files that exist
		if (readLocation != null && readLocation.isFile()) {
			byte[] readData = Files.readAllBytes(readLocation.toPath());
			OriginDeserialiser deserial = this.getDeserialiser();
			if (deserial != null) {
				deserial.deserialise(readData, this);
			} else {
				throw new NullPointerException("The Origin needs a deserialiser in order to be read "
						+ "from a file.");
			}
		} else {
			throw new IOException(String.format("The file %s does not exist.", readLocation));
		}
	}
	
	/**
	 * Get the file this origin is written to.
	 * 
	 * @return the file used to store the origin data
	 */
	public File getFile() {
		return this.path;
	}
	
	/**
	 * Get the deserialiser used for rebuilding the origin from a file.
	 * 
	 * @return the deserialiser for loading the origin
	 */
	public OriginDeserialiser getDeserialiser() {
		return this.deserialiser;
	}
	
	/**
	 * Get the serialiser used for writing the origin to a file.
	 * 
	 * @return the serialiser for saving the origin
	 */
	public OriginSerialiser getSerialiser() {
		return this.serialiser;
	}
	
}
