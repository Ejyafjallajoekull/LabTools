package eyja.lab.tools.control.centre.operation;

import eyja.lab.tools.control.centre.management.Project;

/**
 * The Initialiser interface provides a way to initialise modules for specific projects.
 * 
 * @author Planters
 *
 */
public interface Initialiser {
		
	/**
	 * Initialise this module for the specified project.
	 * 
	 * @param project - the project to initialise the module for
	 */
	public void initialise(Project project);
	
	/**
	 * Get the plugin module descriptor of this initialiser, containing its name, version and a 
	 * description of its function.
	 * 
	 * @return the initialiser descriptor.
	 */
	public InitialiserDescriptor getDescriptor();

}
