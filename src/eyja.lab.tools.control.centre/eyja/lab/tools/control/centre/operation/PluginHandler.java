package eyja.lab.tools.control.centre.operation;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;

public class PluginHandler {

	public static final File PLUGIN_FOLDER = new File("Plugins");
	private static final FileFilter JAR_FILTER = new FileFilter() {
		
		@Override
		public boolean accept(File f) {
			if (f != null) {
				return f.getName().trim().toLowerCase().endsWith(".jar");
			}
			return false;
		}
		
	};
	private static HashMap<String, Initialiser> plugins = new HashMap<String, Initialiser>();
	private static URLClassLoader pluginLoader = null;
	
	public static void scan() {
		PluginHandler.plugins.clear();
		URL[] pluginURLs = Arrays.stream(PluginHandler.PLUGIN_FOLDER.listFiles(PluginHandler.JAR_FILTER)).map(File::toURI)
		.map(uri -> {
			try {
				return uri.toURL();
			} catch (MalformedURLException e) {
				MainLogger.getMainLogger().log(Level.WARNING, 
						String.format("No plugin URL could be created from the URI \"%s\".", uri), e);
				return null;
			}
		}).toArray(URL[]::new);
		PluginHandler.pluginLoader = new URLClassLoader(pluginURLs);
		for (Initialiser init : ServiceLoader.load(Initialiser.class, PluginHandler.pluginLoader)) {
			System.out.println(init.getClass().getName());
			// TODO: something useful
		}
	}
	
}
