package eyja.lab.tools.cell.counter.functionality;

import java.util.ArrayList;
import java.util.logging.Level;

import eyja.lab.tools.cell.counter.core.CellCountInitialiser;
import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.binaryop.BinaryOperator;
import eyja.lab.tools.control.centre.management.CachedReference;
import eyja.lab.tools.control.centre.management.ReferenceException;
import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceReference;
import eyja.lab.tools.control.centre.operation.MainLogger;

/**
 * The CountingSeries class represents a series of sequential, uninterrupted cell counting and 
 * dilution events.
 * 
 * @author Planters
 *
 */
public class CountingSeries extends Resource {
	
	/**
	 * The resource type.
	 */
	public static final CellCountResourceType type = CellCountResourceType.COUNTING_SERIES;
	
	private ArrayList<CachedReference> counts = new ArrayList<CachedReference>();
	
	/**
	 * Add the specified counting and dilution event to the series.
	 * Null cannot be added.
	 * 
	 * @param countedDilution - the event to add
	 * @return true if the addition has been successful, false otherwise
	 */
	public boolean add(ResourceReference countedDilution) {
		if (countedDilution != null) {
			return this.counts.add(CachedReference.generateCachedReference(countedDilution));
		} else {
			return false;
		}
	}
	
	/**
	 * Add the specified counting and dilution event to the series.
	 * Null cannot be added.
	 * 
	 * @param countedDilution - the event to add
	 * @return true if the addition has been successful, false otherwise
	 */
	public boolean add(CountedDilution countedDilution) {
		if (countedDilution != null) {
			return this.counts.add(countedDilution.getCachedReference());
		} else {
			return false;
		}
	}
	
	/**
	 * Remove the specified event from the counting series.
	 * 
	 * @param countedDilution - the reference to the event to remove
	 * @return true if the removal has been successful, false otherwise
	 */
	public boolean remove(ResourceReference countedDilution) {
		return this.counts.remove(CachedReference.generateCachedReference(countedDilution));
	}
	
	/**
	 * Remove the specified event from the counting series.
	 * 
	 * @param countedDilution - the reference to the event to remove
	 * @return true if the removal has been successful, false otherwise
	 */
	public boolean remove(CountedDilution countedDilution) {
		return this.counts.remove(countedDilution.getCachedReference());
	}
	
	/**
	 * Get all the events that are part of this counting series.
	 * 
	 * @return the events of this series
	 */
	public CountedDilution[] getEvents() {
		return this.counts.stream().map(cd -> {
			try {
				return CellCountInitialiser.getMainHandler().dereference(cd, CountedDilution.class);
			} catch (ReferenceException e) {
				MainLogger.getMainLogger().log(Level.SEVERE, String.format("The reference %s could not be "
						+ "dereferenced", cd), e);
				return null;
			}
		}).toArray(CountedDilution[]::new);
	}

	@Override
	public byte[] serialise() {
		if (this.getID() != null) { 
			byte[] type = Count.type.toBytes();
			byte[] id = BinaryConverter.toBytes(this.getID().getID());
			byte[] countSize = BinaryConverter.toBytes(this.counts.size());
			byte[][] binaryCounts = new byte[this.counts.size()][];
			for (int i = 0; i < this.counts.size(); i++) {
				final byte[] c =  BinaryConverter.toBytes(this.counts.get(i));
				// store length for easier deserialisation
				binaryCounts[i] = BinaryOperator.joinBytes(BinaryConverter.toBytes(c.length), c);
			}
			byte[] flatBinaryCounts = BinaryOperator.joinBytes(binaryCounts);
			return BinaryOperator.joinBytes(type, id, countSize, flatBinaryCounts);
		} else {
			throw new NullPointerException(String.format("%s cannot be serialised without a resource "
					+ "ID.", this));
		}
	}	
	
	@Override
	public String toString() {
		return this.counts.toString();
	}
	

	@Override
	public int hashCode() {
		// independent of ID / superclass
		return this.counts.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return this.counts.equals(((CountingSeries) obj).counts);
		}
		return false;
	}

	
}
