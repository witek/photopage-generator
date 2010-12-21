/*
 * Copyright 2010 Witoslaw Koczewski
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with Foobar. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package photo.page;

import ilarkesto.core.logging.Log;
import ilarkesto.io.IO;
import ilarkesto.ui.web.HtmlRenderer;
import ilarkesto.ui.web.HtmlRenderer.Tag;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Generator {

	private static Log log = Log.get(Generator.class);

	private static File pageFile;
	private static File[] photoFiles;

	public static void generate(Project project) throws IOException {

		File photosDir = project.getDir();
		if (!photosDir.exists()) throw new RuntimeException("Directory does not exist: " + photosDir.getPath());
		project.setDir(photosDir);

		String title = System.getProperty("title", photosDir.getName());
		project.setTitle(title);

		photoFiles = photosDir.listFiles(new PhotoFileFilter());
		pageFile = new File(photosDir.getPath() + "/index.html");
		generateScaled(project.getDir(), "thumbs", 100);
		generateScaled(project.getDir(), "pictures", 500);
		writePage(project);
		copyResources(project.getDir());
	}

	private static void writePage(Project project) throws IOException {
		log.info("Writing HTML page:", pageFile);
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pageFile)));
		HtmlRenderer html = new HtmlRenderer(out, IO.UTF_8);
		html.startHTML();
		html.startHEAD(project.getTitle(), "de");
		html.LINKcss("resources/screen.css");
		html.SCRIPTdojo();
		html.SCRIPTjavascript("resources/activator.js", null);
		html.endHEAD();
		html.startBODY();
		writeBoard(html);
		writePictures(html);
		html.googleAnalytics(project.getGoogleAnalyticsId());
		html.endBODY();
		html.endHTML();
		html.flush();
		out.close();
	}

	private static void writePictures(HtmlRenderer html) {
		html.startDIV().setId("picture");
		html.endDIV();
		html.startDIV().setId("hidden-pictures");
		for (int i = 0; i < photoFiles.length; i++) {
			File file = photoFiles[i];
			File prev = i == 0 ? photoFiles[photoFiles.length - 1] : photoFiles[i - 1];
			File next = i + 1 >= photoFiles.length ? photoFiles[0] : photoFiles[i + 1];
			writePicture(html, file, prev, next);
		}
		html.endDIV();
	}

	private static void writePicture(HtmlRenderer html, File file, File previousFile, File nextFile) {
		html.startDIV().setId("img-" + file.getName());
		html.startTABLE(null, 0, 0, 0).setWidth("100%");
		html.startTR();
		html.startTD().setAlign("center").setValign("middle").setWidth(750);
		html.startDIV("img-container");
		html.IMG("resources/pictures/" + file.getName(), "Picture", null, null, null, 500);
		html.endDIV();
		html.endTD();
		html.startTD().setAlign("right").setValign("center");
		html.startDIV("navig-toolbar");
		imageHref(html, "back", "javascript:showBoard()", "back to overview");
		html.BR();
		html.BR();
		html.BR();
		imageHref(html, "left", "javascript:showPicture('" + previousFile.getName() + "')", "previous picture");
		html.BR();
		html.BR();
		imageHref(html, "right", "javascript:showPicture('" + nextFile.getName() + "')", "next picutre");
		html.BR();
		html.BR();
		html.BR();
		imageHref(html, "download", file.getName(), "download high resolution picture");
		html.endDIV();
		html.endTD();
		html.endTR();
		html.endTABLE();
		html.endDIV();
	}

	private static void imageHref(HtmlRenderer html, String image, String href, String tooltip) {
		Tag a = html.startA(href);
		if (!href.startsWith("javascript:")) a.setTargetBlank();
		html.IMG("resources/" + image + ".png", tooltip, null, null, 32, 32);
		html.endA();
	}

	private static void writeBoard(HtmlRenderer html) {
		html.startDIV().setId("board");
		for (File file : photoFiles) {
			html.startDIV("photo");
			html.startA("javascript:showPicture('" + file.getName() + "')");
			html.IMG("resources/thumbs/" + file.getName(), "Photo", null, null, null, 100);
			html.endA();
			html.endDIV();
		}
		html.endDIV();
	}

	private static void generateScaled(File dir, String name, int height) throws IOException {
		log.info("Creating", name);
		for (File file : photoFiles) {
			log.info("   ", file.getName());
			BufferedImage image = IO.loadImage(file);
			Image thumb = IO.scaledToHeight(image, height);
			IO.writeImage(thumb, "jpg", dir.getPath() + "/resources/" + name + "/" + file.getName());
		}
	}

	private static void copyResources(File dir) {
		log.info("Copying resources");
		String destination = dir.getPath() + "/resources";
		IO.createDirectory(destination);
		IO.copyResource("screen.css", destination);
		IO.copyResource("activator.js", destination);
		IO.copyResource("back.png", destination);
		IO.copyResource("left.png", destination);
		IO.copyResource("right.png", destination);
		IO.copyResource("download.png", destination);
	}
}
