package photo.page;

import ilarkesto.swing.ExceptionPanel;
import ilarkesto.swing.FileField;
import ilarkesto.swing.PanelBuilder;
import ilarkesto.swing.Swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Gui {

	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.show();
	}

	Project project = new Project();

	private JFrame frame;
	private FileField fileField;
	private JTextField titleField;
	private JButton generateButton;

	public Gui() {
		generateButton = new JButton("Generate photo album");
		generateButton.addActionListener(new GenerateActionListener());

		fileField = FileField.createForDirectory();
		fileField.addFileSelectionListener(new FileSelectedListener());

		titleField = new JTextField();

		PanelBuilder pb = new PanelBuilder();
		pb.setBorder(new EmptyBorder(5, 5, 5, 5));
		pb.setDefaultAnchorToNorthWest();
		pb.setDefaultPadding(2, 2, 2, 2);
		pb.setDefaultFillToHorizontal();
		pb.addLn("Photo directory", fileField);
		pb.addLn("Title", titleField);
		pb.addLn("", generateButton);

		frame = new JFrame("Photo page generator");
		frame.add(pb.toPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		updateFields();
	}

	public void updateFields() {
		fileField.setFile(project.getDir());
		titleField.setText(project.getTitle());
	}

	public void storeFields() {
		project.setDir(fileField.getFile());
		project.setTitle(titleField.getText());
	}

	public void generate() {
		storeFields();
		try {
			Generator.generate(project);
		} catch (Throwable ex) {
			ExceptionPanel.showErrorDialog(frame, ex);
		}
	}

	public void show() {
		frame.pack();
		Swing.center(frame);
		frame.setVisible(true);
	}

	private class FileSelectedListener implements FileField.FileSelectionListener {

		@Override
		public void onFileSelected(File file) {
			if (Swing.isBlank(titleField)) titleField.setText(file.getName());
		}
	}

	private class GenerateActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			generate();
		}
	}

}
