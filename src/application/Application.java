package application;

import client.EncryptionClient;

public class Application {

	private static String KEY = "DATSECUREKEY";
	private static Integer PORT = 1337;
	private static boolean bRunAsServer = true;
	
	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("No key given as argument. Using standard key '"+KEY+"'");
			
		}else{
			KEY = args[0];
		}
		
		if (args.length > 1){
			if (args[1].equals("client")){
				bRunAsServer = false;
			}
		}
		
		new EncryptionClient(KEY, PORT, bRunAsServer);
		
	}
	
}
