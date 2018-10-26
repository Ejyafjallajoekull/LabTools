package eyja.lab.tools.control.centre.test.binaryop;

import java.util.Arrays;

import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The BinaryConverterTesting class test the BinaryConverter class for correct functionality.
 * 
 * @author Planters
 *
 */
public class BinaryConverterTesting implements TestSubject {

	@Override
	public void runAllTests() throws TestFailureException {
		BinaryConverterTesting.testLongConversion();
		BinaryConverterTesting.testDoubleConversion();
	}
	
	/**
	 * Test the binary conversion of longs.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testLongConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal long conversion
				long testLong = TestRunnerWrapper.RANDOM.nextLong();
				byte[] binaryRep = BinaryConverter.toBytes(testLong);
				long convertedLong = BinaryConverter.getLong(binaryRep);
				TestSubject.assertTestCondition(testLong == convertedLong, 
						String.format("The long %s has been converted to %s and reconverted to %s.", 
								testLong, binaryRep, convertedLong));
			} { // test invalid byte arrays
				byte[] randomBytes = new byte[TestRunnerWrapper.RANDOM.nextInt(3000)];
				TestRunnerWrapper.RANDOM.nextBytes(randomBytes);
				if (randomBytes.length != Long.BYTES) {
					try {
						long convertedLong = BinaryConverter.getLong(randomBytes);
						throw new TestFailureException(String.format("The conversion of %s to a long should "
								+ "fail, but was converted into %s.", Arrays.toString(randomBytes), 
								convertedLong));
					} catch (IllegalArgumentException e) {
						// Do nothing as this is expected behaviour.
					}
				} else {
					// This should be a normal conversion, so do nothing.
					BinaryConverter.getLong(randomBytes);
				}
			}
		} { // test null
			try {
				long convertedLong = BinaryConverter.getLong(null);
				throw new TestFailureException(String.format("The conversion of %s to a long should "
						+ "fail, but was converted into %s.", null, convertedLong));
			} catch (NullPointerException e) {
				// Do nothing as this is expected behaviour.
			}
		}
	}
	
	/**
	 * Test the binary conversion of doubles.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testDoubleConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal double conversion
				double testDouble = TestRunnerWrapper.RANDOM.nextDouble() * 3000000d;
				byte[] binaryRep = BinaryConverter.toBytes(testDouble);
				double convertedDouble = BinaryConverter.getDouble(binaryRep);
				TestSubject.assertTestCondition(testDouble == convertedDouble, 
						String.format("The double %s has been converted to %s and reconverted to %s.", 
								testDouble, binaryRep, convertedDouble));
			} { // test invalid byte arrays
				byte[] randomBytes = new byte[TestRunnerWrapper.RANDOM.nextInt(3000)];
				TestRunnerWrapper.RANDOM.nextBytes(randomBytes);
				if (randomBytes.length != Double.BYTES) {
					try {
						double convertedDouble = BinaryConverter.getDouble(randomBytes);
						throw new TestFailureException(String.format("The conversion of %s to a double should "
								+ "fail, but was converted into %s.", Arrays.toString(randomBytes), 
								convertedDouble));
					} catch (IllegalArgumentException e) {
						// Do nothing as this is expected behaviour.
					}
				} else {
					// This should be a normal conversion, so do nothing.
					BinaryConverter.getDouble(randomBytes);
				}
			}
		} { // test positive infinity
			double positiveInf = Double.POSITIVE_INFINITY;
			byte[] binaryRep = BinaryConverter.toBytes(positiveInf);
			double convertedDouble = BinaryConverter.getDouble(binaryRep);
			TestSubject.assertTestCondition(positiveInf == convertedDouble, 
					String.format("The double %s has been converted to %s and reconverted to %s.", 
							positiveInf, binaryRep, convertedDouble));
		} { // test negative infinity
			double negativeInf = Double.NEGATIVE_INFINITY;
			byte[] binaryRep = BinaryConverter.toBytes(negativeInf);
			double convertedDouble = BinaryConverter.getDouble(binaryRep);
			TestSubject.assertTestCondition(negativeInf == convertedDouble, 
					String.format("The double %s has been converted to %s and reconverted to %s.", 
							negativeInf, binaryRep, convertedDouble));
		} { // test NaN
			double nan = Double.NaN;
			byte[] binaryRep = BinaryConverter.toBytes(nan);
			double convertedDouble = BinaryConverter.getDouble(binaryRep);
			TestSubject.assertTestCondition(Double.isNaN(nan), 
					String.format("The double %s has been converted to %s and reconverted to %s.", 
							nan, binaryRep, convertedDouble));
		} { // test null
			try {
				double convertedDouble = BinaryConverter.getDouble(null);
				throw new TestFailureException(String.format("The conversion of %s to a double should "
						+ "fail, but was converted into %s.", null, convertedDouble));
			} catch (NullPointerException e) {
				// Do nothing as this is expected behaviour.
			}
		}
	}

}
