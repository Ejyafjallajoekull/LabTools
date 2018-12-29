package eyja.lab.tools.control.centre.operation;

import java.util.Objects;

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
		int result = Objects.hashCode(this.name);
		result = prime * result + Objects.hashCode(this.description);
		result = prime * result + Objects.hashCode(this.version);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof InitialiserDescriptor) {
			InitialiserDescriptor comp = (InitialiserDescriptor) obj;
			return Objects.equals(this.name, comp.name) 
					&& Objects.equals(this.version, comp.version) 
					&& Objects.equals(this.description, comp.description);
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("[%s-%s: %s]", this.name, this.version, this.description);
	}

}
