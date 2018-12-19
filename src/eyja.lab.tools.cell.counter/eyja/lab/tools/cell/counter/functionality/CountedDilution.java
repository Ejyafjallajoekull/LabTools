package eyja.lab.tools.cell.counter.functionality;

import eyja.lab.tools.cell.counter.core.CellCountInitialiser;
import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.binaryop.BinaryOperator;
import eyja.lab.tools.control.centre.management.CachedReference;
import eyja.lab.tools.control.centre.management.ReferenceException;
import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceReference;

/**
 * The CountedDilution class represents a cell counting event and the subsequent dilution of the 
 * sample.
 * 
 * @author Planters
 *
 */
public class CountedDilution extends Resource {
	
	/**
	 * The resource type.
	 */
	public static final CellCountResourceType type = CellCountResourceType.COUNTED_DILUTION;
	private CachedReference countRef = null;
	private CachedReference dilutionRef = null;
	
	/**
	 * Create a new combined cell count and dilution event. Either of both may be null.
	 * 
	 * @param countingEvent - the cell counting event before dilution
	 * @param dilutionEvent - the dilution event after cell counting
	 */
	public CountedDilution (ResourceReference countingEvent, ResourceReference dilutionEvent) {
		this.countRef = CachedReference.generateCachedReference(countingEvent);
		this.dilutionRef = CachedReference.generateCachedReference(dilutionEvent);
	}
	
	/**
	 * Set the counting event performed before dilution. Only references referencing actual counting 
	 * events are allowed.
	 * 
	 * @param countingEvent - the reference to the counting event
	 */
	public void setCountingEvent(ResourceReference countingEvent) {
		this.countRef = CachedReference.generateCachedReference(countingEvent);
	}
	
	/**
	 * Set the dilution event performed after counting. Only references referencing actual dilution 
	 * events are allowed.
	 * 
	 * @param dilutionEvent - the reference to the dilution event
	 */
	public void setDilutionEvent(ResourceReference dilutionEvent) {
		this.countRef = CachedReference.generateCachedReference(dilutionEvent);
	}
	
	/**
	 * Get the cell count event that occurred before dilution. May be null.
	 * 
	 * @return the cell counting event
	 * @throws ReferenceException if the internally kept resource reference could not be resolved
	 */
	public Count getCountingEvent() throws ReferenceException {
		if (this.countRef != null) {
			return this.countRef.getResource(CellCountInitialiser.getMainHandler(), Count.class);
		} else {
			return null;
		}
	}
	
	/**
	 * Get the dilution event that occurred after cell counting. May be null.
	 * 
	 * @return the dilution event
	 * @throws ReferenceException if the internally kept resource reference could not be resolved
	 */
	public Dilution getDilutionEvent() throws ReferenceException {
		if (this.countRef != null) {
			return this.dilutionRef.getResource(CellCountInitialiser.getMainHandler(), Dilution.class);
		} else {
			return null;
		}
	}
	
	@Override
	public byte[] serialise() {
		if (this.getID() != null) { 
			byte[] type = Count.type.toBytes();
			byte[] id = BinaryConverter.toBytes(this.getID().getID());
			byte[] binaryCount = BinaryConverter.toBytes(this.countRef);
			byte[] lengthCount = BinaryConverter.toBytes(binaryCount.length);
			byte[] binaryDilution = BinaryConverter.toBytes(this.dilutionRef);
			byte[] lengthDilution = BinaryConverter.toBytes(binaryDilution.length);
			return BinaryOperator.joinBytes(type, id, lengthCount, binaryCount, 
					lengthDilution, binaryDilution);
		} else {
			throw new NullPointerException(String.format("%s cannot be serialised without a resource "
					+ "ID.", this));
		}
	}
	
	@Override
	public String toString() {	
		return String.format("[%s:%s:%s]", this.getID(), this.countRef, this.dilutionRef);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1; // independent of ID / superclass
		result = prime * result + ((this.countRef == null) ? 0 : this.countRef.hashCode());
		result = prime * result + ((this.dilutionRef == null) ? 0 : this.dilutionRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// independent of ID / superclass
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		CountedDilution other = (CountedDilution) obj;
		if (this.countRef == null) {
			if (other.countRef != null) {
				return false;
			}
		} else if (!this.countRef.equals(other.countRef)) {
			return false;
		}
		if (this.dilutionRef == null) {
			if (other.dilutionRef != null) {
				return false;
			}
		} else if (!this.dilutionRef.equals(other.dilutionRef)) {
			return false;
		}
		return true;
	}

}
