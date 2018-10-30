package eyja.lab.tools.control.centre.management;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * The Project class represents a Lab Tools project with all its specific origins as an enclosed 
 * environment with its own settings.
 * 
 * @author Planters
 *
 */
public class Project {
	
	private static final File PROJECT_FOLDER = new File("Projects");
	private static final String PROJECT_EXTENSION = ".ltp";
	private final File projectFile; // the file containing all settings for this origin
	private final File projectFolder; // the folder all project specific origins should be serialised to
	private final OriginHandler projectOrigins = new OriginHandler();
	
	public Project(String name) throws IOException {
		if (name != null && name.length() > 0) {
			File potentialProjectFolder = new File(PROJECT_FOLDER, name);
			if (!Arrays.stream(Project.PROJECT_FOLDER.listFiles()).anyMatch(potentialProjectFolder::equals)) {
				this.projectFolder = potentialProjectFolder;
				this.projectFile = new File(potentialProjectFolder, name + Project.PROJECT_EXTENSION);
			} else {
				throw new IOException(String.format("The requested project %s already exists.", potentialProjectFolder));
			}
		} else {
			throw new IllegalArgumentException(String.format("The name of a project cannot be %s.", name));
		}
	}
	
	/**
	 * Get the folder containing all project related information.
	 * 
	 * @return the project folder
	 */
	public File getProjectFolder() {
		return this.projectFolder;
	}
	
	/**
	 * Get the file this project is serialised to.
	 * 
	 * @return the project file
	 */
	public File getProjectFile() {
		return this.projectFile;
	}
	
	/**
	 * The origin handler of this project, managing all related origins.
	 * 
	 * @return the project's origin handler
	 */
	public OriginHandler getOriginHandler() {
		return this.projectOrigins;
	}
	
	/**
	 * Check if the specified origin is part of this project or an external resource.
	 * 
	 * @param origin - the origin to check
	 * @return true if the specified origin is contained by the project, false if it is an external
	 * resource
	 */
	public boolean isInternalOrigin(Origin origin) {
		if (origin != null && origin.getFile() != null) {
			return origin.getFile().getAbsolutePath().startsWith(this.projectFolder.getAbsolutePath());
		}
		return false;
	}
	
	/**
	 * Serialise the project to a file.
	 * 
	 * @throws IOException if the specified file could not be written to
	 */
	public void write() throws IOException {
		File writeLocation = this.getProjectFile();
		if (writeLocation != null) {
			// create the folder structure if needed
			File parentFolder = writeLocation.getParentFile();
			if (parentFolder != null && !parentFolder.exists()) {
				parentFolder.mkdirs();
			}
			try (DataOutputStream projectData = new DataOutputStream(
					new FileOutputStream(writeLocation, false))) {
				Origin[] allOrigins = this.getOriginHandler().getOrigins();
				projectData.writeInt(allOrigins.length);
				for (Origin o : allOrigins) {
					if (o != null && o.getFile() != null) { // check for valid origins
						String originPath = o.getFile().getPath();
						projectData.writeInt(originPath.length());
						projectData.writeUTF(originPath);
					} else { // mark origin as invalid
						projectData.writeInt(-1);
					}
				}
			}
		} else {
			throw new IOException("No file for writing has been specified.");
		}
	}
	
	/**
	 * Deserialise the origin and all its resources from a file.
	 * 
	 * @throws IOException if the file this origin represents does not exist
	 * @throws NullPointerException if the deserialiser of this origin is null
	 */
	public void read() throws IOException {
		File readLocation = this.getProjectFile();
		// only allow files that exist
		if (readLocation != null && readLocation.isFile()) {
			try (DataInputStream readData = new DataInputStream(new FileInputStream(readLocation))) {
				int numOrigins = readData.readInt();
				for (int i = 0; i < numOrigins; i++) {
					
				}
			}
		} else {
			throw new IOException(String.format("The file %s does not exist.", readLocation));
		}
	}
	
	/**
	 * Get the folder containing all projects.
	 * 
	 * @return the projects folder
	 */
	public static File getProjectsFolder() {
		return Project.PROJECT_FOLDER;
	}

}
