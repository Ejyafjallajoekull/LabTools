package eyja.lab.tools.control.centre.test.management;

import eyja.lab.tools.control.centre.management.CachedReference;
import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.OriginHandler;
import eyja.lab.tools.control.centre.management.ReferenceException;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.management.ResourceReference;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The CachedReferenceTesting class tests the CachedReference class for correct functionality.
 * 
 * @author Planters
 *
 */
public class CachedReferenceTesting implements TestSubject {

	@Override
	public void runAllTests() throws TestFailureException {
		CachedReferenceTesting.testConstructors();
		CachedReferenceTesting.testGetReference();
		CachedReferenceTesting.testCaching();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test normal
				ResourceReference testRef = CachedReferenceTesting.createRandomReference();
				CachedReference<TestResource> firstRef = new CachedReference<TestResource>(testRef);
				CachedReference<TestResource> secondRef = new CachedReference<TestResource>(testRef);
				TestSubject.assertTestCondition(firstRef.equals(secondRef), 
						String.format("The cached reference %s should equal %s.", firstRef, secondRef));
				TestSubject.assertTestCondition(!firstRef.equals(null), 
						String.format("The cached reference %s should not equal %s.", firstRef, null));
				secondRef = new CachedReference<TestResource>(
						new ResourceReference(testRef.getOrigin(), testRef.getID() + 1l));
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The cached reference %s should not equal %s.", firstRef, secondRef));
				CachedReference<DifferentTestResource> differentTypeRef = new CachedReference<DifferentTestResource>(testRef);
				TestSubject.assertTestCondition(firstRef.equals(differentTypeRef), 
						String.format("The cached reference %s should equal %s despite having different "
								+ "types.", firstRef, differentTypeRef));
				
			}
		} { // test null reference
			try {
				CachedReference<TestResource> nullRef = new CachedReference<TestResource>(null);
				throw new TestFailureException(String.format("The creation of cached reference "
						+ "%s should fail as no resource reference is supplied.", nullRef));
			} catch (NullPointerException e) {
				// Do nothing as this is expected behaviour.
			}
			
		}
	}
	
	/**
	 * Test getting the underlying reference.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetReference() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			ResourceReference testRef = CachedReferenceTesting.createRandomReference();
			CachedReference<TestResource> testCachedRef = new CachedReference<TestResource>(testRef);
			TestSubject.assertTestCondition(testRef.equals(testCachedRef.getReference()), 
					String.format("The cached reference %s should wrap resource reference %s, "
							+ "but wraps %s instead..", testCachedRef, testRef, testCachedRef.getReference()));
		}
	}
	
	/**
	 * Test caching the referenced resource.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testCaching() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test valid caching
				OriginHandler testHandler = new OriginHandler();
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				testHandler.requestAdd(testOrigin);
				TestResource testResource = new TestResource();
				testOrigin.requestAdd(testResource);
				CachedReference<TestResource> testRef = new CachedReference<TestResource>(testResource.getReference());
				// test before caching
				TestSubject.assertTestCondition(!testRef.isCached(), 
						String.format("The cached reference %s should not be cached upon initialisation." ,
								testRef));
				TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
						String.format("The cached reference %s should not have any cached resource "
								+ "upon initialisation, but it has cached %s." ,
								testRef, testRef.getCachedResource()));
				// test caching
				try {
					testRef.cache(testHandler);
					TestSubject.assertTestCondition(testRef.isCached(), 
							String.format("The cached reference %s should be cached upon request." ,
									testRef));
					TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource()), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s." ,
									testRef, testResource, testRef.getCachedResource()));
					// test overwriting the cached resource
					TestResource overwriteResource = new TestResource(testResource.getID());
					testOrigin.requestAdd(overwriteResource); // overwrite the original testing resource
					TestSubject.assertTestCondition(testRef.isCached(), 
							String.format("The cached reference %s should be cached even after changing "
									+ "the referenced resource from %s to %s in the handler." ,
									testRef, testResource, overwriteResource));
					TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource()), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler and "
									+ "refreshing the cache." ,
									testRef, testResource, testRef.getCachedResource(), testResource, 
									overwriteResource));
					testRef.cache(testHandler);
					TestSubject.assertTestCondition(testRef.isCached(), 
							String.format("The cached reference %s should be cached even after changing "
									+ "the referenced resource from %s to %s in the handler." ,
									testRef, testResource, overwriteResource));
					TestSubject.assertTestCondition(overwriteResource.equals(testRef.getCachedResource()), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler and "
									+ "refreshing the cache." ,
									testRef, overwriteResource, testRef.getCachedResource(), testResource, 
									overwriteResource));
				} catch (ReferenceException e) {
					e.printStackTrace();
					throw new TestFailureException(e);
				}
			} {
				OriginHandler testHandler = new OriginHandler();
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				testHandler.requestAdd(testOrigin);
				TestResource testResource = new TestResource();
				testOrigin.requestAdd(testResource);
				CachedReference<DifferentTestResource> testRef = new CachedReference<DifferentTestResource>(testResource.getReference());
				// test before caching
				TestSubject.assertTestCondition(!testRef.isCached(), 
						String.format("The cached reference %s should not be cached upon initialisation." ,
								testRef));
				TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
						String.format("The cached reference %s should not have any cached resource "
								+ "upon initialisation, but it has cached %s." ,
								testRef, testRef.getCachedResource()));
				// test caching
				try {
					testRef.cache(testHandler);
					System.out.println(testRef.getCachedResource().getClass());
					throw new TestFailureException(String.format("Caching reference %s should fail as "
							+ "the cached resource %s is of type %s, but type %s is expected.", 
							testRef, testResource, testResource.getClass(), DifferentTestResource.class));
				} catch (ReferenceException e) {
					/*
					 * Do nothing as this is expected behaviour.
					 */
				}
			} { // test wrong handler
				
			}
		} { // test null handler
			
		}
	}
	
	/**
	 * Create a random resource reference.
	 * 
	 * @return a random reference
	 */
	private static ResourceReference createRandomReference() {
		return new ResourceReference(TestRunnerWrapper.createRandomString(), 
				TestRunnerWrapper.RANDOM.nextLong());
	}
	
	/**
	 * Create a random origin populated with random resources.
	 * 
	 * @return a random origin
	 */
	private static Origin createRandomOrigin() {
		Origin randomOrigin = OriginTesting.createRandomOrigin();
		int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
		TestResource[] randomResources = new TestResource[randomNumResource]; 
		for (int j = 0; j < randomNumResource; j++) {
			randomResources[j] = new TestResource(new ResourceID(randomOrigin, j));
			randomOrigin.requestAdd(randomResources[j]);
		}
		return randomOrigin;
	}

}
