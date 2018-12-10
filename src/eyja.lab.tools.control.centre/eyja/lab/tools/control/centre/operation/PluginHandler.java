package eyja.lab.tools.control.centre.operation;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

public class PluginHandler {

	public static final File PLUGIN_FOLDER = new File(".");
	private static HashMap<String, Initialiser> plugins = new HashMap<String, Initialiser>();
	
	public static void scan() {
		PluginHandler.plugins.clear();
		PluginHandler.PLUGIN_FOLDER.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File f) {
				if (f != null) {
					return f.getName().trim().toLowerCase().endsWith(".jar");
				}
				return false;
			}
			
		});
	}
	
}
