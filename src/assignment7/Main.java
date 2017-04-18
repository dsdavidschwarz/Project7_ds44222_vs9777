package assignment7;

public class Main {
	   public static void main(String args[]) { 
		   Server server = new Server(Integer.parseInt(args[0]));
		   server.addThread();
	   }
}
