package eyja.lab.tools.control.centre.test.management;

import eyja.lab.tools.control.centre.management.CachedReference;
import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.OriginHandler;
import eyja.lab.tools.control.centre.management.ReferenceException;
import eyja.lab.tools.control.centre.management.Resource;
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
		CachedReferenceTesting.testCaching();
		CachedReferenceTesting.testGetCachedResource();
		CachedReferenceTesting.testGetResource();
		CachedReferenceTesting.testgenerateCachedReference();
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
				CachedReference firstRef = new CachedReference(testOrigin, testID);
				CachedReference secondRef = new CachedReference(testOrigin, testID);
				TestSubject.assertTestCondition(firstRef.equals(secondRef), 
						String.format("The resource reference %s should equal %s.", firstRef, secondRef));
				TestSubject.assertTestCondition(!firstRef.equals(null), 
						String.format("The resource reference %s should not equal %s.", firstRef, null));
				secondRef = new CachedReference(testOrigin, testID + 1l);
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				secondRef = new CachedReference(TestRunnerWrapper.createRandomString() + 
						firstRef.getOrigin() + "failure", testID);
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				// test null origin
				try {
					CachedReference nullRef = new CachedReference(null, testID);
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
			} { // test resource ID
				Origin testOrigin = OriginTesting.createRandomOrigin();
				long testID = TestRunnerWrapper.RANDOM.nextLong();
				ResourceID testRID = new ResourceID(testOrigin, testID);
				CachedReference firstRef = new CachedReference(testRID);
				CachedReference secondRef = new CachedReference(testRID);
				TestSubject.assertTestCondition(firstRef.equals(secondRef), 
						String.format("The resource reference %s should equal %s.", firstRef, secondRef));
				TestSubject.assertTestCondition(!firstRef.equals(null), 
						String.format("The resource reference %s should not equal %s.", firstRef, null));
				secondRef = new CachedReference(new ResourceID(testOrigin, testID + 1l));
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				secondRef = new CachedReference(TestRunnerWrapper.createRandomString() + 
						firstRef.getOrigin() + "failure", testID);
				TestSubject.assertTestCondition(!firstRef.equals(secondRef), 
						String.format("The resource reference %s should not equal %s.", firstRef, secondRef));
				// test null id
				try {
					CachedReference nullRef = new CachedReference(null);
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
				// test null origin
				try {
					CachedReference nullRef = new CachedReference(new ResourceID(null, testID));
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
				// test null origin
				try {
					CachedReference nullRef = new CachedReference(new ResourceID(new Origin(null, null), testID));
					throw new TestFailureException(String.format("The creation of resource reference "
							+ "%s should fail as no origin is supplied.", nullRef));
				} catch (NullPointerException e) {
					// Do nothing as this is expected behaviour.
				}
			}
			
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
				CachedReference testRef = testResource.getCachedReference();
				// test before caching
				TestSubject.assertTestCondition(!testRef.isCached(), 
						String.format("The cached reference %s should not be cached upon initialisation." ,
								testRef));
				TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
						String.format("The cached reference %s should not have any cached resource "
								+ "upon initialisation, but it has cached %s." ,
								testRef, testRef.getCachedResource()));
				// test caching
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
//			} {
//				OriginHandler testHandler = new OriginHandler();
//				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
//				testHandler.requestAdd(testOrigin);
//				TestResource testResource = new TestResource();
//				testOrigin.requestAdd(testResource);
//				CachedReference<DifferentTestResource> testRef = new CachedReference<DifferentTestResource>(testResource.getReference());
//				// test before caching
//				TestSubject.assertTestCondition(!testRef.isCached(), 
//						String.format("The cached reference %s should not be cached upon initialisation." ,
//								testRef));
//				TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
//						String.format("The cached reference %s should not have any cached resource "
//								+ "upon initialisation, but it has cached %s." ,
//								testRef, testRef.getCachedResource()));
//				// test caching
//				try {
//					testRef.cache(testHandler);
//					System.out.println(testRef.getCachedResource().getClass());
//					throw new TestFailureException(String.format("Caching reference %s should fail as "
//							+ "the cached resource %s is of type %s, but type %s is expected.", 
//							testRef, testResource, testResource.getClass(), DifferentTestResource.class));
//				} catch (ReferenceException e) {
//					/*
//					 * Do nothing as this is expected behaviour.
//					 */
//				}
			} { // test wrong handler
				OriginHandler testHandler = new OriginHandler();
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				testHandler.requestAdd(testOrigin);
				TestResource testResource = new TestResource();
				Origin wrongOrigin = CachedReferenceTesting.createRandomOrigin();
				wrongOrigin.requestAdd(testResource);
				CachedReference testRef = testResource.getCachedReference();
				// test before caching
				TestSubject.assertTestCondition(!testRef.isCached(), 
						String.format("The cached reference %s should not be cached upon initialisation." ,
								testRef));
				TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
						String.format("The cached reference %s should not have any cached resource "
								+ "upon initialisation, but it has cached %s." ,
								testRef, testRef.getCachedResource()));
				try {
					testRef.cache(testHandler);
					throw new TestFailureException(String.format("Caching reference %s should fail as "
							+ "the handler %s does not handle the origin %s the reference "
							+ "is pointing to.",
							testRef, testHandler, wrongOrigin));
				} catch (IllegalArgumentException e) {
					/*
					 * Do nothing as this is expected behaviour.
					 */
				}
			}
		} { // test null handler
			try {
				CachedReference nullRef = CachedReferenceTesting.createRandomReference();
				nullRef.cache(null);
				throw new TestFailureException(String.format("Caching reference %s should fail as "
						+ "the handler responsible for caching is null.", nullRef));
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
		}
	}
	
	/**
	 * Test retrieving the cached resources.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetCachedResource() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test valid caching
				try {
					OriginHandler testHandler = new OriginHandler();
					Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
					testHandler.requestAdd(testOrigin);
					TestResource testResource = new TestResource();
					Class<?> testClass = testResource.getClass();
					testOrigin.requestAdd(testResource);
					CachedReference testRef = testResource.getCachedReference();
					// test before caching
					TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
							String.format("The cached reference %s should not have any cached resource "
									+ "upon initialisation, but it has cached %s." ,
									testRef, testRef.getCachedResource()));
					TestSubject.assertTestCondition(testRef.getCachedResource(testClass) == null, 
							String.format("The cached reference %s should not have any cached resource "
									+ "of class %s upon initialisation, but it has cached %s." ,
									testRef, TestResource.class, testRef.getCachedResource()));
					// test after caching
					testRef.cache(testHandler);
					TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource()), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s." ,
									testRef, testResource, testRef.getCachedResource()));
					TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource(testClass)), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s." ,
									testRef, testResource, testRef.getCachedResource()));
					// test overwriting the cached resource
					TestResource overwriteResource = new TestResource(testResource.getID());
					testOrigin.requestAdd(overwriteResource); // overwrite the original testing resource
					TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource()), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler." ,
									testRef, testResource, testRef.getCachedResource(), testResource, 
									overwriteResource));
					TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource(testClass)), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler." ,
									testRef, testResource, testRef.getCachedResource(), testResource, 
									overwriteResource));
					testRef.cache(testHandler);
					TestSubject.assertTestCondition(overwriteResource.equals(testRef.getCachedResource()), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler and "
									+ "refreshing the cache." ,
									testRef, overwriteResource, testRef.getCachedResource(), testResource, 
									overwriteResource));
					TestSubject.assertTestCondition(overwriteResource.equals(testRef.getCachedResource(testClass)), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler and "
									+ "refreshing the cache." ,
									testRef, overwriteResource, testRef.getCachedResource(), testResource, 
									overwriteResource));
				} catch(ReferenceException e) {
					throw new TestFailureException(e);
				}
			} { // test wrong type
				OriginHandler testHandler = new OriginHandler();
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				testHandler.requestAdd(testOrigin);
				TestResource testResource = new TestResource();
				Class<DifferentTestResource> failureClass = DifferentTestResource.class;
				testOrigin.requestAdd(testResource);
				CachedReference testRef = testResource.getCachedReference();
				// test before caching
				TestSubject.assertTestCondition(testRef.getCachedResource() == null, 
						String.format("The cached reference %s should not have any cached resource "
								+ "upon initialisation, but it has cached %s." ,
								testRef, testRef.getCachedResource()));
				try {
					TestSubject.assertTestCondition(testRef.getCachedResource(failureClass) == null, 
							String.format("The cached reference %s should not have any cached resource "
									+ "of class %s upon initialisation, but it has cached %s." ,
									testRef, TestResource.class, testRef.getCachedResource()));
				} catch (ReferenceException e1) {
					throw new TestFailureException(e1);
				}
				// test after caching
				testRef.cache(testHandler);
				TestSubject.assertTestCondition(testResource.equals(testRef.getCachedResource()), 
						String.format("The cached reference %s should have cached the resource %s"
								+ "upon request, but it has cached %s." ,
								testRef, testResource, testRef.getCachedResource()));
				try {
					Resource failureResource = (Resource) testRef.getCachedResource(failureClass);
					throw new TestFailureException(String.format("Caching reference %s should fail as "
							+ "the cached resource %s is of type %s, but type %s is expected, however "
							+ "%s was retrieved.", 
							testRef, testResource, testResource.getClass(), failureClass, failureResource));
				} catch (ReferenceException e1) {
					/*
					 * Do nothing as this is expected behaviour.
					 */
				}
			}
		} { // test null class
			try {
				CachedReference nullRef = CachedReferenceTesting.createRandomReference();
				nullRef.getCachedResource(null);
				throw new TestFailureException(String.format("Caching reference %s should fail as "
						+ "the handler responsible for caching is null.", nullRef));
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			} catch (ReferenceException e) {
				throw new TestFailureException(e);
			}
		}
	}
	
	/**
	 * Test caching and retrieving the cached resources.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetResource() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test valid caching
				try {
					OriginHandler testHandler = new OriginHandler();
					Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
					testHandler.requestAdd(testOrigin);
					TestResource testResource = new TestResource();
					Class<?> testClass = testResource.getClass();
					testOrigin.requestAdd(testResource);
					CachedReference testRef = testResource.getCachedReference();
					// test caching and retrieval
					TestSubject.assertTestCondition(testResource.equals(testRef.getResource(testHandler)), 
							String.format("The cached reference %s should cache and return the resource %s"
									+ "upon request, but it has returend %s." ,
									testRef, testResource, testRef.getResource(testHandler)));
					TestSubject.assertTestCondition(testResource.equals(testRef.getResource(testHandler, testClass)), 
							String.format("The cached reference %s should cache and return the resource %s"
									+ "upon request, but it has returend %s." ,
									testRef, testResource, testRef.getResource(testHandler, testClass)));
					// test overwriting the cached resource
					TestResource overwriteResource = new TestResource(testResource.getID());
					testOrigin.requestAdd(overwriteResource); // overwrite the original testing resource
					TestSubject.assertTestCondition(testResource.equals(testRef.getResource(testHandler)), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler." ,
									testRef, testResource,testRef.getResource(testHandler), testResource, 
									overwriteResource));
					TestSubject.assertTestCondition(testResource.equals(testRef.getResource(testHandler, testClass)), 
							String.format("The cached reference %s should have cached the resource %s"
									+ "upon request, but it has cached %s after changing " 
									+ "the referenced resource from %s to %s in the handler." ,
									testRef, testResource, testRef.getResource(testHandler, testClass), testResource, 
									overwriteResource));
				} catch(ReferenceException e) {
					throw new TestFailureException(e);
				}
			} { // test wrong type
				OriginHandler testHandler = new OriginHandler();
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				testHandler.requestAdd(testOrigin);
				TestResource testResource = new TestResource();
				Class<DifferentTestResource> failureClass = DifferentTestResource.class;
				testOrigin.requestAdd(testResource);
				CachedReference testRef = testResource.getCachedReference();
				try {
					Resource failureResource = (Resource) testRef.getResource(testHandler, failureClass);
					throw new TestFailureException(String.format("Caching reference %s should fail as "
							+ "the cached resource %s is of type %s, but type %s is expected, however "
							+ "%s was retrieved.", 
							testRef, testResource, testResource.getClass(), failureClass, failureResource));
				} catch (ReferenceException e1) {
					/*
					 * Do nothing as this is expected behaviour.
					 */
				}
			}
		} { // test null class
			try {
				OriginHandler nullHandler = new OriginHandler();
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				nullHandler.requestAdd(testOrigin);
				TestResource testResource = new TestResource();
				testOrigin.requestAdd(testResource);
				CachedReference nullRef = testResource.getCachedReference();
				nullRef.getResource(nullHandler, null);
				throw new TestFailureException(String.format("Caching reference %s should fail as "
						+ "the class expected is null.", nullRef));
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			} catch (ReferenceException e) {
				throw new TestFailureException(e);
			}
		} { // test null handler
			
				Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
				TestResource testResource = new TestResource();
				testOrigin.requestAdd(testResource);
				CachedReference nullRef = testResource.getCachedReference();
			try {
				nullRef.getResource(null);
				throw new TestFailureException(String.format("Caching reference %s should fail as "
						+ "the handler required for caching is null.", nullRef));
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			}
			try {
				nullRef.getResource(null, testResource.getClass());
				throw new TestFailureException(String.format("Caching reference %s should fail as "
						+ "the handler required for caching is null.", nullRef));
			} catch (NullPointerException e) {
				/*
				 * Do nothing as this is expected behaviour.
				 */
			} catch (ReferenceException e) {
				throw new TestFailureException(e);
			}
		}
	}
	
	/**
	 * Test generating cached references from standard references.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testgenerateCachedReference() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			OriginHandler testHandler = new OriginHandler();
			Origin testOrigin = CachedReferenceTesting.createRandomOrigin();
			testHandler.requestAdd(testOrigin);
			TestResource testResource = new TestResource();
			testOrigin.requestAdd(testResource);
			CachedReference testCachedRef = testResource.getCachedReference();
			ResourceReference refToTransform = testResource.getReference();
			CachedReference generatedCachedRef = CachedReference.generateCachedReference(refToTransform);
			// test caching and retrieval
			TestSubject.assertTestCondition(generatedCachedRef.equals(testCachedRef), 
					String.format("The cached reference %s generated from reference %s "
							+ "should be equal to %s." ,
							generatedCachedRef, refToTransform, testCachedRef));
		}
		// test null
		CachedReference nullRef = CachedReference.generateCachedReference(null);
		TestSubject.assertTestCondition(nullRef == null, String.format("Generating a cached "
				+ "resource from null should return null, but returned %s instead.", 
				null, null, nullRef));
	}
	
	/**
	 * Create a random resource reference.
	 * 
	 * @return a random reference
	 */
	private static CachedReference createRandomReference() {
		return new CachedReference(TestRunnerWrapper.createRandomString(), 
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
