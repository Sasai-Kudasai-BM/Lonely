package net.skds.core.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JarFileReader {

	private static final ClassLoader cl = JarFileReader.class.getClassLoader();

	public final String parent;
	public final List<String> files;

	public JarFileReader(String path) {
		this.parent = path;
		this.files = new ArrayList<>();
	}

	private JarFileReader(String path, List<String> files) {
		this.parent = path;
		this.files = files;
	}

	public void read() throws IOException {
		BufferedInputStream bis = new BufferedInputStream(cl.getResourceAsStream(parent));

		int len = bis.available();
		byte[] bytes = new byte[len];
		bis.read(bytes);
		bis.close();
		String readed = new String(bytes);

		for (String file : readed.split("\n")) {
			if (file.isEmpty()) {
				continue;
			}
			String newPath = parent + "/" + file;
			if (file.contains(".")) {
				files.add(newPath);
			} else {
				JarFileReader newReader = new JarFileReader(newPath, files);
				newReader.read();
			}
		}

	}
}
