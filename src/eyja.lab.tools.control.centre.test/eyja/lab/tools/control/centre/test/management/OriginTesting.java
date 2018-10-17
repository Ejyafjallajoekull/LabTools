package eyja.lab.tools.control.centre.test.management;

import java.io.File;
import java.util.Arrays;

import eyja.lab.tools.control.centre.management.Origin;
import eyja.lab.tools.control.centre.management.OriginDeserialiser;
import eyja.lab.tools.control.centre.management.OriginSerialiser;
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

	@Override
	public void runAllTests() throws TestFailureException {
		OriginTesting.testConstructors();
		OriginTesting.testGetting();
		OriginTesting.testGetResources();
		OriginTesting.testAddingResources();
//		OriginTesting.
//		OriginTesting.
//		OriginTesting.
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
			TestSubject.assertTestCondition(Arrays.equals(firstOrigin.getResources(), randomResources), 
					String.format("The origin %s should contain the resources %s, but contains %s.", 
							firstOrigin, Arrays.toString(randomResources), 
							Arrays.toString(firstOrigin.getResources())));
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
				boolean success = firstOrigin.requestAdd(randomResources[j]);
				TestSubject.assertTestCondition(randomResources[j].equals(firstOrigin.retrieve(currentID)), 
						String.format("The origin %s should yield resource %s when supplying key %s, "
								+ "but yields %s.", 
								firstOrigin, randomResources[j], currentID, 
								firstOrigin.retrieve(currentID)));
				TestSubject.assertTestCondition(success, 
						String.format("The resource %s should be added successfully to origin %s.", 
								randomResources[j], firstOrigin));
			}
//			boolean nullSuccess = firstOrigin.requestAdd(null);
//			TestSubject.assertTestCondition(!nullSuccess, 
//					String.format("The resource %s should not be added successfully to origin %s.", 
//							null, firstOrigin));
//			boolean nullSuccess = firstOrigin.requestAdd(null);
//			TestSubject.assertTestCondition(!nullSuccess, 
//					String.format("The resource %s should not be added successfully to origin %s.", 
//							null, firstOrigin));
		}
	}

	
	/**
	 * Create a random origin.
	 * 
	 * @return an origin
	 */
	private static Origin createRandomOrigin() {
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
			public void deserialise(byte[] originData, Origin originToBuild) {
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
			public byte[] serialise(Origin origin) {
				return null;
			}
			
		};
	}
	
	/**
	 * Create a random file.
	 * 
	 * @return a random file
	 */
	private static File createRandomFile() {
		return new File("LabToolsTestRunner/" + TestRunnerWrapper.RANDOM.nextInt());
	}

}
