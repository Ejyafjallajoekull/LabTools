package eyja.lab.tools.control.centre.management;

/**
 * The CachedResource class enables caching for resource references.
 * 
 * @author Planters
 *
 * @param <T> - the type of resource expected
 */
public class CachedReference<T extends Resource> {

	private T cachedResource = null;
	boolean isCached = false;
	private final ResourceReference referenceToResource;
	private final Class<T> type;
	
	/**
	 * Create a new cached reference from the specified resource reference.
	 * Thereby the resource referenced can be cached and must not be dereferenced from 
	 * the according handler for every access.
	 * 
	 * @param reference - the resource reference to cache
	 * @param type - the type of the reference
	 * @throws NullPointerException if the specified reference or type is null
	 */
	public CachedReference(ResourceReference reference, Class<T> type) {
		if (reference != null && type != null) {
			this.referenceToResource = reference;
			this.type = type;
		} else {
			throw new NullPointerException("Null cannot be dereferenced, thereby caching the "
					+ "according resource is impossible.");
		}
	}
	
	/**
	 * Get the resource referenced by this reference. If the resource has not been cached, it 
	 * will be dereferenced from the specified handler. Otherwise the cached resource is returned.
	 * 
	 * @param fallbackHandler - the handler to dereference the reference from if it has not 
	 * already been cached
	 * @return the referenced resource
	 * @ throws NullPointerException if the resource is not cached yet and the origin handler 
	 * for dereferencing is null
	 * @throws ReferenceException if the type of the resource does not match the type of this
	 * reference while caching
	 * @throws IllegalArgumentException if the reference origin is not managed by the specified 
	 * handler while caching
	 */
	public T getResource(OriginHandler fallbackHandler) throws ReferenceException {
		if(!this.isCached()) {
			this.cache(fallbackHandler);			
		}
		return this.getCachedResource();
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
	 * @throws NullPointerException if the origin handler 
	 * for dereferencing is null
	 * @throws ReferenceException if the type of the resource does not match the type of this
	 * reference
	 * @throws IllegalArgumentException if the reference origin is not managed by the specified 
	 * handler
	 */
	@SuppressWarnings("unchecked")
	public void cache(OriginHandler referenceHandler) throws ReferenceException {
		if (referenceHandler != null) {
			try {
				Resource a = referenceHandler.dereference(referenceToResource);
				this.cachedResource = this.convert(a);
				System.out.println(String.format("%s /// %s", a.getClass(), ((T) a).getClass()));

//				this.cachedResource = (T) referenceHandler.dereference(referenceToResource);
				this.isCached = true;
			} catch (ClassCastException e) {
				System.out.println("Trigger");
				throw new ReferenceException(String.format("The resource %s dereferenced from "
						+ " handler %s is not of the expected type.", 
						referenceHandler.dereference(referenceToResource), referenceHandler), e);
			}
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
	public T getCachedResource() {
		return this.cachedResource;
	}
	
	/**
	 * Get the resource reference used for caching.
	 * 
	 * @return the underlying resource reference
	 */
	public ResourceReference getReference() {
		return this.referenceToResource;
	}
	
	@Override
	public String toString() {
		return String.format("[Chached: %s ; %s]", this.isCached(), this.getReference());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.cachedResource == null) ? 0 : this.cachedResource.hashCode());
		result = prime * result + (this.isCached ? 1231 : 1237);
		result = prime * result + this.referenceToResource.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		CachedReference other = (CachedReference) obj;
		if (this.cachedResource == null) {
			if (other.cachedResource != null)
				return false;
		} else if (!this.cachedResource.equals(other.cachedResource))
			return false;
		if (this.isCached != other.isCached)
			return false;
		if (!this.referenceToResource.equals(other.referenceToResource))
			return false;
		return true;
	}
	
}
