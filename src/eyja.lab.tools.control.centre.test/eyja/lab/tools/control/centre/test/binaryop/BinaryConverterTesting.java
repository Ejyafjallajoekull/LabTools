package eyja.lab.tools.control.centre.test.binaryop;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import eyja.lab.tools.control.centre.binaryop.BinaryConverter;
import eyja.lab.tools.control.centre.management.ResourceReference;
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
		BinaryConverterTesting.testBooleanConversion();
		BinaryConverterTesting.testIntConversion();
		BinaryConverterTesting.testLongConversion();
		BinaryConverterTesting.testDoubleConversion();
		BinaryConverterTesting.testStringConversion();
		BinaryConverterTesting.testLocalDateTimeConversion();
		BinaryConverterTesting.testResourceReferenceConversion();
	}
	
	/**
	 * Test the binary conversion of booleans.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testBooleanConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal boolean conversion
				boolean testBool = TestRunnerWrapper.RANDOM.nextBoolean();
				byte binaryRep = BinaryConverter.toBytes(testBool);
				boolean convertedInt = BinaryConverter.getBoolean(binaryRep);
				TestSubject.assertTestCondition(testBool == convertedInt, 
						String.format("The boolean %s has been converted to %s and reconverted to %s.", 
								testBool, binaryRep, convertedInt));
			} 
		}
	}
	
	/**
	 * Test the binary conversion of ints.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testIntConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal int conversion
				int testInt = TestRunnerWrapper.RANDOM.nextInt();
				byte[] binaryRep = BinaryConverter.toBytes(testInt);
				int convertedInt = BinaryConverter.getInt(binaryRep);
				TestSubject.assertTestCondition(testInt == convertedInt, 
						String.format("The int %s has been converted to %s and reconverted to %s.", 
								testInt, binaryRep, convertedInt));
			} { // test invalid byte arrays
				byte[] randomBytes = new byte[TestRunnerWrapper.RANDOM.nextInt(3000)];
				TestRunnerWrapper.RANDOM.nextBytes(randomBytes);
				if (randomBytes.length != Integer.BYTES) {
					try {
						long convertedInt = BinaryConverter.getInt(randomBytes);
						throw new TestFailureException(String.format("The conversion of %s to a int should "
								+ "fail, but was converted into %s.", Arrays.toString(randomBytes), 
								convertedInt));
					} catch (IllegalArgumentException e) {
						// Do nothing as this is expected behaviour.
					}
				} else {
					// This should be a normal conversion, so do nothing.
					BinaryConverter.getInt(randomBytes);
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
	
	/**
	 * Test the binary conversion of strings.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testStringConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal string conversion
				String testString = TestRunnerWrapper.createRandomString();
				byte[] binaryRep = BinaryConverter.toBytes(testString);
				String convertedString = BinaryConverter.getString(binaryRep);
				TestSubject.assertTestCondition(testString.equals(convertedString), 
						String.format("The string %s has been converted to %s and reconverted to %s.", 
								testString, binaryRep, convertedString));
			} { // test invalid byte arrays
				byte[] randomBytes = BinaryConverter.toBytes(TestRunnerWrapper.RANDOM.nextInt(3000000));
				try {
					ResourceReference convertedString = BinaryConverter.getResourceReference(randomBytes);
					throw new TestFailureException(String.format("The conversion of %s to a "
							+ "string should fail, but was converted into %s.", 
							Arrays.toString(randomBytes), convertedString));
				} catch (IllegalArgumentException e) {
					// Do nothing as this is expected behaviour.
				}
			}
		} { // test null
			String nullString = null;
			byte[] binaryRep = BinaryConverter.toBytes(nullString);
			String convertedString = BinaryConverter.getString(binaryRep);
			TestSubject.assertTestCondition(nullString == convertedString, 
					String.format("The string %s has been converted to %s and reconverted to %s.", 
							nullString, binaryRep, convertedString));
		}
	}
	
	/**
	 * Test the binary conversion of local date time.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testLocalDateTimeConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal local date time conversion
				long seconds = TestRunnerWrapper.RANDOM.nextInt();
				int nano = TestRunnerWrapper.RANDOM.nextInt(1000000000);
				try {
					LocalDateTime testLDT = LocalDateTime.ofEpochSecond(seconds, nano, ZoneOffset.UTC);
					byte[] binaryRep = BinaryConverter.toBytes(testLDT);
					LocalDateTime convertedLDT = BinaryConverter.getLocalDateTime(binaryRep);
					TestSubject.assertTestCondition(testLDT.equals(convertedLDT), 
							String.format("The local date time %s has been converted to %s and reconverted to %s.", 
									testLDT, binaryRep, convertedLDT));	
				} catch (DateTimeException e) {
					/*
					 * Do nothing as this should only happen if by accident the maximum range of 
					 * LocalDateTime got exceeded. This error can just be ignored as it should never 
					 * happen.
					 */
					e.printStackTrace();
				}
			} { // test invalid byte arrays
				byte[] randomBytes = new byte[TestRunnerWrapper.RANDOM.nextInt(3000)];
				TestRunnerWrapper.RANDOM.nextBytes(randomBytes);
				if (randomBytes.length != BinaryConverter.LOCAL_DATE_TIME_BYTES) {
					try {
						LocalDateTime convertedLDT = BinaryConverter.getLocalDateTime(randomBytes);
						throw new TestFailureException(String.format("The conversion of %s to a "
								+ "local date time should fail, but was converted into %s.", 
								Arrays.toString(randomBytes), convertedLDT));
					} catch (IllegalArgumentException e) {
						// Do nothing as this is expected behaviour.
					}
				} else {
					// This should be a normal conversion, so do nothing.
				}
			}
		} { // test null
			try {
				LocalDateTime convertedLDT = BinaryConverter.getLocalDateTime(null);
				throw new TestFailureException(String.format("The conversion of %s to a local date time "
						+ "should fail, but was converted into %s.", 
						null, convertedLDT));
			} catch (NullPointerException e) {
				// Do nothing as this is expected behaviour.
			}
		}
	}
	
	/**
	 * Test the binary conversion of resource references.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testResourceReferenceConversion() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal resource reference conversion
				ResourceReference testRef = new ResourceReference(TestRunnerWrapper.createRandomString(), 
						TestRunnerWrapper.RANDOM.nextLong());
				byte[] binaryRep = BinaryConverter.toBytes(testRef);
				ResourceReference convertedRef = BinaryConverter.getResourceReference(binaryRep);
				TestSubject.assertTestCondition(testRef.equals(convertedRef), 
						String.format("The resource reference %s has been converted to %s and reconverted to %s.", 
								testRef, binaryRep, convertedRef));
			} { // test invalid byte arrays
				byte[] randomBytes = BinaryConverter.toBytes(TestRunnerWrapper.RANDOM.nextInt(3000000));
				try {
					ResourceReference convertedRef = BinaryConverter.getResourceReference(randomBytes);
					throw new TestFailureException(String.format("The conversion of %s to a "
							+ "resource reference should fail, but was converted into %s.", 
							Arrays.toString(randomBytes), convertedRef));
				} catch (IllegalArgumentException e) {
					// Do nothing as this is expected behaviour.
				}
			}
		} { // test null
			ResourceReference nullRef = null;
			byte[] binaryRep = BinaryConverter.toBytes(nullRef);
			ResourceReference convertedRef = BinaryConverter.getResourceReference(binaryRep);
			TestSubject.assertTestCondition(nullRef == convertedRef, 
					String.format("The resource reference %s has been converted to %s and reconverted to %s.", 
							nullRef, binaryRep, convertedRef));
		}
	}

}
