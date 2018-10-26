package eyja.lab.tools.control.centre.binaryop;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;

/**
 * The BinaryConverter class provides functionality for serialising and deserialising primitive 
 * values.
 * 
 * @author Planters
 *
 */
public final class BinaryConverter {
	
	/**
	 * The length of the binary representation of a LocalDateTime object.
	 */
	public static final int LOCAL_DATE_TIME_BYTES = Integer.BYTES * 7;
	
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
	 * Convert the specified array of bytes into a local date time.
	 * 
	 * @param binaryLocalDateTime - the binary representation of a local date time
	 * @return the local date time represented by the supplied byte array
	 * @throws NullPointerException if the specified byte array is null
	 * @throws IllegalArgumentException if the length of the specified byte array is different from 
	 * the binary local date time
	 */
	public static LocalDateTime getLocalDateTime(byte[] binaryLocalDateTime) {
		if (binaryLocalDateTime != null) {
			if (binaryLocalDateTime.length == BinaryConverter.LOCAL_DATE_TIME_BYTES) {
				ByteBuffer ldtBuffer = ByteBuffer.wrap(binaryLocalDateTime);
				int year = ldtBuffer.getInt();
				int month = ldtBuffer.getInt();
				int day = ldtBuffer.getInt();
				int hour = ldtBuffer.getInt();
				int minute = ldtBuffer.getInt();
				int second = ldtBuffer.getInt();
				int nano = ldtBuffer.getInt();
				return LocalDateTime.of(year, month, day, hour, minute, second, nano);
			} else {
				throw new IllegalArgumentException(String.format("%s bytes are needed for conversion, "
						+ "but %s are supplied.", Double.BYTES, binaryLocalDateTime.length));
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
	
	/**
	 * Convert the specified local date time to its binary representation.
	 * 
	 * @param dateTime - the local date time to convert into its binary representation
	 * @return the binary representation of the specified local date time
	 */
	public static byte[] toBytes(LocalDateTime dateTime) {
		byte[] binaryDate = new byte[BinaryConverter.LOCAL_DATE_TIME_BYTES];
		ByteBuffer ldtBuffer = ByteBuffer.wrap(binaryDate);
		ldtBuffer.putInt(dateTime.getYear());
		ldtBuffer.putInt(dateTime.getMonthValue());
		ldtBuffer.putInt(dateTime.getDayOfMonth());
		ldtBuffer.putInt(dateTime.getHour());
		ldtBuffer.putInt(dateTime.getMinute());
		ldtBuffer.putInt(dateTime.getSecond());
		ldtBuffer.putInt(dateTime.getNano());
		return binaryDate;
	}
	
	
}
