package eyja.lab.tools.cell.counter.functionality;

import eyja.lab.tools.cell.counter.core.CellCountInitialiser;
import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.binaryop.BinaryOperator;
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
	private ResourceReference countRef = null;
	private ResourceReference dilutionRef = null;
	private Count count = null;
	private Dilution dilution = null;
	
	/**
	 * Create a new combined cell count and dilution event. Either of both may be null.
	 * 
	 * @param countingEvent - the cell counting event before dilution
	 * @param dilutionEvent - the dilution event after cell counting
	 */
	public CountedDilution (ResourceReference countingEvent, ResourceReference dilutionEvent) {
		this.countRef = countingEvent;
		this.dilutionRef = dilutionEvent;
	}
	
	/**
	 * Set the counting event performed before dilution. Only references referencing actual counting 
	 * events are allowed.
	 * 
	 * @param countingEvent - the reference to the counting event
	 */
	public void setCountingEvent(ResourceReference countingEvent) {
		this.countRef = countingEvent;
	}
	
	/**
	 * Set the dilution event performed after counting. Only references referencing actual dilution 
	 * events are allowed.
	 * 
	 * @param dilutionEvent - the reference to the dilution event
	 */
	public void setDilutionEvent(ResourceReference dilutionEvent) {
		this.countRef = dilutionEvent;
	}
	
	/**
	 * Get the cell count event that occurred before dilution. May be null.
	 * 
	 * @return the cell counting event
	 */
	public Count getCountingEvent() {
		if (this.countRef != null) { // only retrieve the resource if the reference indicates one
			if (this.count == null) { // cache the count
				this.count = (Count) CellCountInitialiser.getMainHandler().dereference(this.countRef);
			}
			return this.count;
		} else {
			return null;
		}
	}
	
	/**
	 * Get the dilution event that occurred after cell counting. May be null.
	 * 
	 * @return the dilution event
	 */
	public Dilution getDilutionEvent() {
		if (this.dilutionRef != null) { // only retrieve the resource if the reference indicates one
			if (this.dilution == null) { // cache the count
				this.dilution = (Dilution) CellCountInitialiser.getMainHandler().dereference(this.dilutionRef);
			}
			return this.dilution;
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
		return String.format("[%s:%s:%s]", this.getID(), this.getCountingEvent(), this.getDilutionEvent());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((countRef == null) ? 0 : countRef.hashCode());
		result = prime * result + ((dilutionRef == null) ? 0 : dilutionRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		CountedDilution other = (CountedDilution) obj;
		if (countRef == null) {
			if (other.countRef != null)
				return false;
		} else if (!countRef.equals(other.countRef))
			return false;
		if (dilutionRef == null) {
			if (other.dilutionRef != null)
				return false;
		} else if (!dilutionRef.equals(other.dilutionRef))
			return false;
		return true;
	}

}
