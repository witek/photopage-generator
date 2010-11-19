package photo.page;

import java.io.File;
import java.io.FilenameFilter;

public class PhotoFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		name = name.toLowerCase();
		if (name.endsWith(".jpg")) return true;
		if (name.endsWith(".jpeg")) return true;
		if (name.endsWith(".png")) return true;
		return false;
	}

}
