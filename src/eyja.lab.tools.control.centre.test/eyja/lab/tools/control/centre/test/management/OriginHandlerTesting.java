package eyja.lab.tools.control.centre.test.management;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.OriginHandler;
import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceReference;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The OriginHandlerTesting class test the OriginHandler class for correct functionality.
 * 
 * @author Planters
 *
 */
public class OriginHandlerTesting implements TestSubject {

	private static final Comparator<Origin> ORIGIN_COMPARATOR= new Comparator<Origin>() {

		@Override
		public int compare(Origin o1, Origin o2) {
			return o1.getFile().getPath().compareTo(o2.getFile().getPath());
		}
		
	};
	
	@Override
	public void runAllTests() throws TestFailureException {
		OriginHandlerTesting.testConstructors();
		OriginHandlerTesting.testGetOrigins();
		OriginHandlerTesting.testAddingOrigins();
		OriginHandlerTesting.testRemove();
		OriginHandlerTesting.testDereference();
		OriginHandlerTesting.testCheckedDereference();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			OriginHandler firstHandler = new OriginHandler();
			OriginHandler secondHandler = new OriginHandler();
			TestSubject.assertTestCondition(firstHandler.equals(secondHandler), 
					String.format("The origin handler %s should equal %s.", firstHandler, secondHandler));
			TestSubject.assertTestCondition(!firstHandler.equals(null), 
					String.format("The origin handler %s should not equal %s.", firstHandler, null));
			secondHandler.requestAdd(OriginTesting.createRandomOrigin());
			TestSubject.assertTestCondition(!firstHandler.equals(secondHandler), 
					String.format("The origin handler %s should not equal %s.", firstHandler, secondHandler));
		}
	}
	
	/**
	 * Test getting origins from the handler.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetOrigins() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			OriginHandler testHandler = new OriginHandler();
			int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(3);
			Origin[] randomOrigins = new Origin[randomNumOrigin]; 
			for (int j = 0; j < randomNumOrigin; j++) {
				randomOrigins[j] = new Origin(new File(Integer.toString(j)), null);
				testHandler.requestAdd(randomOrigins[j]);
			}
			// Sort arrays so they are comparable.
			Arrays.sort(randomOrigins, OriginHandlerTesting.ORIGIN_COMPARATOR);
			Origin[] retrievedOrigins = testHandler.getOrigins();
			Arrays.sort(randomOrigins, OriginHandlerTesting.ORIGIN_COMPARATOR);
			TestSubject.assertTestCondition(Arrays.equals(retrievedOrigins, randomOrigins), 
					String.format("The origin handler %s should contain the origins %s, but contains %s.", 
							testHandler, Arrays.toString(randomOrigins), 
							Arrays.toString(retrievedOrigins)));
		}
	}
	
	/**
	 * Test adding origins to the handler.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testAddingOrigins() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test valid addition
				OriginHandler testHandler = new OriginHandler();
				int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(3);
				Origin[] randomOrigins = new Origin[randomNumOrigin]; 
				for (int j = 0; j < randomNumOrigin; j++) {
					randomOrigins[j] = new Origin(new File(Integer.toString(j)), null);
					boolean testContains = Arrays.stream(testHandler.getOrigins()).anyMatch(randomOrigins[j]::equals);
					TestSubject.assertTestCondition(!testContains, String.format("The origin handler %s "
							+ "should not contain the origin %s before adding it.", 
									testHandler, randomOrigins[j]));
					boolean success = testHandler.requestAdd(randomOrigins[j]);
					TestSubject.assertTestCondition(success, String.format("The origin handler %s "
							+ "should be successful adding the origin %s, but was not.", 
									testHandler, randomOrigins[j]));
					testContains = Arrays.stream(testHandler.getOrigins()).anyMatch(randomOrigins[j]::equals);
					TestSubject.assertTestCondition(testContains, String.format("The origin handler %s "
							+ "should contain the origin %s after adding it.", 
									testHandler, randomOrigins[j]));
				}
			} { // test adding null
				OriginHandler nullHandler = new OriginHandler();
				boolean success = nullHandler.requestAdd(null);
				TestSubject.assertTestCondition(!success, String.format("The origin handler %s "
						+ "should not be successful adding the origin %s.", 
								nullHandler, null));
			} { // test invalid addition
				OriginHandler nullHandler = new OriginHandler();
				Origin invalidOrigin  = new Origin(null, null);
				boolean success = nullHandler.requestAdd(invalidOrigin);
				TestSubject.assertTestCondition(!success, String.format("The origin handler %s "
						+ "should not be successful adding the origin %s.", 
								nullHandler, invalidOrigin));
			} { // test addition of origins referencing the same physical file
				OriginHandler testHandler = new OriginHandler();
				Origin originalOrigin = OriginTesting.createRandomOrigin();
				testHandler.requestAdd(originalOrigin);
				// same file path but not equal since a resource has been added
				Origin equivalentOrigin = new Origin(originalOrigin.getFile(), null);
				equivalentOrigin.requestAdd(new TestResource());
				boolean containsOriginal = Arrays.stream(testHandler.getOrigins()).anyMatch(originalOrigin::equals);
				TestSubject.assertTestCondition(containsOriginal, String.format("The origin handler %s "
						+ "should contain the origin %s before overriding it with origin %s.", 
								testHandler, originalOrigin, equivalentOrigin));
				testHandler.requestAdd(equivalentOrigin);
				containsOriginal = Arrays.stream(testHandler.getOrigins()).anyMatch(originalOrigin::equals);
				boolean containsEquivalent = Arrays.stream(testHandler.getOrigins()).anyMatch(equivalentOrigin::equals);
				TestSubject.assertTestCondition(!containsOriginal, String.format("The origin handler %s "
						+ "should not contain the origin %s after adding origin %s.", 
								testHandler, originalOrigin, equivalentOrigin));
				TestSubject.assertTestCondition(containsEquivalent, String.format("The origin handler %s "
						+ "should contain the origin %s after overriding origin %s.", 
								testHandler, equivalentOrigin, originalOrigin));
			}
		}
	}
	
	/**
	 * Test removing origins from the handler.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testRemove() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			OriginHandler removeHandler = new OriginHandler();
			// add some random origins
			int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(300);
			for (int j = 0; j < randomNumOrigin; j++) {
				removeHandler.requestAdd(OriginTesting.createRandomOrigin());
			}
			Origin removeOrigin = OriginTesting.createRandomOrigin();
			removeHandler.requestAdd(removeOrigin);
			boolean containsRemoval = Arrays.stream(removeHandler.getOrigins()).anyMatch(removeOrigin::equals);
			TestSubject.assertTestCondition(containsRemoval, String.format("The origin handler %s "
					+ "should contain the origin %s before removing it.", 
					removeHandler, removeOrigin));
			removeHandler.remove(removeOrigin);
			containsRemoval = Arrays.stream(removeHandler.getOrigins()).anyMatch(removeOrigin::equals);
			TestSubject.assertTestCondition(!containsRemoval, String.format("The origin handler %s "
					+ "should contain the origin %s before removing it.", 
					removeHandler, removeOrigin));
			// test removing null
			Origin[] allOriginsBeforeRemoval = removeHandler.getOrigins();
			removeHandler.remove(null);
			Origin[] allOriginsAfterRemoval = removeHandler.getOrigins();
			// Sort arrays so they are comparable.
			Arrays.sort(allOriginsBeforeRemoval, OriginHandlerTesting.ORIGIN_COMPARATOR);
			Arrays.sort(allOriginsAfterRemoval, OriginHandlerTesting.ORIGIN_COMPARATOR);
			TestSubject.assertTestCondition(Arrays.equals(allOriginsBeforeRemoval, allOriginsAfterRemoval), 
					String.format("The origin handler %s should contain the origins %s after removal of "
							+ "%s, but contains %s.", 
							removeHandler, Arrays.toString(allOriginsBeforeRemoval), null, 
							Arrays.toString(allOriginsAfterRemoval)));
		}
	}
	
	/**
	 * Test dereferencing resource references.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testDereference() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{
				OriginHandler refHandler = new OriginHandler();
				// add some random origins
				int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumOrigin; j++) {
					refHandler.requestAdd(OriginTesting.createRandomOrigin());
				}
				// create a random origin with a resource that can be referenced
				Origin refOrigin = OriginTesting.createRandomOrigin();
				Resource refResource = new TestResource();
				refOrigin.requestAdd(refResource);
				ResourceReference testRef = refResource.getReference();
				refHandler.requestAdd(refOrigin);
				// dereference the reference
				Resource derefResource = refHandler.dereference(testRef);
				TestSubject.assertTestCondition(refResource == derefResource, String.format("The origin handler %s "
						+ "should dereference the resource reference %s to resource %s, but instead "
						+ "dereference it to %s.", 
						refHandler, testRef, refResource, derefResource));
			} { // test unmanaged origins
				OriginHandler unmanagedHandler = new OriginHandler();
				// add some random origins
				int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumOrigin; j++) {
					unmanagedHandler.requestAdd(OriginTesting.createRandomOrigin());
				}
				// create a random origin with a resource that can be referenced but do not add it to the handler
				Origin unmanagedOrigin = OriginTesting.createRandomOrigin();
				Resource unmanagedResource = new TestResource();
				unmanagedOrigin.requestAdd(unmanagedResource);
				ResourceReference unmanagedRef = unmanagedResource.getReference();
				// dereference the reference
				try {
					Resource derefResource = unmanagedHandler.dereference(unmanagedRef);
					throw new TestFailureException(String.format("The origin handler %s should fail "
							+ "dereferencing %s since it belongs to the unmanaged origin %s, but "
							+ "dereferenced it to %s.", unmanagedHandler, unmanagedRef, unmanagedOrigin, 
							derefResource));
				} catch (IllegalArgumentException e) {
					// Do nothing as this is expected behaviour.
				}
			}
		}
		{ // test dereferencing null
			OriginHandler nullHandler = new OriginHandler();
			try {
				nullHandler.dereference(null);
				throw new TestFailureException(String.format("The origin handler %s should fail "
						+ "dereferencing %s.", nullHandler, null));
			} catch (NullPointerException e) {
				// Do nothing as this is expected behaviour.
			}
		}
	}
	
	/**
	 * Test dereferencing resource references with expected types.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testCheckedDereference() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{
				OriginHandler refHandler = new OriginHandler();
				// add some random origins
				int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumOrigin; j++) {
					refHandler.requestAdd(OriginTesting.createRandomOrigin());
				}
				// create a random origin with a resource that can be referenced
				Origin refOrigin = OriginTesting.createRandomOrigin();
				Resource refResource = new TestResource();
				refOrigin.requestAdd(refResource);
				ResourceReference testRef = refResource.getReference();
				refHandler.requestAdd(refOrigin);
				// dereference the reference
				Resource derefResource = refHandler.dereference(testRef, TestResource.class);
				TestSubject.assertTestCondition(refResource == derefResource, String.format("The origin handler %s "
						+ "should dereference the resource reference %s to resource %s, but instead "
						+ "dereference it to %s.", 
						refHandler, testRef, refResource, derefResource));
				try {
					Class<DifferentTestResource> failureType = DifferentTestResource.class;
					Resource failureResource = refHandler.dereference(testRef, failureType);
					throw new TestFailureException(String.format("The origin handler %s should fail "
							+ "dereferencing %s since it should point to resource %s of type %s "
							+ "while the type %s has been specified. However, dereferincing "
							+ "returned resource %s of type %s.", refHandler, testRef, 
							refResource, refResource.getClass(), failureType, failureResource, 
							failureResource.getClass()));
				} catch (ClassCastException e) {
					// Do nothing as this is expected behaviour.
				}
			} { // test unmanaged origins
				OriginHandler unmanagedHandler = new OriginHandler();
				// add some random origins
				int randomNumOrigin = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumOrigin; j++) {
					unmanagedHandler.requestAdd(OriginTesting.createRandomOrigin());
				}
				// create a random origin with a resource that can be referenced but do not add it to the handler
				Origin unmanagedOrigin = OriginTesting.createRandomOrigin();
				Resource unmanagedResource = new TestResource();
				unmanagedOrigin.requestAdd(unmanagedResource);
				ResourceReference unmanagedRef = unmanagedResource.getReference();
				// dereference the reference
				try {
					Resource derefResource = unmanagedHandler.dereference(unmanagedRef, TestResource.class);
					throw new TestFailureException(String.format("The origin handler %s should fail "
							+ "dereferencing %s since it belongs to the unmanaged origin %s, but "
							+ "dereferenced it to %s.", unmanagedHandler, unmanagedRef, unmanagedOrigin, 
							derefResource));
				} catch (IllegalArgumentException e) {
					// Do nothing as this is expected behaviour.
				}
			}
		}
		{ // test dereferencing null
			OriginHandler nullHandler = new OriginHandler();
			try {
				nullHandler.dereference(null, TestResource.class);
				throw new TestFailureException(String.format("The origin handler %s should fail "
						+ "dereferencing %s.", nullHandler, null));
			} catch (NullPointerException e) {
				// Do nothing as this is expected behaviour.
			}
		}
	}

}
