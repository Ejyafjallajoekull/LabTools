package eyja.lab.tools.control.centre.operation;

import eyja.lab.tools.control.centre.management.Project;

/**
 * The MainLauncher class starts up LabTools and its initialiser.
 * 
 * @author Planters
 *
 */
public final class MainLauncher {
	
	private static Project currentProject = null;

	/**
	 * Constructor preventing instantiation.
	 */
	private MainLauncher() {
		throw new AssertionError("This class cannot be instantiated.");
	}
	
	public static void main(String[] args) {

	}
	
	/**
	 * Get the currently loaded project.
	 * 
	 * @return the current project
	 */
	public static Project getCurrentProject() {
		return MainLauncher.currentProject;
	}

}
