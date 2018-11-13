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
	 * Resolve the resource reference to a resource of the expected type. This method will 
	 * fail if the referenced resource is from a different type.
	 * 
	 * @param <T> - the type of resource the reference is expected to point to
	 * @param reference - the resource reference to resolve
	 * @param expectedType - the class of the expected type
	 * 
	 * @return the resource referenced or null if the resource is not contained in the origin 
	 * specified by the reference
	 * 
	 * @throws NullPointerException if the specified resource reference or type is null
	 * @throws IllegalArgumentException if the origin specified by the resource reference is not 
	 * managed by this handler
	 * @throws ReferenceException if the referenced resource is not of the expected type
	 */
	public <T extends Resource> T dereference(ResourceReference reference, Class<T> expectedType) 
	throws ReferenceException {
		if (reference != null && expectedType != null) {
			Origin refOrigin = this.originMap.get(reference.getOrigin());
			if (refOrigin != null) {
				Resource retrievedResource = refOrigin.retrieve(reference.getID());
				if (expectedType.isInstance(retrievedResource)) {
					return expectedType.cast(retrievedResource);
				} else {
					throw new ReferenceException(String.format("The resource %s dereferenced from %s "
							+ "is of type %s while %s was expected.", retrievedResource, reference, 
							retrievedResource.getClass(), expectedType));
				}
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
	
	@Override
	public String toString() {
		return this.originMap.keySet().toString();
	}

	@Override
	public int hashCode() {
		return 31 + originMap.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof OriginHandler) {
			final OriginHandler comp = (OriginHandler) obj;
			if (comp.originMap == null && this.originMap == null) {
				return true;
			} else if (this.originMap != null) {
				return this.originMap.equals(comp.originMap);
			}
		}
		return false;
	}
	
}
