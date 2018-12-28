package eyja.lab.tools.control.centre.management;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**
 * The Origin class represents an enclosed resource environment with its own resource IDs 
 * and functional purpose. An Origin can be serialised containing all its resource information.
 * 
 * @author Planters
 *
 */
public final class Origin {

	/**
	 * The default file extension for origins.
	 */
	public static final String ORIGIN_EXTENSION = ".ori";
	
	private final File path;
	private final OriginDeserialiser deserialiser;
	private final OriginSerialiser serialiser;
	private final HashMap<Long, Resource> resourceMap = new HashMap<Long, Resource>();
	private long lastId = Long.MIN_VALUE;

	/**
	 * Create a new origin serialised to the specified file with the specified deserailising 
	 * behaviour.
	 * 
	 * @param file - the file this origin is serialised to
	 * @param deserialiser - the deseraliser loading this origin from the specified file
	 */
	public Origin(File file, OriginDeserialiser deserialiser) {
		this.path = file;
		this.deserialiser = deserialiser;
		this.serialiser = null;
	}
	
	/**
	 * Create a new origin serialised to the specified file with the specified serialising and 
	 * deserailising behaviour.
	 * 
	 * @param file - the file this origin is serialised to
	 * @param deserialiser - the deseraliser loading this origin from the specified file
	 * @param serialiser - the serialiser writing this origin to the specified file
	 */
	public Origin(File file, OriginDeserialiser deserialiser, OriginSerialiser serialiser) {
		this.path = file;
		this.deserialiser = deserialiser;
		this.serialiser = serialiser;
	}
	
	/**
	 * Serialise the origin and all its resources to a file.
	 * 
	 * @throws IOException if the specified file could not be written to
	 */
	public void write() throws IOException {
		File writeLocation = this.getFile();
		if (writeLocation != null) {
			// create the folder structure if needed
			File parentFolder = writeLocation.getParentFile();
			if (parentFolder != null && !parentFolder.exists()) {
				parentFolder.mkdirs();
			}
			try (FileOutputStream originData = new FileOutputStream(writeLocation, false)) {
				if (this.getSerialiser() != null) {
					this.getSerialiser().serialise(originData, this);
				} else {
					// Default implementation to serialise all resources.
					for (Resource r : this.getResources()) {
						if (r != null) {
							originData.write(r.serialise());
						}
					}
				}
			}
		} else {
			throw new IOException("No file for writing has been specified.");
		}
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
			OriginDeserialiser deserial = this.getDeserialiser();
			if (deserial != null) {
				try (FileInputStream readData = new FileInputStream(readLocation)) {
					deserial.deserialise(readData, this);
				}
			} else {
				throw new NullPointerException("The origin needs a deserialiser in order to be read "
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
	
	/**
	 * Get all resources managed by this origin.
	 * 
	 * @return an array of all resources contained by this origin
	 */
	public Resource[] getResources() {
		Collection<Resource> resources = this.resourceMap.values();
		return resources.toArray(new Resource[resources.size()]);
	}
	
	/**
	 * Request the specified resource to be added to the origin. This request will only be 
	 * accepted if the resource either has no ID assigned or the origin specifier of the resource's ID 
	 * points to this origin. The ID will be returned after successful addition. In case of failure 
	 * null is returned.
	 * 
	 * @param resource - the resource to add to the origin
	 * @return the resource's ID after successful addition, null if the resource belongs to 
	 * a different origin or is null
	 */
	public ResourceID requestAdd(Resource resource) {
		if (resource != null) {
			ResourceID id = resource.getID();
			// The resource is completely new
			if (id == null) {
				id = new ResourceID(this, this.requestID());
				resource.setID(id);
				this.resourceMap.put(id.getID(), resource);
				return id;
			} else if (id.getOrigin() == this) {
				// TODO: check if ID already existed
				this.resourceMap.put(id.getID(), resource);
				if (id.getID() >= this.lastId) {
					this.lastId = id.getID() + 1l;
				}
				return id;
			}
		}
		return null;
	}
	
	/**
	 * Get the resource with the specified ID from this origin.
	 * 
	 * @param id - the ID of the resource to retrieve
	 * @return the resource with the specified ID or null if no resource with the specified ID 
	 * belongs to this origin
	 */
	public Resource retrieve(long id) {
		return this.resourceMap.get(id);
	}
	
	/**
	 * Get the resource with the specified ID from this origin. Null will be returned if no 
	 * resource with the specified ID belongs to this origin or the specified ID is null.
	 * 
	 * @param id - the ID of the resource to retrieve
	 * @return the resource with the specified ID or null if no resource with the specified ID 
	 * belongs to this origin
	 * @throws IllegalArgumentException if the ID belongs to a different origin
	 */
	public Resource retrieve(ResourceID id) throws IllegalArgumentException {
		if (id != null) {
			if (id.getOrigin() == this) {
				return this.retrieve(id.getID());
			} else {
				throw new IllegalArgumentException(String.format("The ID %s belongs to origin %s "
						+ " and cannot be added to origin %s.", id, id.getOrigin(), this));
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Remove the resource with the specified implicit ID from this origin.
	 * 
	 * @param id - the implicit ID of the resource to remove
	 * @return the resource removed or null if no resource with the specified ID 
	 * belonged to this origin
	 */
	public Resource remove(long id) {
		return this.resourceMap.remove(id);
	}
	
	/**
	 * Remove the resource with the specified explicit ID from this origin.
	 * 
	 * @param id - the explicit ID of the resource to remove
	 * @return the resource removed or null if no resource with the specified ID 
	 * belonged to this origin
	 * 
	 * @throws IllegalArgumentException if the ID belongs to a different origin
	 */
	public Resource remove(ResourceID id) {
		if (id != null) {
			if (id.getOrigin() == this) {
				return this.resourceMap.remove(id.getID());
			} else {
				throw new IllegalArgumentException(String.format("The ID %s belongs to origin %s "
						+ " and cannot be removed from origin %s.", id, id.getOrigin(), this));
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Removes all resources from the origin.
	 */
	public void clear() {
		this.resourceMap.clear();
	}
	
	/**
	 * Request an unique ID from the origin. This ID can be used to reference specific resources.
	 * 
	 * @return a unique ID
	 */
	public Long requestID() {
		return Long.valueOf(this.lastId++);
	}
	
	@Override
	public String toString() {
		return String.format("Origin: %s", this.getFile());
	}

	@Override
	public int hashCode() {
		int result = Long.hashCode(this.lastId);
		final int prime = 31;
		result = prime * result + Objects.hashCode(this.path);
		result = prime * result + Objects.hashCode(this.deserialiser);
		result = prime * result + Objects.hashCode(this.serialiser);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Origin) {
			Origin comp = (Origin) obj;
			return this.lastId == comp.lastId 
					&& Objects.equals(this.path, comp.path) 
					&& Objects.equals(this.deserialiser, comp.deserialiser) 
					&& Objects.equals(this.serialiser, comp.serialiser)
					&& Objects.equals(this.resourceMap, comp.resourceMap);
		}
		return false;
	}
	
}
