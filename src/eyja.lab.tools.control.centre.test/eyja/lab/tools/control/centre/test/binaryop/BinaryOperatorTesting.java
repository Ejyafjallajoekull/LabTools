package eyja.lab.tools.control.centre.test.binaryop;

import java.util.Arrays;

import eyja.lab.tools.control.centre.binaryop.BinaryOperator;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The BinaryOperatorTesting class test the BinaryOperator class for correct functionality.
 * 
 * @author Planters
 *
 */
public class BinaryOperatorTesting implements TestSubject {

	@Override
	public void runAllTests() throws TestFailureException {
		BinaryOperatorTesting.testJoinBytes();
	}
	
	/**
	 * Test the joining of bytes.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testJoinBytes() throws TestFailureException {
		for (int i = 0; i < 10000; i++) { // test valid byte arrays
			int numBytes = TestRunnerWrapper.RANDOM.nextInt(20);
			byte[][] a = new byte[numBytes + 1][];
			byte[] reference = new byte[0];
			for (int j = 0; j < a.length; j++) {
				if (TestRunnerWrapper.RANDOM.nextDouble() >= 0.05) {
					a[j] = new byte[TestRunnerWrapper.RANDOM.nextInt(300)];
					TestRunnerWrapper.RANDOM.nextBytes(a[j]);
					byte[] oldRef = reference;
					reference = new byte[oldRef.length + a[j].length];
					System.arraycopy(oldRef, 0, reference, 0, oldRef.length);
					System.arraycopy(a[j], 0, reference, oldRef.length, a[j].length);
				} else { // insert random nulls
					a[j] = null;
				}
			}
			byte[] joined = BinaryOperator.joinBytes(a);
			TestSubject.assertTestCondition(Arrays.equals(joined, reference), 
					String.format("Joining the bytes %s should yield %s, but did yield %s.", 
							Arrays.toString(a), Arrays.toString(reference), Arrays.toString(joined)));
		} { // test zero length
			byte[][] zeroLength = new byte[0][];
			byte[] joined = BinaryOperator.joinBytes(zeroLength);
			TestSubject.assertTestCondition(joined == null, 
					String.format("Joining the bytes %s should yield %s, but did yield %s.", 
							Arrays.toString(zeroLength), null, Arrays.toString(joined)));
		} { // test no argument
			byte[] joined = BinaryOperator.joinBytes();
			TestSubject.assertTestCondition(joined == null, 
					String.format("Joining the no bytes should yield %s, but did yield %s.", 
							null, Arrays.toString(joined)));
		}
	}

}
