package eyja.lab.tools.cell.counter.functionality;

/**
 * The CellCountResourceTypes defines all types of resources for the cell count module.
 * 
 * @author Planters
 *
 */
public enum CellCountResourceType {

	COUNT((byte) 0),
	DILUTION((byte) 1),
	COUNTED_DILUTION((byte) 2),
	COUNTING_SERIES((byte) 3);
	
	/**
	 * The number of bytes needed for serialisation of a cell count resource type
	 */
	public static final int BYTES = Byte.BYTES;
	private byte identifier = -1;
	
	private CellCountResourceType(byte identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Infer a binary representation of the according cell count resource type.
	 * 
	 * @return the resource type's binary representation
	 */
	public byte[] toBytes() {
		return new byte[] {this.identifier};
	}
	
	/**
	 * Infer the cell count resource type from binary data.
	 * 
	 * @param data - the binary data identifying the cell count resource type
	 * @return the corresponding resource type
	 */
	public static CellCountResourceType fromByte(byte data) {
		for (CellCountResourceType v : CellCountResourceType.values()) {
			if (v.identifier == data) {
				return v;
			}
		}
		return null;
	}
}
