package eyja.lab.tools.control.centre.binaryop;

import java.nio.ByteBuffer;

/**
 * The BinaryConverter class provides functionality for serialising and deserialising primitive 
 * values.
 * 
 * @author Planters
 *
 */
public final class BinaryConverter {
	
	/**
	 * Convert the specified array of bytes into a long.
	 * 
	 * @param binaryLong - the binary representation of a primitive long
	 * @return the long represented by the supplied byte array
	 * @throws NullPointerException if the specified byte array is null
	 * @throws IllegalArgumentException if the length of the specified byte array is different from 
	 * the binary long
	 */
	public static long getLong(byte[] binaryLong) {
		if (binaryLong != null) {
			if (binaryLong.length == Long.BYTES) {
				return ByteBuffer.wrap(binaryLong).getLong();
			} else {
				throw new IllegalArgumentException(String.format("%s bytes are needed for conversion, "
						+ "but %s are supplied.", Long.BYTES, binaryLong.length));
			}
		} else {
			throw new NullPointerException("Null cannot converted to a primitive value.");
		}
	}
	
	/**
	 * Convert the specified array of bytes into a double.
	 * 
	 * @param binaryDouble - the binary representation of a primitive double
	 * @return the double represented by the supplied byte array
	 * @throws NullPointerException if the specified byte array is null
	 * @throws IllegalArgumentException if the length of the specified byte array is different from 
	 * the binary double
	 */
	public static double getDouble(byte[] binaryDouble) {
		if (binaryDouble != null) {
			if (binaryDouble.length == Double.BYTES) {
				return ByteBuffer.wrap(binaryDouble).getDouble();
			} else {
				throw new IllegalArgumentException(String.format("%s bytes are needed for conversion, "
						+ "but %s are supplied.", Double.BYTES, binaryDouble.length));
			}
		} else {
			throw new NullPointerException("Null cannot converted to a primitive value.");
		}
	}
	
	/**
	 * Convert the specified primitive long to its binary representation.
	 * 
	 * @param l - the long to convert into its binary representation
	 * @return the binary representation of the specified long
	 */
	public static byte[] toBytes(long l) {
		byte[] binaryLong = new byte[Long.BYTES];
		ByteBuffer.wrap(binaryLong).putLong(l);
		return binaryLong;
	}
	
	/**
	 * Convert the specified primitive double to its binary representation.
	 * 
	 * @param d - the double to convert into its binary representation
	 * @return the binary representation of the specified double
	 */
	public static byte[] toBytes(double d) {
		byte[] binaryDouble = new byte[Double.BYTES];
		ByteBuffer.wrap(binaryDouble).putDouble(d);
		return binaryDouble;
	}
	
	
}
