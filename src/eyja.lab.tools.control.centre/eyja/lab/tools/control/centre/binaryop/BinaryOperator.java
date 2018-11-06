package eyja.lab.tools.control.centre.binaryop;

/**
 * The BinaryOperator class allows some extended binary operations.
 * 
 * @author Planters
 *
 */
public class BinaryOperator {
	
	/**
	 * Join the specified bytes into a single byte array. The processing order is sequential 
	 * to the argument input order.
	 * 
	 * @param bytes - the bytes to join
	 * @return all bytes joined sequentially into a byte array or null if no arguments are 
	 * given
	 */
	public static byte[] joinBytes(byte[] ... bytes) {
		if (bytes.length > 0) {
			int totalLength = 0;
			for (byte[] b : bytes) {
				if (b != null) {
					totalLength += b.length;
				}
			}
			byte[] joinedBytes = new byte[totalLength];
			int currentInsertionIndex = 0;
			for (byte[] b : bytes) {
				if (b != null) {
					System.arraycopy(b, 0, joinedBytes, currentInsertionIndex, b.length);
					currentInsertionIndex += b.length;
				}
			}
			return joinedBytes;
		} else {
			return null;
		}
	}

}
