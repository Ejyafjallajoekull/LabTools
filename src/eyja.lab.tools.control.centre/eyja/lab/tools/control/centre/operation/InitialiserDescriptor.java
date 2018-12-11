package eyja.lab.tools.control.centre.operation;

/**
 * The InitialiserDesciptor class describes an initialiser and its plugin functionality.
 * 
 * @author Planters
 *
 */
public final class InitialiserDescriptor {
	
	private final String name;
	private final String description;
	private final String version;
	
	/**
	 * Create a new descriptor for an initialiser, specifying its plugin name, version and a 
	 * description of its function. All values may be null, however the plugin name is supposed to 
	 * be unique for every provided plugin module.
	 * 
	 * @param name - the plugin name of the initialiser
	 * @param description - the plugin description of the initialiser
	 * @param version - the plugin version of the initialiser
	 */
	public InitialiserDescriptor(String name, String description, String version) {
		this.name = name;
		this.description = description;
		this.version = version;
	}
	
	/**
	 * Get the plugin name of the initialiser. 
	 * This may be null and is supposed to be a unique identifier.
	 * 
	 * @return the plugin name of the initialiser
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the plugin description of the initialiser. 
	 * This may be null.
	 * 
	 * @return the plugin description of the initialiser
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Get the plugin version of the initialiser. 
	 * This may be null.
	 * 
	 * @return the plugin version of the initialiser
	 */
	public String getVersion() {
		return this.version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		InitialiserDescriptor other = (InitialiserDescriptor) obj;
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.version == null) {
			if (other.version != null) {
				return false;
			}
		} else if (!this.version.equals(other.version)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("[%s-%s: %s]", this.name, this.version, this.description);
	}

}
