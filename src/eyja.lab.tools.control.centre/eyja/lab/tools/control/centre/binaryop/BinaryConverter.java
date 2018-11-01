package eyja.lab.tools.control.centre.binaryop;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

import eyja.lab.tools.control.centre.management.ResourceReference;

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
	
	private static final byte BOOLEAN_FALSE = 0;
	private static final byte BOOLEAN_TRUE = 1;
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	
	/**
	 * Convert the specified byte into a boolean.
	 * 
	 * @param binaryBoolean - the binary representation of a primitive boolean
	 * @return the boolean represented by the supplied byte
	 */
	public static boolean getBoolean(byte binaryBoolean) {
		return binaryBoolean != BinaryConverter.BOOLEAN_FALSE;
	}
	
	/**
	 * Convert the specified array of bytes into an int.
	 * 
	 * @param binaryInt - the binary representation of a primitive int
	 * @return the int represented by the supplied byte array
	 * @throws NullPointerException if the specified byte array is null
	 * @throws IllegalArgumentException if the length of the specified byte array is different from 
	 * the binary int
	 */
	public static int getInt(byte[] binaryInt) {
		if (binaryInt != null) {
			if (binaryInt.length == Integer.BYTES) {
				return ByteBuffer.wrap(binaryInt).getInt();
			} else {
				throw new IllegalArgumentException(String.format("%s bytes are needed for conversion, "
						+ "but %s are supplied.", Integer.BYTES, binaryInt.length));
			}
		} else {
			throw new NullPointerException("Null cannot converted to a primitive value.");
		}
	}
	
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
	 * Convert the specified array of bytes into a resource reference. May be null.
	 * 
	 * @param binaryReference - the binary representation of a resource reference
	 * @return the resource reference represented by the supplied byte array
	 * @throws NullPointerException if the specified byte array is null
	 */
	public static ResourceReference getResourceReference(byte[] binaryReference) {
		if (binaryReference != null) {
			ByteBuffer refBuffer = ByteBuffer.wrap(binaryReference);
			try {
				int lengthOrigin = refBuffer.getInt();
				if (lengthOrigin >= 0) {
					byte[] binaryOrigin = new byte[lengthOrigin];
					refBuffer.get(binaryOrigin);
					String origin = new String(binaryOrigin, BinaryConverter.DEFAULT_CHARSET);
					long id = refBuffer.getLong();
					return new ResourceReference(origin, id);
				} else {
					return null;
				}
			} catch (BufferUnderflowException e) {
				throw new IllegalArgumentException(String.format("%s cannot be resolved to a resource "
						+ "reference.", Arrays.toString(binaryReference)), e);
			}
		} else {
			throw new NullPointerException("Null cannot converted to a primitive value.");
		}
	}
	
	/**
	 * Convert the specified primitive boolean to its binary representation.
	 * 
	 * @param b - the boolean to convert into its binary representation
	 * @return the binary representation of the specified boolean
	 */
	public static byte toBytes(boolean b) {
		if (b) {
			return BinaryConverter.BOOLEAN_TRUE;
		} else {
			return BinaryConverter.BOOLEAN_FALSE;
		}
	}
	
	/**
	 * Convert the specified primitive int to its binary representation.
	 * 
	 * @param i - the int to convert into its binary representation
	 * @return the binary representation of the specified int
	 */
	public static byte[] toBytes(int i) {
		byte[] binaryInt = new byte[Integer.BYTES];
		ByteBuffer.wrap(binaryInt).putInt(i);
		return binaryInt;
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
	
	/**
	 * Convert the specified resource reference to its binary representation. Will return the binary 
	 * integer -1 if null is passed instead of the length of the referenced origin.
	 * 
	 * @param reference - the resource reference to convert into its binary representation
	 * @return the binary representation of the specified resource reference or the integer -1
	 */
	public static byte[] toBytes(ResourceReference reference) {
		if (reference != null) {
			byte[] origin = reference.getOrigin().getBytes(BinaryConverter.DEFAULT_CHARSET);
			byte[] length = BinaryConverter.toBytes(origin.length);
			byte[] id = BinaryConverter.toBytes(reference.getID());
			byte[] binaryRef = new byte[length.length + origin.length + id.length];
			System.arraycopy(length, 0, binaryRef, 0, length.length);
			System.arraycopy(origin, 0, binaryRef, length.length, origin.length);
			System.arraycopy(id, 0, binaryRef, length.length + origin.length, id.length);
			return binaryRef;
		} else {
			return BinaryConverter.toBytes(-1);
		}
	}
	
}
