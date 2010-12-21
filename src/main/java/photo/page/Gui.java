package photo.page;

import ilarkesto.swing.FileField;
import ilarkesto.swing.Swing;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Gui {

	public static void main(String[] args) {
		Gui gui = new Gui();
		gui.show();
	}

	private JFrame frame;
	private FileField fileField;
	private JTextField titleField;

	public Gui() {
		fileField = new FileField();

		titleField = new JTextField();

		frame = new JFrame("Photo page generator");
		frame.add(fileField);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void show() {
		frame.pack();
		Swing.center(frame);
		frame.setVisible(true);
	}

}
