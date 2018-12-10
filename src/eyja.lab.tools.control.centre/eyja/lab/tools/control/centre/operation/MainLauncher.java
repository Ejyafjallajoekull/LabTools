package eyja.lab.tools.control.centre.operation;

import eyja.lab.tools.control.centre.management.Project;

/**
 * The MainLauncher class starts up LabTools and its initialiser.
 * 
 * @author Planters
 *
 */
public class MainLauncher {
	
	private static Project currentProject = null;

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
