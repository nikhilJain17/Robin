import java.net.*;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.io.*;

public class Server {
	
	 static class HandleInput implements Runnable {
		 
		  float x, y;
		  
		  public HandleInput(float x, float y) {
			  this.x = x;
			  this.y = y;
		  }

		@Override
		public void run() {
			try {
				moveMouse(x, y);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 
	 };
	
	public static void main(String[] args) throws Exception {
		

		  ServerSocket serverSocket = new ServerSocket(61474);
		  System.out.println("Server started on localhost:" + serverSocket.getLocalPort());
		  
		    
	      
	      // forever young
	      while (true) {
			  Socket clientSocket = null;

		    try {
		        clientSocket = serverSocket.accept();
		    } catch (IOException e) {
		        System.err.println("Accept failed.");
		        System.exit(1);
	        }
	      

	        // outputter
	        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	       
	        // input stuff
	        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        String line;
	        
	        // reinit them
			float getx = 0, gety = 0;

	        while ((line = in.readLine()) != null) {
	        	
//	        	System.out.println(line);
	        	
	        	// parse the line
	        	if (line.equals("break"))
	        		break;
	        	else if (line.contains("x: ")) { //first == 'x') {
	        		System.out.println("\n\nGot an X Value!\n\n");
	        		getx = Float.parseFloat(line.substring(3));
	        	}
	        	else if (line.contains("y: ")) { //first == 'y') {
	        		gety = Float.parseFloat(line.substring(3));
	        		System.out.println("\n\nGot a Y Value!\n\n");
	        	}
	        	
	        	// handle server input
	        	Runnable r = new HandleInput(getx, gety);
	        	new Thread(r).start();

	        	
	        	System.out.print(line + "\r\n");
	        }
	        
	        // output an ack and a response
	        out.println("HTTP/1.0 200 OK");
	        out.println("Content-Type: text/html");
	        out.println("Server: Bot");
	        out.println(""); // signals end of headers
	        out.println("<h1>I CAN SEE YOU</h1>");
	        out.flush();
	        
	        
	    } // end of while
	           
	        
	} // end of main
	
	
	
	private static void moveMouse(float x, float y) throws AWTException {
		
		float currentX = (float) MouseInfo.getPointerInfo().getLocation().getX();
		float currentY = (float) MouseInfo.getPointerInfo().getLocation().getY();
		
		int newX = (int) (currentX + (10 * x));
		int newY = (int) (currentY + (10 * y));
		
		System.out.println("Current Mouse: " + Float.toString(currentX) + ", " 
				+ currentY);
		System.out.println("New Mouse: " + Float.toString(newX) + ", "
				+ newY);
		
		
		Robot robot = new Robot();
		robot.mouseMove(newX, newY);
		
	}
	
	
}// end of class