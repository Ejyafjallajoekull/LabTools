package eyja.lab.tools.cell.counter.functionality;

import java.time.LocalDateTime;

import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.management.Resource;

/**
 * The Count class represents a single cell count event.
 * 
 * @author Planters
 *
 */
public class Count extends Resource {

	/**
	 * The resource type.
	 */
	public static final CellCountResourceType type = CellCountResourceType.COUNT;
	private LocalDateTime timeOfCounting = null;
	private double countingDensity = -1.0d;
	boolean chamberUsed = false;
	private int chamberCount = -1;
	private int countedSquares = -1;
	private double squareVolume = -1.0d;
	
	/**
	 * Create a new cell counting event with the specified cell density in cells per 
	 * milliliter at the specified date and time.
	 * 
	 * @param density - the density determined in cells / ml
	 * @param time - the time and date of the cell counting event
	 * @throws IllegalArgumentException if the cell density is negative
	 * @throws NullPointerException if null is supplied as time of the counting event
	 */
	public Count(double density, LocalDateTime time) {
		this.setCellDensity(density);
		this.setTime(time);
	}
	
	/**
	 * Create a new cell counting event with the specified cell density in cells per 
	 * milliliter at the specified date and time.
	 * The cell density is determined by supplying the cell number 
	 * counted using a Neubauer Counting Chamber with the specified properties.
	 * The total cell count over all counting squares, the number of counted squares and the volume 
	 * of a single counting square need to be specified.
	 * 
	 * @param chamberCount - the number of cells counted in total
	 * @param countedSquares - the number of squares counted
	 * @param squareVolume - the volume of a single square in mm<sup>3</sup>
	 * @param time - the time and date of the cell counting event
	 * @throws IllegalArgumentException if the cell count is negative, the number of counted squares 
	 * is less then 1 or the square volume is equal to or less than zero
	 * @throws NullPointerException if null is supplied as time of the counting event
	 */
	public Count(int chamberCount, int countedSquares, double squareVolume, LocalDateTime time) {
		this.setCellDensity(chamberCount, countedSquares, squareVolume);
		this.setTime(time);
	}
	
	/**
	 * Set the cell density determined during the counting event in cells per milliliter.
	 * 
	 * @param density - the density determined in cells / ml
	 * 
	 * @throws IllegalArgumentException if the cell density is negative
	 */
	public void setCellDensity(double density) {
		if (density >= 0.0d) {
			this.chamberUsed = false;
			this.countingDensity = density;
		} else {
			throw new IllegalArgumentException(String.format("The cell density (%s) can not be "
					+ "negative.", density));
		}
	}
	
	/**
	 * Set the density determined during the counting event by supplying the cell number 
	 * counted using a Neubauer Counting Chamber with the specified properties.
	 * The total cell count over all counting squares, the number of counted squares and the volume 
	 * of a single counting square need to be specified.
	 * 
	 * @param chamberCount - the number of cells counted in total
	 * @param countedSquares - the number of squares counted
	 * @param squareVolume - the volume of a single square in mm<sup>3</sup>
	 * 
	 * @throws IllegalArgumentException if the cell count is negative, the number of counted squares 
	 * is less then 1 or the square volume is equal to or less than zero
	 */
	public void setCellDensity(int chamberCount, int countedSquares, double squareVolume) {
		if (chamberCount >= 0 && countedSquares > 0 && squareVolume > 0.0d) {
			this.chamberUsed = true;
			this.countingDensity = (double) chamberCount / (countedSquares * squareVolume * 1000);
			this.chamberCount = chamberCount;
			this.countedSquares = countedSquares;
			this.squareVolume = squareVolume;
		} else {
			throw new IllegalArgumentException(String.format("The cell count (%s) can not be "
					+ "negative, at least one square (%s) must be counted and the volume of a single "
					+ "square (%s) must be larger than zero.", chamberCount, countedSquares, squareVolume));
		}
	}
	// dm^3 = l
	// cm^3 = ml
	// mm^3 = µl
	
	/**
	 * Set the time and date the cells have been counted.
	 * 
	 * @param time - the time and date of the cell counting event
	 * 
	 * @throws NullPointerException if null is supplied as time of the counting event
	 */
	public void setTime(LocalDateTime time) {
		if (time != null) {
			this.timeOfCounting = time;
		} else {
			throw new NullPointerException("A counting event needs a destinct time to have occoured on.");
		}
	}
	
	/**
	 * Get the time and date the cells have been counted.
	 * 
	 * @return the time of the cell count event
	 */
	public LocalDateTime getTime() {
		return this.timeOfCounting;
	}
	
	/**
	 * Get the cell density in cells per milliliter determined during this counting event.
	 * 
	 * @return the cell density in cells / ml
	 */
	public double getCellDensity() {
		return this.countingDensity;
	}
	
	@Override
	public byte[] serialise() {
		if (this.getID() != null) {
			byte[] type = BinaryConverter.toBytes(Count.type.ordinal());
			byte[] id = BinaryConverter.toBytes(this.getID().getID());
			byte[] ldt = BinaryConverter.toBytes(this.timeOfCounting);
			byte[] chamber = new byte[] {BinaryConverter.toBytes(this.chamberUsed)};
			byte[] count = null;
			if (this.chamberUsed) {
				byte[] chamberCount = BinaryConverter.toBytes(this.chamberCount);
				byte[] countedSquares = BinaryConverter.toBytes(this.countedSquares);
				byte[] squareVolume = BinaryConverter.toBytes(this.squareVolume);
				count = new byte[chamberCount.length + countedSquares.length + squareVolume.length];
				System.arraycopy(chamberCount, 0, count, 0, chamberCount.length);
				System.arraycopy(countedSquares, 0, count, chamberCount.length, countedSquares.length);
				System.arraycopy(squareVolume, 0, count, 
						chamberCount.length + countedSquares.length, squareVolume.length);
			} else {
				count = BinaryConverter.toBytes(this.countingDensity);
			}
			byte[] serialisation = new byte[type.length + id.length + ldt.length + count.length];
			System.arraycopy(type, 0, serialisation, 0, type.length);
			System.arraycopy(id, 0, serialisation, type.length, id.length);
			System.arraycopy(ldt, 0, serialisation, type.length + id.length, ldt.length);
			System.arraycopy(chamber, 0, serialisation, 
					type.length + id.length + ldt.length, chamber.length);
			System.arraycopy(count, 0, serialisation, 
					type.length + id.length + ldt.length + chamber.length, count.length);
			return serialisation;
		} else {
			throw new NullPointerException(String.format("%s cannot be serialised without an resource "
					+ "ID.", this));
		}
	}
	
	@Override
	public String toString() {
		return String.format("Counted a density of %s / ml at %s.", this.getCellDensity(), 
				this.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(countingDensity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		// time of counting cannot be null
		result = prime * result + timeOfCounting.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Count) {
			final Count comp = (Count) obj;
			// counting time cannot be null
			return this.countingDensity == comp.countingDensity && this.timeOfCounting.equals(comp.timeOfCounting);
		}
		return false;
	}
	
}
