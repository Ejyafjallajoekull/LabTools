package eyja.lab.tools.control.centre.test.management;

import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.management.ResourceReference;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The ResourceReferenceTesting class tests the ResourceReference class for correct functionality.
 * 
 * @author Planters
 *
 */
public class ResourceReferenceTesting implements TestSubject {

	@Override
	public void runAllTests() throws TestFailureException {
		ResourceReferenceTesting.testConstructors();
		ResourceReferenceTesting.testGetting();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test custom definition
				String testOrigin = TestRunnerWrapper.createRandomString();
				long testID = TestRunnerWrapper.RANDOM.nextLong();
				ResourceReference firstRef = new ResourceReference(testOrigin, testID);
				ResourceReference secondRef = new ResourceReference(testOrigin, testID);
				TestSubject.assertTestCondition(firstRef.equals(secondRef), 
						String.format("The resource reference %s should equal %s.", firstRef, secondRef));
				TestSubject.assertTestCondition(!firstRef.equals(null), 
						String.format("The resource reference %s should not equal %s.", firstRef, null));
				secondRef = new ResourceReference(testOrigin, testID + 1l);
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				secondRef = new ResourceReference(TestRunnerWrapper.createRandomString() + 
						firstRef.getOrigin() + "failure", testID);
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				// test null origin
				try {
					ResourceReference nullRef = new ResourceReference(null, testID);
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
			} { // test resource ID
				Origin testOrigin = OriginTesting.createRandomOrigin();
				long testID = TestRunnerWrapper.RANDOM.nextLong();
				ResourceID testRID = new ResourceID(testOrigin, testID);
				ResourceReference firstRef = new ResourceReference(testRID);
				ResourceReference secondRef = new ResourceReference(testRID);
				TestSubject.assertTestCondition(firstRef.equals(secondRef), 
						String.format("The resource reference %s should equal %s.", firstRef, secondRef));
				TestSubject.assertTestCondition(!firstRef.equals(null), 
						String.format("The resource reference %s should not equal %s.", firstRef, null));
				secondRef = new ResourceReference(new ResourceID(testOrigin, testID + 1l));
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				secondRef = new ResourceReference(TestRunnerWrapper.createRandomString() + 
						firstRef.getOrigin() + "failure", testID);
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				// test null id
				try {
					ResourceReference nullRef = new ResourceReference(null);
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
				// test null origin
				try {
					ResourceReference nullRef = new ResourceReference(new ResourceID(null, testID));
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
				// test null origin
				try {
					ResourceReference nullRef = new ResourceReference(new ResourceID(new Origin(null, null), testID));
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
			}
			
		}
	}
	
	/**
	 * Test getting the different parts of the reference.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetting() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test custom definition
				String testOrigin = TestRunnerWrapper.createRandomString();
				long testID = TestRunnerWrapper.RANDOM.nextLong();
				ResourceReference testRef = new ResourceReference(testOrigin, testID);
				TestSubject.assertTestCondition(testRef.getID() == testID, 
						String.format("The ID specifier %s of the resource reference %s should be %s.", 
								testRef.getID(), testRef, testID));
				TestSubject.assertTestCondition(testRef.getOrigin() == testOrigin, 
						String.format("The origin specifier %s of the resource reference %s should be %s.", 
								testRef.getOrigin(), testRef, testOrigin));
			} { // test resource ID
				long testID = TestRunnerWrapper.RANDOM.nextLong();
				ResourceID testRID = new ResourceID(OriginTesting.createRandomOrigin(), testID);
				String testOrigin = testRID.getOrigin().getFile().getPath();
				ResourceReference testRef = new ResourceReference(testRID);
				TestSubject.assertTestCondition(testRef.getID() == testID, 
						String.format("The ID specifier %s of the resource reference %s should be %s.", 
								testRef.getID(), testRef, testID));
				TestSubject.assertTestCondition(testRef.getOrigin() == testOrigin, 
						String.format("The origin specifier %s of the resource reference %s should be %s.", 
								testRef.getOrigin(), testRef, testOrigin));
			}
		}
	}
	
}
