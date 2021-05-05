/*** Created By : Amit Barman ***/
import java.net.*;
import java.io.*;
import java.util.Vector;

public class Server implements Runnable{
	private static ServerSocket server_socket;
	private Socket socket;
	public static Vector<BufferedWriter> clint = new Vector<>(); // this vector storing number of client

	public Server(Socket socket){
		try{
			this.socket = socket;
		}catch(Exception e){ e.printStackTrace(); }
	}

	// creating and Overriding run method
	@Override
	public void run(){
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			clint.add(writer);
			while(true){
					String data = reader.readLine().trim();
					for(int i = 0; i < clint.size(); i++){
					BufferedWriter bw = (BufferedWriter)clint.get(i);
					bw.write(data);
					bw.write("\r\n");
					bw.flush();
				}
			}
		}catch(Exception e){ e.printStackTrace(); }
	}

	// main function
	public static void main(String args[]) throws Exception{
		server_socket = new ServerSocket(4444); // server conected through porn no 4444
		int clien_count = 1;
		System.out.println(" ==========================");
		System.out.println("|	   Server up       |");
		System.out.println(" ==========================");
		System.out.println("Waiting....");
		try{
			while(true){
				Socket soc = server_socket.accept();
				Server server = new Server(soc);
				Thread thread = new Thread(server); // creating thread and pass server socket
				thread.start();						// start the thread
				System.out.println(" client conected " + (clien_count++));
			}
		}catch(Exception e){ e.printStackTrace(); }
	}
}