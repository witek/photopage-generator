package photo.page;

import ilarkesto.base.Sys;
import ilarkesto.core.base.Str;
import ilarkesto.io.IO;
import ilarkesto.properties.APropertiesStore;

import java.io.File;
import java.util.Properties;

public class Project extends APropertiesStore {

	private File globalPropertiesFile;
	private Properties globalProperties;

	private File dir;

	public Project() {
		globalPropertiesFile = new File(Sys.getUsersHomePath() + "/.photopage-generator.properties");
		globalProperties = globalPropertiesFile.exists() ? IO.loadProperties(globalPropertiesFile, IO.UTF_8)
				: new Properties();
		String lastProjectPath = globalProperties.getProperty("project.dir");
		if (!Str.isBlank(lastProjectPath)) dir = new File(lastProjectPath);
	}

	@Override
	protected Properties load() {
		File file = getPropertiesFile();
		if (file == null || !file.exists()) return new Properties();
		return IO.loadProperties(file, IO.UTF_8);
	}

	@Override
	protected void save(Properties properties) {
		IO.saveProperties(properties, "photopage-generator project", getPropertiesFile());
	}

	public File getPropertiesFile() {
		if (dir == null) return null;
		return new File(dir.getPath() + "/photopage-generator.properties");
	}

	public String getGoogleAnalyticsId() {
		return get("google-analytics-id");
	}

	public void setGoogleAnalyticsId(String googleAnalyticsId) {
		set("google-analytics-id", googleAnalyticsId);
	}

	public File getDir() {
		return getFile("dir");
	}

	public void setDir(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			throw new IllegalStateException("Directory does not exist: " + dir.getAbsolutePath());
		this.dir = dir;
		set("dir", dir);
		globalProperties.setProperty("project.dir", dir.getAbsolutePath());
		IO.saveProperties(globalProperties, "photopage-generator global configuration file", globalPropertiesFile);
	}

	public String getTitle() {
		return get("title");
	}

	public void setTitle(String title) {
		set("title", title);
	}

}
