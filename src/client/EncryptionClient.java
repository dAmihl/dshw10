package client;

import java.io.IOException;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;



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
		Cipher cipher;
		String encryptedString ="";
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
		    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		    encryptedString = new String(Base64.getEncoder().encode(cipher.doFinal(msg.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return encryptedString;

	}
	
	private String decryptMessage(String secretMessage){
		try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.getDecoder().decode(secretMessage)));
            return decryptedString;
        }
        catch (Exception e)
        {
          e.printStackTrace();

        }
        return null;
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
