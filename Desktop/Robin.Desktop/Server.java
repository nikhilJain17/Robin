import java.net.*;
import java.io.*;

public class Server {
	
	public static void main(String[] args) throws Exception {
			
		  ServerSocket serverSocket = new ServerSocket(0);
	      
	        
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
	        
	        System.out.println("Somebody connected");

	        // outputter
	        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	       
	        // input stuff
	        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        String line;
	        while ((line = in.readLine()) != null) {
	          if (line.equals("break"))
	            break;
	          System.out.print(line + "\r\n");
	        }
	        
	        // output an ack and a response
	        out.println("HTTP/1.0 200 OK");
	        out.println("Content-Type: text/html");
	        out.println("Server: Bot");
	        out.println(""); // signals end of headers
	        out.println("<h1>I CAN SEE YOU</h1>");
	        out.flush();
	        
	        clientSocket.close();
	        
	    } // end of while
	           
	        
	} // end of main
	
	
}// end of class