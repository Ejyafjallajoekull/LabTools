package eyja.lab.tools.control.centre.management;

import java.io.File;

/**
 * The ResourceReference class references a resource based on the file path of the 
 * origin it originates from and its unique origin specific ID.
 * 
 * @author Planters
 *
 */
public class ResourceReference {

	private final String origin;
	private final long id;
	
	/**
	 * Create a new reference to a resource by specifying the file path it originates from and its
	 * ID inside the specified origin. A physical representation of the reference is needed, so the 
	 * origin may not be null.
	 * 
	 * @param origin - the file path of the origin that contains the resource
	 * @param id - the ID of the resource
	 * 
	 * @throws NullPointerException if the origin is null
	 */
	public ResourceReference(String origin, long id) {
		if (origin != null) {
			this.id = id;
			this.origin = origin;
		} else {
			throw new NullPointerException("An origin without a physical representation cannot be "
					+ "referenced.");
		}
	}
	
	/**
	 * Create a new reference to a resource by specifying its unique resource ID.
	 * A physical representation of the reference is needed, so the 
	 * origin of the resource ID may not be null.
	 * 
	 * @param id - the ID of the resource
	 * 
	 * @throws NullPointerException if the resource ID is null or the origin specified by the ID is 
	 * invalid
	 */
	public ResourceReference(ResourceID id) {
		if (id != null) {
			Origin idOrigin = id.getOrigin();
			if (idOrigin != null) {
				File f = idOrigin.getFile();
				if (f != null) {
					String originPath = f.getPath();
					if (originPath != null) {
						this.id = id.getID();
						this.origin = originPath;
					} else {
						throw new NullPointerException("An origin without a physical representation cannot be "
								+ "referenced.");
					}
				} else {
					throw new NullPointerException(String.format("A valid origin is needed to create a valid "
							+ "reference, but the origin %s specified by the resource ID %s is not.", 
							idOrigin, id));
				}
			} else {
				throw new NullPointerException(String.format("An origin is needed to create a valid "
						+ "reference, but none is specified by the resource ID %s.", id));
			}
		} else {
			throw new NullPointerException("An origin without a physical representation cannot be "
					+ "referenced.");
		}
	}
	
	/**
	 * Set the origin of the resource as path to its physical representation. A physical representation 
	 * of a reference is needed, so the origin needs to be represented by a valid file.
	 * 
	 * @param origin - the path of the origin
	 * 
	 * @throws NullPointerException if the path of the origin is null
	 */
//	private void setOrigin(String origin) {
//		if (origin != null) {
//			this.origin = origin;
//		} else {
//			throw new NullPointerException("An origin without a physical representation cannot be "
//					+ "referenced.");
//		}
//	}
	
	/**
	 * Get the file path of the origin containing the resource.
	 * 
	 * @return the file path of the origin
	 */
	public String getOrigin() {
		return this.origin;
	}
	
	/**
	 * Get the origin specific ID of the resource.
	 * 
	 * @return the origin specific ID of the referenced resource
	 */
	public long getID() {
		return this.id;
	}
	
	@Override
	public String toString() {
		return String.format("[%s:%s]", this.getOrigin(), this.getID());
	}

	@Override
	public final int hashCode() {
		return 31 * Long.hashCode(this.id) + this.origin.hashCode();
	}

	@Override
	public final boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof ResourceReference) {
			ResourceReference comp = (ResourceReference) obj;
			return this.id == comp.id && this.origin.equals(comp.origin);
		}
		return false;
	}
	
}
