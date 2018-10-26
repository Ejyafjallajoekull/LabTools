package eyja.lab.tools.control.centre.test.management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.OriginDeserialiser;
import eyja.lab.tools.control.centre.management.OriginSerialiser;
import eyja.lab.tools.control.centre.management.Resource;
import eyja.lab.tools.control.centre.management.ResourceID;
import eyja.lab.tools.control.centre.test.TestRunnerWrapper;
import koro.sensei.tester.TestFailureException;
import koro.sensei.tester.TestSubject;

/**
 * The OriginTesting class test the Origin class for correct functionality.
 * 
 * @author Planters
 *
 */
public class OriginTesting implements TestSubject {
	
	private static final String TEST_FOLDER = "LabToolsTestRunnerOrigin/";
	private static final Comparator<Resource> RESOURCE_COMPARATOR = new Comparator<Resource>() {

		@Override
		public int compare(Resource o1, Resource o2) {
			return (int) (o1.getID().getID() - o2.getID().getID());
		}
		
	};
	private static final OriginDeserialiser TEST_DESERIALISER = new OriginDeserialiser() {

		@Override
		public void deserialise(InputStream originData, Origin originToBuild) {
			try (DataInputStream dis = new DataInputStream(originData)) {
				while (dis.available() > 0) {
					originToBuild.requestAdd(new TestResource(new ResourceID(originToBuild, dis.readLong())));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	};
	private static final OriginSerialiser TEST_SERIALISER = new OriginSerialiser() {
		
		@Override
		public void serialise(OutputStream originData, Origin origin) {
			try (DataOutputStream dos = new DataOutputStream(originData)) {
				for (Resource r : origin.getResources()) {
					// Serialise all resources with even IDs.
					if (r != null && r.getID().getID() % 2 == 0) {
						dos.write(r.serialise());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	};

	@Override
	public void runAllTests() throws TestFailureException {
		OriginTesting.testConstructors();
		OriginTesting.testGetting();
		OriginTesting.testGetResources();
		OriginTesting.testAddingResources();
		OriginTesting.testRetrieve();
		OriginTesting.testRemove();
		OriginTesting.testRequestID();
		OriginTesting.testIO();
	}
	
	/**
	 * Test the constructors and some basic equality.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testConstructors() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// constructor without serialiser
			File randomFile = OriginTesting.createRandomFile();
			OriginDeserialiser randomDeserialiser = OriginTesting.createRandomDeserialiser();
			Origin firstOrigin = new Origin(randomFile, randomDeserialiser);
			Origin secondOrigin = new Origin(randomFile, randomDeserialiser);
			TestSubject.assertTestCondition(firstOrigin.equals(secondOrigin), 
					String.format("The origin %s should equal %s.", firstOrigin, secondOrigin));
			TestSubject.assertTestCondition(!firstOrigin.equals(null), 
					String.format("The origin %s should not equal %s.", firstOrigin, null));
			secondOrigin = new Origin(null, null);
			TestSubject.assertTestCondition(!firstOrigin.equals(secondOrigin), 
					String.format("The origin %s should not equal %s.", firstOrigin, secondOrigin));
			secondOrigin = new Origin(new File(randomFile.getPath() + "1"), OriginTesting.createRandomDeserialiser());
			TestSubject.assertTestCondition(!firstOrigin.equals(secondOrigin), 
					String.format("The origin %s should not equal %s.", firstOrigin, secondOrigin));
			// constructor with serialiser
			OriginSerialiser randomSerialiser = OriginTesting.createRandomSerialiser();
			firstOrigin = new Origin(randomFile, randomDeserialiser, randomSerialiser);
			secondOrigin = new Origin(randomFile, randomDeserialiser, randomSerialiser);
			TestSubject.assertTestCondition(firstOrigin.equals(secondOrigin), 
					String.format("The origin %s should equal %s.", firstOrigin, secondOrigin));
			TestSubject.assertTestCondition(!firstOrigin.equals(null), 
					String.format("The origin %s should not equal %s.", firstOrigin, null));
			secondOrigin = new Origin(randomFile, randomDeserialiser, null);
			TestSubject.assertTestCondition(!firstOrigin.equals(secondOrigin), 
					String.format("The origin %s should not equal %s.", firstOrigin, secondOrigin));
			secondOrigin = new Origin(new File(randomFile.getPath() + "1"), OriginTesting.createRandomDeserialiser(),
					OriginTesting.createRandomSerialiser());
			TestSubject.assertTestCondition(!firstOrigin.equals(secondOrigin), 
					String.format("The origin %s should not equal %s.", firstOrigin, secondOrigin));
		}
	}
	
	/**
	 * Test getting information about the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetting() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test without serialiser
			File randomFile = OriginTesting.createRandomFile();
			OriginDeserialiser randomDeserialiser = OriginTesting.createRandomDeserialiser();
			Origin twoOrigin = new Origin(randomFile, randomDeserialiser);
			// test file path
			TestSubject.assertTestCondition(twoOrigin.getFile().equals(randomFile), 
					String.format("The origin %s should be represented by file %s, but is "
							+ "represented by %s.", twoOrigin, randomFile, twoOrigin.getFile()));
			// test deserialiser
			TestSubject.assertTestCondition(twoOrigin.getDeserialiser().equals(randomDeserialiser), 
					String.format("The origin %s should be deserialised by %s, but is "
							+ "deserialised by %s.", twoOrigin, randomDeserialiser, 
							twoOrigin.getDeserialiser()));
			// test serialiser
			TestSubject.assertTestCondition(twoOrigin.getSerialiser() == null, 
					String.format("The origin %s should be serialised by %s, but is "
							+ "serialised by %s.", twoOrigin, null, 
							twoOrigin.getSerialiser()));
			// test with serialiser
			OriginSerialiser randomSerialiser = OriginTesting.createRandomSerialiser();
			Origin threeOrigin = new Origin(randomFile, randomDeserialiser, randomSerialiser);
			// test file path
			TestSubject.assertTestCondition(threeOrigin.getFile().equals(randomFile), 
					String.format("The origin %s should be represented by file %s, but is "
							+ "represented by %s.", threeOrigin, randomFile, threeOrigin.getFile()));
			// test deserialiser
			TestSubject.assertTestCondition(threeOrigin.getDeserialiser().equals(randomDeserialiser), 
					String.format("The origin %s should be deserialised by %s, but is "
							+ "deserialised by %s.", threeOrigin, randomDeserialiser, 
							threeOrigin.getDeserialiser()));
			// test serialiser
			TestSubject.assertTestCondition(threeOrigin.getSerialiser().equals(randomSerialiser), 
					String.format("The origin %s should be serialised by %s, but is "
							+ "serialised by %s.", threeOrigin, randomSerialiser, 
							threeOrigin.getSerialiser()));
			// test null
			Origin nullOrigin = new Origin(null, null, null);
			// test file path
			TestSubject.assertTestCondition(nullOrigin.getFile() == null, 
					String.format("The origin %s should be represented by file %s, but is "
							+ "represented by %s.", nullOrigin, null, nullOrigin.getFile()));
			// test deserialiser
			TestSubject.assertTestCondition(nullOrigin.getDeserialiser() == null, 
					String.format("The origin %s should be deserialised by %s, but is "
							+ "deserialised by %s.", nullOrigin, null, 
							nullOrigin.getDeserialiser()));
			// test serialiser
			TestSubject.assertTestCondition(nullOrigin.getSerialiser() == null, 
					String.format("The origin %s should be serialised by %s, but is "
							+ "serialised by %s.", nullOrigin, null, 
							nullOrigin.getSerialiser()));
		}
	}
	
	/**
	 * Test getting resources from the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testGetResources() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			Origin firstOrigin = OriginTesting.createRandomOrigin();
			int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
			TestResource[] randomResources = new TestResource[randomNumResource]; 
			for (int j = 0; j < randomNumResource; j++) {
				randomResources[j] = new TestResource(new ResourceID(firstOrigin, j));
				firstOrigin.requestAdd(randomResources[j]);
			}
			// Sort arrays so they are comparable.
			Arrays.sort(randomResources, OriginTesting.RESOURCE_COMPARATOR);
			Resource[] retrievedResources = firstOrigin.getResources();
			Arrays.sort(retrievedResources, OriginTesting.RESOURCE_COMPARATOR);
			TestSubject.assertTestCondition(Arrays.equals(retrievedResources, randomResources), 
					String.format("The origin %s should contain the resources %s, but contains %s.", 
							firstOrigin, Arrays.toString(randomResources), 
							Arrays.toString(retrievedResources)));
		}
	}
	
	/**
	 * Test adding resources to the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testAddingResources() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			Origin firstOrigin = OriginTesting.createRandomOrigin();
			int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
			TestResource[] randomResources = new TestResource[randomNumResource];
			// test random resources
			for (int j = 0; j < randomNumResource; j++) {
				ResourceID currentID = new ResourceID(firstOrigin, j);
				randomResources[j] = new TestResource(currentID);
				ResourceID assignedID = firstOrigin.requestAdd(randomResources[j]);
				// retrieve by explicit ID
				TestSubject.assertTestCondition(randomResources[j].equals(firstOrigin.retrieve(currentID)), 
						String.format("The origin %s should yield resource %s when supplying key %s, "
								+ "but yields %s.", 
								firstOrigin, randomResources[j], currentID, 
								firstOrigin.retrieve(currentID)));
				// retrieve by implicit ID
				TestSubject.assertTestCondition(randomResources[j].equals(firstOrigin.retrieve(currentID.getID())), 
						String.format("The origin %s should yield resource %s when supplying key %s, "
								+ "but yields %s.", 
								firstOrigin, randomResources[j], currentID, 
								firstOrigin.retrieve(currentID)));
				TestSubject.assertTestCondition(currentID.equals(assignedID), 
						String.format("The resource %s should be added successfully to origin %s with ID "
								+ "%s, but has been added with ID %s.", 
								randomResources[j], firstOrigin, currentID, assignedID));
			}
			// test null
			ResourceID nullID = firstOrigin.requestAdd(null);
			TestSubject.assertTestCondition(nullID == null, 
					String.format("The resource %s should not be added successfully to origin %s. The "
							+ "returned ID should be "
							+ "%s, but is %s.", 
							null, firstOrigin, null, nullID));
			// test wrong ID
			Resource wrongResource = new TestResource(new ResourceID(new Origin(new File(
					firstOrigin.getFile().toString() + TestRunnerWrapper.RANDOM.nextInt(500)),
					firstOrigin.getDeserialiser()), TestRunnerWrapper.RANDOM.nextLong()));
			ResourceID wrongID = firstOrigin.requestAdd(wrongResource);
			TestSubject.assertTestCondition(wrongID == null, 
					String.format("The resource %s should not be added successfully to origin %s. The "
							+ "returned ID should be "
							+ "%s, but is %s.", 
							wrongResource, firstOrigin, null, wrongID));
		}
	}
	
	/**
	 * Test getting resources from the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testRetrieve() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test adding resources and increment id
			Origin testOrigin = OriginTesting.createRandomOrigin();
			ResourceID randomID = new ResourceID(testOrigin, TestRunnerWrapper.RANDOM.nextLong());
			Resource testResource = new TestResource(randomID);
			testOrigin.requestAdd(testResource);
			Resource queryID = testOrigin.retrieve(randomID);
			Resource queryLong = testOrigin.retrieve(randomID.getID());
			TestSubject.assertTestCondition(testResource.equals(queryID), 
					String.format("For origin %s the resource %s retrieved with explicite ID %s "
							+ "should be %s.", 	testOrigin, queryID, randomID, testResource));
			TestSubject.assertTestCondition(queryID.equals(queryLong), 
					String.format("For origin %s the resource %s retrieved with explicite ID %s and "
							+ "resource %s retrived with implicite ID %s should be equal.", 
							testOrigin, queryID, randomID, queryLong, randomID.getID()));
			// test null
			Resource queryNull = testOrigin.retrieve(null);
			TestSubject.assertTestCondition(queryNull == null, 
					String.format("For origin %s the resource %s retrieved with explicite ID %s "
							+ "should be %s.", 	testOrigin, queryNull, null, null));
			// test ID from different origin
			Origin wrongOrigin = OriginTesting.createRandomOrigin();
			ResourceID wrongID = new ResourceID(wrongOrigin, TestRunnerWrapper.RANDOM.nextLong());
			try {
				testOrigin.retrieve(wrongID);
				throw new TestFailureException(String.format("Requesting the explicit ID %s of origin %s from origin "
						+ "%s should fail.", wrongID, wrongOrigin, testOrigin));
			} catch (IllegalArgumentException e) {
				// Do nothing as this is expected behaviour.
			}

		}
	}
	
	/**
	 * Test removing resources from the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testRemove() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test implicit removal
				Origin removeOrigin = OriginTesting.createRandomOrigin();
				// add some random resources
				int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumResource; j++) {
					removeOrigin.requestAdd(new TestResource());
				}
				Resource[] allResources = removeOrigin.getResources(); 
				Resource removeResource = new TestResource();
				ResourceID removeID = removeOrigin.requestAdd(removeResource);
				Resource removedResource = removeOrigin.remove(removeID.getID());
				TestSubject.assertTestCondition(removedResource == removeResource, 
						String.format("The resource %s removed from origin %s should be %s.", 
								removedResource, removeOrigin, removeResource));
				TestSubject.assertTestCondition(Arrays.equals(allResources, removeOrigin.getResources()), 
						String.format("The resources %s contained by origin %s should be %s "
								+ "after removal of %s.", Arrays.toString(removeOrigin.getResources()), 
								removeOrigin, Arrays.toString(allResources), removeID));
				// test returning null if not available
				Resource nonExistentResource = removeOrigin.remove(removeID.getID());
				TestSubject.assertTestCondition(nonExistentResource == null, 
						String.format("The resource %s removed from origin %s should be %s.", 
								nonExistentResource, removeOrigin, null));
			} { // test explicit removal
				Origin removeOrigin = OriginTesting.createRandomOrigin();
				// add some random resources
				int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumResource; j++) {
					removeOrigin.requestAdd(new TestResource());
				}
				Resource[] allResources = removeOrigin.getResources(); 
				Resource removeResource = new TestResource();
				ResourceID removeID = removeOrigin.requestAdd(removeResource);
				Resource removedResource = removeOrigin.remove(removeID);
				TestSubject.assertTestCondition(removedResource == removeResource, 
						String.format("The resource %s removed from origin %s should be %s.", 
								removedResource, removeOrigin, removeResource));
				TestSubject.assertTestCondition(Arrays.equals(allResources, removeOrigin.getResources()), 
						String.format("The resources %s contained by origin %s should be %s "
								+ "after removal of %s.", Arrays.toString(removeOrigin.getResources()), 
								removeOrigin, Arrays.toString(allResources), removeID));
				// test returning null if not available
				Resource nonExistentResource = removeOrigin.remove(removeID);
				TestSubject.assertTestCondition(nonExistentResource == null, 
						String.format("The resource %s removed from origin %s should be %s.", 
								nonExistentResource, removeOrigin, null));
			} { // test explicit removal with wrong origin
				Origin removeOrigin = OriginTesting.createRandomOrigin();
				Origin differentOrigin = OriginTesting.createRandomOrigin();
				ResourceID removeID = new ResourceID(differentOrigin, TestRunnerWrapper.RANDOM.nextLong());
				try {
					Resource removedResource = removeOrigin.remove(removeID);
					throw new TestFailureException(String.format(
							"Removing the ID %s should fail as it is from a different origin, but "
							+ "removed resource %s instead.", removeID, removedResource));
				} catch (IllegalArgumentException e) {
					// Do nothing as this is expected behaviour.
				}
			}

		}
	}
	
	/**
	 * Test getting new IDs from the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testRequestID() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			// test numeric increment
			Origin numericOrigin = OriginTesting.createRandomOrigin();
			long startingID = numericOrigin.requestID();
			long endID = startingID;
			// add some random resources
			int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
			for (int j = 0; j < randomNumResource; j++) {
				endID = (numericOrigin.requestAdd(new TestResource())).getID();
			}
			TestSubject.assertTestCondition(endID == startingID + randomNumResource, 
					String.format("For origin %s (ID: %s)the last ID issued should be %s instead of %s "
							+ "after adding %s resources.", 
							numericOrigin, startingID, startingID + randomNumResource, 
							endID, randomNumResource));
			// test adding resources and increment id
			Origin loadOrigin = OriginTesting.createRandomOrigin();
			ResourceID startingResource = new ResourceID(loadOrigin, TestRunnerWrapper.RANDOM.nextLong());
			loadOrigin.requestAdd(new TestResource(startingResource));
			long endIDLoad = startingResource.getID();
			// add some random resources
			int randomNumResourceLoad = TestRunnerWrapper.RANDOM.nextInt(300);
			for (int j = 0; j < randomNumResourceLoad; j++) {
				endIDLoad = (loadOrigin.requestAdd(new TestResource())).getID();
			}
			TestSubject.assertTestCondition(endIDLoad == startingResource.getID() + randomNumResourceLoad, 
					String.format("For origin %s (ID: %s)the last ID issued should be %s instead of %s "
							+ "after adding %s resources.", 
							loadOrigin, startingResource, startingResource.getID() + randomNumResourceLoad, 
							endIDLoad, randomNumResourceLoad));

		}
	}
	
	/**
	 * Test reading and writing the origin.
	 * 
	 * @throws TestFailureException the test did fail
	 */
	private static void testIO() throws TestFailureException {
		for (int i = 0; i < 10000; i++) {
			{ // test default serialiser
				Origin defaultOrigin = new Origin(OriginTesting.createRandomFile(), OriginTesting.TEST_DESERIALISER);
				// add some random resources
				int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumResource; j++) {
					defaultOrigin.requestAdd(new TestResource());
				}
				try {
					defaultOrigin.write();
					Origin readOrigin = new Origin(defaultOrigin.getFile(), defaultOrigin.getDeserialiser());
					readOrigin.read();
					TestSubject.assertTestCondition(readOrigin.equals(defaultOrigin), 
							String.format("Origin %s should equal the origin %s", readOrigin, defaultOrigin));
				} catch (IOException e) {
					e.printStackTrace();
					throw new TestFailureException(e);
				}
			} { // test custom serialiser
				Origin customOrigin = new Origin(OriginTesting.createRandomFile(), OriginTesting.TEST_DESERIALISER, 
						OriginTesting.TEST_SERIALISER);
				// add some random resources
				int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumResource; j++) {
					customOrigin.requestAdd(new TestResource());
				}
				try {
					customOrigin.write();
					// only accept resources with even IDs
					Origin evenOrigin = new Origin(customOrigin.getFile(), customOrigin.getDeserialiser(), 
							customOrigin.getSerialiser());
					for (Resource r : customOrigin.getResources()) {
						if (r.getID().getID() % 2 == 0) {
							evenOrigin.requestAdd(new TestResource(new ResourceID(evenOrigin, r.getID().getID())));
						}
					}
					Origin readOrigin = new Origin(customOrigin.getFile(), customOrigin.getDeserialiser(), 
							customOrigin.getSerialiser());
					readOrigin.read();
					TestSubject.assertTestCondition(readOrigin.equals(evenOrigin),
							String.format("Origin %s should equal the origin %s", readOrigin, evenOrigin));
				} catch (IOException e) {
					e.printStackTrace();
					throw new TestFailureException(e);
				}
			} { // test null deserialiser
				Origin nullDOrigin = new Origin(OriginTesting.createRandomFile(), null);
				// add some random resources
				int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumResource; j++) {
					nullDOrigin.requestAdd(new TestResource());
				}
				try {
					nullDOrigin.write(); // writing should still work even without deserialiser
					try {
						nullDOrigin.read();
						throw new TestFailureException(String.format(
								"Reading the origin %s without a deserialiser should fail.", nullDOrigin));
					} catch (NullPointerException npe) {
						// Do nothing as this is expected behaviour.
					}
				} catch (IOException e) {
					e.printStackTrace();
					throw new TestFailureException(e);
				}
			} { // test null file
				Origin nullFOrigin = new Origin(null, OriginTesting.TEST_DESERIALISER);
				// add some random resources
				int randomNumResource = TestRunnerWrapper.RANDOM.nextInt(300);
				for (int j = 0; j < randomNumResource; j++) {
					nullFOrigin.requestAdd(new TestResource());
				}
				try {
					nullFOrigin.write();
					throw new TestFailureException(String.format(
							"Writing the origin %s without a specified file should fail.", nullFOrigin));
				} catch (IOException e) {
					// Do nothing as this is expected behaviour.
				}
				try {
					nullFOrigin.read();
					throw new TestFailureException(String.format(
							"Reading the origin %s without a specified file should fail.", nullFOrigin));
				} catch (IOException e) {
					// Do nothing as this is expected behaviour.
				}
			}
		}
		// delete all created files and folders
		File testFolder = new File(OriginTesting.TEST_FOLDER);
		for (File f : testFolder.listFiles()) {
			f.delete();
		}
		testFolder.delete();
	}
	
	/**
	 * Create a random origin.
	 * 
	 * @return an origin
	 */
	@SuppressWarnings("exports")
	public static Origin createRandomOrigin() {
		if (TestRunnerWrapper.RANDOM.nextDouble() < 0.4) {
			return new Origin(OriginTesting.createRandomFile(), 
					OriginTesting.createRandomDeserialiser(), null);
		} else {
			return new Origin(OriginTesting.createRandomFile(), 
					OriginTesting.createRandomDeserialiser(), OriginTesting.createRandomSerialiser());
		}
	}
	
	/**
	 * Create a new deserialiser.
	 * 
	 * @return a new deserialiser
	 */
	private static OriginDeserialiser createRandomDeserialiser() {
		return new OriginDeserialiser() {
			
			@Override
			public void deserialise(InputStream originData, Origin originToBuild) {
				// Do nothing here.
			}
			
		};
	}
	
	/**
	 * Create a new serialiser or null.
	 * 
	 * @return a new serialiser or null
	 */
	private static OriginSerialiser createRandomSerialiser() {
		return new OriginSerialiser() {

			@Override
			public void serialise(OutputStream originData, Origin origin) {
				// Do nothing here.
			}
			
		};
	}
	
	/**
	 * Create a random file.
	 * 
	 * @return a random file
	 */
	private static File createRandomFile() {
		return new File(OriginTesting.TEST_FOLDER + TestRunnerWrapper.RANDOM.nextInt());
	}

}
