package application;

import client.EncryptionClient;

public class Application {

	private static String KEY = "thisIsASecretKey";
	private static Integer PORT = 1337;
	private static boolean bRunAsServer = true;
	
	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("No key given as argument. Using standard key '"+KEY+"'");
			
		}else{
			if (args[0].toLowerCase().equals("client")){
				bRunAsServer = false;
			}else{
				KEY = args[0];
			}
		}
		
		if (args.length > 1){
			if (args[1].equals("client")){
				bRunAsServer = false;
			}
		}
		
		if (KEY.length() != 16){
			System.out.println("Wrong key length: Needed length 16.");
			System.exit(-1);
		}
		
		new EncryptionClient(KEY, PORT, bRunAsServer);
		
	}
	
}
