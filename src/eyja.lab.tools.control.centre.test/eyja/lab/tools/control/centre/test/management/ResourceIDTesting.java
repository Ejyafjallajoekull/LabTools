package eyja.lab.tools.control.centre.test.management;

import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The ResourceIDTesting class test the ResourceID class for correct functionality.
 * 
 * @author Planters
 *
 */
public class ResourceIDTesting implements TestSubject {

	@Override
	public void runAllTests() throws TestFailureException {
		ResourceIDTesting.testConstructors();
		ResourceIDTesting.testGetting();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			Origin testOrigin = null;
			if (TestRunnerWrapper.RANDOM.nextDouble() > 0.001d) {
				testOrigin = OriginTesting.createRandomOrigin();
			}
			long testID = TestRunnerWrapper.RANDOM.nextLong();
			ResourceID firstID = new ResourceID(testOrigin, testID);
			ResourceID secondID = new ResourceID(testOrigin, testID);
			TestSubject.assertTestCondition(firstID.equals(secondID), 
					String.format("The resource ID %s should equal %s.", firstID, secondID));
			TestSubject.assertTestCondition(!firstID.equals(null), 
					String.format("The resource ID %s should not equal %s.", firstID, null));
			secondID = new ResourceID(testOrigin, testID + 1l);
			TestSubject.assertTestCondition(!firstID.equals(secondID), 
					String.format("The resource ID %s should not equal %s.", firstID, secondID));
			secondID = new ResourceID(OriginTesting.createRandomOrigin(), testID);
			TestSubject.assertTestCondition(!firstID.equals(secondID), 
					String.format("The resource ID %s should not equal %s.", firstID, secondID));
		}
	}
	
	/**
	 * Test getting the different parts of the ID.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetting() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			Origin testOrigin = null;
			if (TestRunnerWrapper.RANDOM.nextDouble() > 0.001d) {
				testOrigin = OriginTesting.createRandomOrigin();
			}
			long testID = TestRunnerWrapper.RANDOM.nextLong();
			ResourceID testRID = new ResourceID(testOrigin, testID);
			TestSubject.assertTestCondition(testRID.getID() == testID, 
					String.format("The ID specifier %s of the resource ID %s should be %s.", 
							testRID.getID(), testRID, testID));
			TestSubject.assertTestCondition(testRID.getOrigin() == testOrigin, 
					String.format("The origin specifier %s of the resource ID %s should be %s.", 
							testRID.getOrigin(), testRID, testOrigin));
		}
	}

}
