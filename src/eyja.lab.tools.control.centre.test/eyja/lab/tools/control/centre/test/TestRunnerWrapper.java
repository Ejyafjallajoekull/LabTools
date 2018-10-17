package eyja.lab.tools.control.centre.test;


import java.util.ArrayList;
import java.util.Random;

import koro.sensei.tester.TestRunner;

/**
 * The TestRunnerWrapper class wraps the TestRunner classes main method from the Korosensei 
 * testing framework for easier execution. 
 * 
 * @author Planters
 *
 */
public class TestRunnerWrapper {
	
	public static final Random RANDOM = new Random();

	/**
	 * Passes the command line arguments to the TestRunners main function.
	 * 
	 * @param args - the command line arguments for the test runner
	 */
	public static void main(String[] args) {
		TestRunner.main(args);
	}
	
	/**
	 * Create a random string.
	 * 
	 * @return a random string
	 */
	public static String createRandomString() {
		byte[] randomBytes = new byte[TestRunnerWrapper.RANDOM.nextInt(300)];
		TestRunnerWrapper.RANDOM.nextBytes(randomBytes);
		return new String(randomBytes);
	}
	
	/**
	 * Create a random string list.
	 * 
	 * @return a random string list
	 */
	public static ArrayList<String> createRandomStringList() {
		ArrayList<String> randomList = new ArrayList<String>();
		int randomListSize = TestRunnerWrapper.RANDOM.nextInt(300);
		for (int i = 0; i < randomListSize; i++) {
			randomList.add(TestRunnerWrapper.createRandomString());
		}
		return randomList;
	}
	
	/**
	 * Create a random string array.
	 * 
	 * @return a random string array
	 */
	public static String[] createRandomStringArray() {
		String[] randomArray = new String[TestRunnerWrapper.RANDOM.nextInt(300)];
		for (int i = 0; i < randomArray.length; i++) {
			randomArray[i] = TestRunnerWrapper.createRandomString();
		}
		return randomArray;
	}

}
