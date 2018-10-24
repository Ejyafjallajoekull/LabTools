package eyja.lab.tools.control.centre.management;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

/**
 * The OriginHandler class allows easy access to all resources of registered origins and allows 
 * dereferencing resource references.
 * 
 * @author Planters
 *
 */
public class OriginHandler {

	private HashMap<String, Origin> originMap = new HashMap<String, Origin>();
	
	/**
	 * Get all origins managed by this handler.
	 * 
	 * @return an array of all origins contained by this handler
	 */
	public Origin[] getOrigins() {
		Collection<Origin> origins = this.originMap.values();
		return origins.toArray(new Origin[origins.size()]);
	}
	
	/**
	 * Request the specified origin to be added to the handler. This request will only be 
	 * accepted if the origin specifies a valid file. If an origin pointing to the same file already 
	 * exists, it will be overwritten.
	 * 
	 * @param origin - the origin to add to the handler
	 * @return true if the origin has successfully been added, false if the origin is invalid
	 */
	public boolean requestAdd(Origin origin) {
		if (origin != null) {
			File file = origin.getFile();
			if (file != null && file.getPath() != null) {
				this.originMap.put(file.getPath(), origin);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Resolve the resource reference to a resource.
	 * 
	 * @param reference - the resource reference to resolve
	 * @return the resource referenced or null if the resource is not contained in the origin 
	 * specified by the reference
	 * @throws NullPointerException if the specified resource reference is null
	 * @throws IllegalArgumentException if the origin specified by the resource reference is not 
	 * managed by this handler
	 */
	public Resource dereference(ResourceReference reference) {
		if (reference != null) {
			Origin refOrigin = this.originMap.get(reference.getOrigin());
			if (refOrigin != null) {
				return refOrigin.retrieve(reference.getID());
			} else {
				throw new IllegalArgumentException(String.format("The origin %s specified by resource "
						+ "reference %s is not handled by %s.",  reference.getOrigin(), reference, this));
			}
		} else {
			throw new NullPointerException("Null cannot be dereferenced.");
		}
	}
	
	/**
	 * Remove the specified origin from this handler.
	 * 
	 * @param origin - the origin to remove
	 */
	public void remove(Origin origin) {
		if (origin != null) {
			File file = origin.getFile();
			if (file != null && file.getPath() != null) {
				this.originMap.remove(file.getPath());
			}
		}
	}
	
	
}
