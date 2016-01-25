// launches batch file
// outputs ngrok URL
// has a gui

import javax.swing.*;
import java.awt.*;
import java.lang.Process;
import java.io.*;
import java.net.*;


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

	// listen for javascript code to tell java app what the url is
	private void getURL() {
		
		final int PORT = 4445; // node server runs on 4444

		try {
		    
		    ServerSocket serverSocket = new ServerSocket(PORT);
		    
		    Socket clientSocket = serverSocket.accept();
		    
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		    
		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(clientSocket.getInputStream()));

		    // read the stuff
		    String inputLine, outputLine = "";

		    while ((inputLine = in.readLine()) != null) {
		        
		        // out.println(inputLine);
		        outputLine += inputLine;
		        if (outputLine.equals("Bye."))
		            break;
		    } // end of while

		    System.out.println("URL: " + outputLine);

		} 
		catch(Exception e) {
			e.printStackTrace();
		}

	} // end of getURL()

	public static void main(String[] args) {

		System.out.println("Hello World");
		
		RobinDesktop desktop = new RobinDesktop();
		desktop.getURL();

	} // end of main

} // end of class
