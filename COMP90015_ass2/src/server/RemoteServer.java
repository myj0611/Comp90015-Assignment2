package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import remote.RemoteInterface;

/**
 * @author Sebastian Yan
 * @date 23/09/2019
 */
public class RemoteServer {
	
	
	/**
	 * Define port number
	 */
	private String port;
	public RemoteInterface remoteMethods;

	
	/**
	 * Request the port number
	 */
	public RemoteServer() {	
		try {
			// Request the port number
			Scanner input = new Scanner(System.in);
			System.out.println("Please type port number:");
			this.port = input.nextLine().toString();
					
			// Use regular expression to verify the port number
			Pattern pattern = Pattern.compile("[1-9][0-9]*");
			Matcher isNum = pattern.matcher(port);
			while(!isNum.matches() || Integer.parseInt(port) >= 65535 ){
				System.out.println("Please type a valid port number:");		
				this.port = input.nextLine().toString();			
				isNum = pattern.matcher(port);
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "What are you doing? I will shut down the program now.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	/**
	 * Request the port number
	 */
	public boolean initiateRMI() {	
		try {
			// Create the interface of remote service
			// Note: RMI is multi-threaded. What needs to be done is to guarantee the object is thread-safe.
			remoteMethods = new RemoteImplementation();
			
			// Create the registry (localhost with specified port number) and publish the remote object's stub in the registry under the name "RemoteOperation"
			Registry registry = LocateRegistry.getRegistry(Integer.parseInt(this.port));
			registry.rebind("RemoteOperation", remoteMethods);
			

			// If there is no exception
			return true;
			
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	public static void main(String[] args) {
		
		// Create the server instance
		RemoteServer remoteServer = new RemoteServer();
		
		try {
			// Initiate the server, including establishing server socket and RMI
			boolean isInitiated = remoteServer.initiateRMI();
			
			// Check whether there is no error of building RMI and server socket
			if(isInitiated) {
				System.out.println("RMI initiated. Waiting for connection...");
				// Keep server running
				while(true);
			}
			else {
				System.out.println("RMI initiation failed. Please check your RMI status and socket settings.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println( "RMI initiation failed.");
		}
		
	}
}
