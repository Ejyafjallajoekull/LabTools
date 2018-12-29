package eyja.lab.tools.control.centre.operation;

import java.io.File;
import java.util.logging.Logger;

import central.logging.functionality.Logging;

/**
 * The MainLogger class organises the logging of LabTools.
 * 
 * @author Planters
 *
 */
public final class MainLogger {

	private static final Logging LOG = new Logging(new File("Logs"), "LabTools_Log");

	/**
	 * Constructor preventing instantiation.
	 */
	private MainLogger() {
		throw new AssertionError("This class cannot be instantiated.");
	}
	
	/**
	 * Get the main logger responsible for LabTools.
	 * 
	 * @return the main logger
	 */
	public static Logger getMainLogger() {
		return MainLogger.LOG.getLog();
	}
	
}
