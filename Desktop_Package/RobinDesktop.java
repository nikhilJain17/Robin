// launches batch file
// outputs ngrok URL
// has a gui

import javax.swing.*;
import java.awt.*;


public class RobinDesktop {
	
	JFrame frame;
	JTextField textfield;

	// constructor
	public RobinDesktop() {

		frame = new JFrame("Robn");
		frame.setBounds(10, 10, 600, 600);
		frame.setPreferredSize(new Dimension(600, 600));


		textfield = new JTextField();

		frame.add(textfield);
		frame.show();

		executeBatchFile();

	} // end of constructors

	private void executeBatchFile() {

		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("/Users/nikhil/development/Robin/Desktop_Package/script.tool");
			// System.out.println("Free memory: " + runtime.freeMemory());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		RobinDesktop desktop = new RobinDesktop();
		System.out.println("Hello World");

	} // end of main

} // end of class
