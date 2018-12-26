package eyja.lab.tools.control.centre.management;

/**
 * The CachedResource class enables caching for resource references.
 * 
 * @author Planters
 *
 */
public class CachedReference extends ResourceReference {

	private Resource cachedResource = null;
	private boolean isCached = false;
	
	/**
	 * Create a new cached reference to a resource by specifying the file path it originates from and its
	 * ID inside the specified origin. 
	 * Thereby the resource referenced can be cached and must not be dereferenced from 
	 * the according handler for every access.
	 * A physical representation of the reference is needed, so the 
	 * origin may not be null.
	 * 
	 * @param origin - the file path of the origin that contains the resource
	 * @param id - the ID of the resource
	 * 
	 * @throws NullPointerException if the origin is null
	 */
	public CachedReference(String origin, long id) {
		super(origin, id);
	}

	/**
	 * Create a new cached reference to a resource by specifying its unique resource ID.
	 * Thereby the resource referenced can be cached and must not be dereferenced from 
	 * the according handler for every access.
	 * A physical representation of the reference is needed, so the 
	 * origin of the resource ID may not be null.
	 * 
	 * @param id - the ID of the resource
	 * 
	 * @throws NullPointerException if the resource ID is null or the origin specified by the ID is 
	 * invalid
	 */
	public CachedReference(ResourceID id) {
		super(id);
	}
	
	/**
	 * Get the resource referenced by this reference. If the resource has not been cached, it 
	 * will be dereferenced from the specified handler. Otherwise the cached resource is returned.
	 * 
	 * @param fallbackHandler - the handler to dereference the reference from if it has not 
	 * already been cached
	 * 
	 * @return the referenced resource
	 * 
	 * @ throws NullPointerException if the resource is not cached yet and the origin handler 
	 * for dereferencing is null
	 * @throws IllegalArgumentException if the reference origin is not managed by the specified 
	 * handler while caching
	 */
	public Resource getResource(OriginHandler fallbackHandler) {
		if(!this.isCached()) {
			this.cache(fallbackHandler);			
		}
		return this.getCachedResource();
	}
	
	/**
	 * Get the resource referenced by this reference. If the resource has not been cached, it 
	 * will be dereferenced from the specified handler. Otherwise the cached resource is returned.
	 * 
	 * @param <T> - the expected type
	 * @param fallbackHandler - the handler to dereference the reference from if it has not 
	 * already been cached
	 * @param expectedType - the class of the expected type
	 * 
	 * @return the referenced resource
	 * 
	 * @ throws NullPointerException if the resource is not cached yet and the origin handler 
	 * for dereferencing is null
	 * @throws ReferenceException if the type of the resource does not match the type of this
	 * reference after caching
	 * @throws IllegalArgumentException if the reference origin is not managed by the specified 
	 * handler while caching
	 */
	public <T> T getResource(OriginHandler fallbackHandler, Class<T> expectedType) throws ReferenceException {
		if(!this.isCached()) {
			this.cache(fallbackHandler);			
		}
		return this.getCachedResource(expectedType);
	}
	
	/**
	 * Returns if the resource referenced is cached and can be accessed.
	 * 
	 * @return true if the resource referenced is cached
	 */
	public boolean isCached() {
		return this.isCached;
	}
	
	/**
	 * Cache the referenced resource. This will even attempt to cache the resource if it has been 
	 * previously cached. All preexisting caching data will be overwritten.
	 * 
	 * @param referenceHandler - the handler to use for caching
	 * 
	 * @throws NullPointerException if the origin handler 
	 * for dereferencing is null
	 * @throws IllegalArgumentException if the reference origin is not managed by the specified 
	 * handler
	 */
	public void cache(OriginHandler referenceHandler) {
		if (referenceHandler != null) {
			this.cachedResource = referenceHandler.dereference(this);
			this.isCached = true;
		} else {
			throw new NullPointerException(String.format("Cannot cache reference %s "
					+ "from a null handler.", this));
		}
	}
	
	/**
	 * Get the cached resource this reference is pointing to. This will return null 
	 * if no caching has been performed yet.
	 * 
	 * @return the currently cached resource.
	 */
	public Resource getCachedResource() {
		return this.cachedResource;
	}
	
	/**
	 * Get the cached resource this reference is pointing to. This will return null 
	 * if no caching has been performed yet.
	 * @param <T> - the expected type
	 * @param expectedType - the class of the expected type
	 * 
	 * @return the currently cached resource
	 * 
	 * @throws NullPointerException if the expected type is null
	 * @throws ReferenceException if the cached resource is not of the expected type
	 */
	public <T> T getCachedResource(Class<T> expectedType) throws ReferenceException {
		if (expectedType != null) {
			if (this.cachedResource != null) {
				if (expectedType.isInstance(this.cachedResource)) {
					return expectedType.cast(this.cachedResource);
				} else {
					throw new ReferenceException(String.format("The resource %s cached is of type %s, "
							+ "but type %s is expected.", this.cachedResource, 
							this.cachedResource.getClass(), expectedType));
				}
			} else {
				return null;
			}
		} else {
			throw new NullPointerException("The expected type of a reference cannot be null.");
		}
	}
	
	/**
	 * Generates a cached version of the supplied resource reference. If the reference is already 
	 * a cached on it will simply be returned again.
	 * 
	 * @param reference - the reference to create a cached version from
	 * @return the cached version of the specified resource reference or null if the specified reference 
	 * is null
	 */
	public static CachedReference generateCachedReference(ResourceReference reference) {
		if (reference != null) {
			if (reference instanceof CachedReference) {
				return (CachedReference) reference;
			} else {
				return new CachedReference(reference.getOrigin(), reference.getID());
			}
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return String.format("[%s:%s:%s]", this.getOrigin(), this.getID(), this.isCached());
	}
	
}
