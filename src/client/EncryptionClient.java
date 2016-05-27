package client;

import java.io.IOException;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



public class EncryptionClient {

	private String key;
	private Integer port;
	
	private Socket socket;
	
	public EncryptionClient(String key, Integer port, boolean bServer){
		this.key = key;
		this.port = port;
		if (bServer){
			startServer();
		}else{
			startClient();
		}
	}
	
	private void startServer(){
		outputMessage("Starting server, initializing socket.");
		try {
			ServerSocket socket = new ServerSocket(this.port);
			Socket other = socket.accept();
			outputMessage("Client connected: "+other);
			boolean running = true;
			while (running){
				outputMessage("Write something:");
				String input = readUserInput();
				outputMessage("Encrypting message: "+input);
				String inputEncrypted = encryptMessage(input);
				outputMessage("Sending encrypted message: "+inputEncrypted);
				sendSocket(other, inputEncrypted);
				outputMessage("Waiting for message.");
				String inMessage = readSocket(other);
				receivedMessage(inMessage);
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startClient(){
		outputMessage("Starting client. Connecting to server socket..");
		try {
			Socket other = new Socket("localhost", this.port);
			outputMessage("Connected to server.");
			boolean running = true;
			while (running){
				outputMessage("Waiting for message.");
				String inMessage = readSocket(other);
				receivedMessage(inMessage);
				outputMessage("Write something:");
				String input = readUserInput();
				outputMessage("Encrypting message: "+input);
				String inputEncrypted = encryptMessage(input);
				outputMessage("Sending encrypted message: "+inputEncrypted);
				sendSocket(other, inputEncrypted);
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	
	
	private String readUserInput(){
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		return input;
	}
	
	private String readSocket(Socket in){
		String message = "";
		  try {
			Scanner scanner  = new Scanner( in.getInputStream() );
			message = scanner.nextLine();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return message;

	}
	
	private void sendSocket(Socket out, String message){
		try {
			PrintWriter outStream = new PrintWriter( out.getOutputStream(), true );
			outStream.println(message);
			outStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private String encryptMessage(String msg){
		return msg;
	}
	
	private String decryptMessage(String secretMessage){
		return secretMessage;
	}
	
	
	private synchronized void receivedMessage(String message){
		outputMessage("Received message: "+message);
		String decrypted = decryptMessage(message);
		outputMessage("Received message decrypted: "+decrypted);		
	}
	
	private synchronized void outputMessage(String message){
		System.out.println(message);
	}
	
}
