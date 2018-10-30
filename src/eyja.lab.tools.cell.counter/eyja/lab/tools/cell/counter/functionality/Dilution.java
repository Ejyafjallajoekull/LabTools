package eyja.lab.tools.cell.counter.functionality;

import java.time.LocalDateTime;

import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.management.Resource;

/**
 * The Dilution class represents a single sample dilution event.
 * 
 * @author Planters
 *
 */
public class Dilution extends Resource {
	
	private LocalDateTime timeOfDilution = null;
	private double dilutionVolume = -1;
	private double sampleVolume = -1;
	
	/**
	 * Get the sample volume in milliliter used for dilution.
	 * 
	 * @return the sample volume diluted with the dilution volume
	 */
	public double getUsedSampleVolume() {
		return this.sampleVolume;
	}
	
	/**
	 * Get the volume in milliliter used for dilution.
	 * 
	 * @return the volume used to dilute the sample volume
	 */
	public double getDilutionVolume() {
		return this.dilutionVolume;
	}

	/**
	 * Set the sample volume in milliliter used for dilution.
	 * 
	 * @param sampleVolume - the volume diluted with the dilution volume
	 * 
	 * @throws IllegalArgumentException if the sample volume is negative
	 */
	public void setUsedSampleVolume(double sampleVolume) {
		if (sampleVolume >= 0.0d) {
			this.sampleVolume = sampleVolume;
		} else {
			throw new IllegalArgumentException(String.format("The sample volume (%s) can not be "
					+ "negative.", sampleVolume));
		}
	}
	
	/**
	 * Set the volume in milliliter used for dilution.
	 * 
	 * @param dilutionVolume - the volume used to dilute the sample volume
	 * 
	 * @throws IllegalArgumentException if the dilution volume is negative
	 */
	public void setDilutionVolume(double dilutionVolume) {
		if (dilutionVolume >= 0.0d) {
			this.sampleVolume = dilutionVolume;
		} else {
			throw new IllegalArgumentException(String.format("The dilution volume (%s) can not be "
					+ "negative.", dilutionVolume));
		}
	}
	
	/**
	 * Get the total volume of the diluted sample. This is the volume used for dilution and 
	 * the used sample volume.
	 * 
	 * @return the sum of used sample and dilution volume
	 */
	public double getTotalVolume() {
		return this.getUsedSampleVolume() + this.getDilutionVolume();
	}
	
	/**
	 * Set the time and date the dilution has been performed.
	 * 
	 * @param time - the time and date of the dilution event
	 * 
	 * @throws NullPointerException if null is supplied as time of the dilution event
	 */
	public void setTime(LocalDateTime time) {
		if (time != null) {
			this.timeOfDilution = time;
		} else {
			throw new NullPointerException("A dilution event needs a destinct time to have occoured on.");
		}
	}
	
	/**
	 * Get the time and date the cells have been diluted.
	 * 
	 * @return the time of the cell dilution event
	 */
	public LocalDateTime getTime() {
		return this.timeOfDilution;
	}
	
	@Override
	public byte[] serialise() {
		if (this.getID() != null) {
			byte[] serialisation = new byte[Long.BYTES + Double.BYTES * 2 + BinaryConverter.LOCAL_DATE_TIME_BYTES];
			byte[] id = BinaryConverter.toBytes(this.getID().getID());
			byte[] ldt = BinaryConverter.toBytes(this.timeOfDilution);
			byte[] sample = BinaryConverter.toBytes(this.sampleVolume);
			byte[] dilution = BinaryConverter.toBytes(this.dilutionVolume);
			System.arraycopy(id, 0, serialisation, 0, id.length);
			System.arraycopy(ldt, 0, serialisation, id.length, ldt.length);
			System.arraycopy(sample, 0, serialisation, id.length + ldt.length, sample.length);
			System.arraycopy(dilution, 0, serialisation, id.length + ldt.length + sample.length, dilution.length);
			return serialisation;
		} else {
			throw new NullPointerException(String.format("%s cannot be serialised without an resource "
					+ "ID.", this));
		}
	}

	@Override
	public String toString() {
		return String.format("Diluted %s ml of the sample in %s ml at %s.", this.getUsedSampleVolume(), 
				this.getDilutionVolume(), this.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(dilutionVolume);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sampleVolume);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		// time of dilution cannot be null
		result = prime * result + timeOfDilution.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Dilution) {
			final Dilution comp = (Dilution) obj;
			// dilution time cannot be null
			return this.sampleVolume == comp.sampleVolume
					&& this.dilutionVolume == comp.dilutionVolume
					&& this.timeOfDilution.equals(comp.timeOfDilution);
		}
		return false;
	}
	
}