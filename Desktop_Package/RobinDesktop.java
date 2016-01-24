// launches batch file
// outputs ngrok URL
// has a gui

import javax.swing.*;
import java.awt.*;
import java.lang.Process;
import java.io.*;

// @TODO: have user enter path!

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

			// get path
			String path = System.getProperty("user.dir");
			System.out.println("Current directory: " + path);


			// give permission to run
			String chmodCommand = "chmod u+x " + path + "/script.tool";
			runtime.exec(chmodCommand);

			// run it
			String runCommand = path + "/script.tool";
			Process tunneling = runtime.exec(runCommand);


			// get output, look for ngrok url, output that cheese to the usr
			InputStream in = tunneling.getInputStream();

			String sb = " ";
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String read;

			while((read = br.readLine()) != null) {
			    //System.out.println(read);
			    sb += read + "\n";   
			}

			br.close();
			System.out.println("Output: " + sb);


		}
		catch (Exception e) {
			e.printStackTrace();
			textfield.setText("Check your path and try again!");
		}

	}

	public static void main(String[] args) {

		RobinDesktop desktop = new RobinDesktop();
		System.out.println("Hello World");

	} // end of main

} // end of class
