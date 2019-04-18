package meta.SimulatorInterface;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Random;

// code taken partly from Computer Networking: A top down approach
// and modified to use threading
public class TcpServer extends Thread {
	
	private boolean stopped = false;
	private ServerSocket serverSocket;
	private Socket socket;
	private Random rand;
	
	// ---------------- Parameters for transmission -------------------- //
	public static double FORWARD;
	public static double ROTATE;
	public static boolean STOP;
	
	public TcpServer(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		rand = new Random();
		FORWARD = 0;
		ROTATE = 0;
		STOP = false;
				
	}
	
	public void halt() {
		this.stopped = true;
	}
	


	public void run() {
		System.out.println("Server starts sending data");
		try {
			socket = serverSocket.accept();
			//PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			OutputStream os = socket.getOutputStream();
			//DataOutputStream dos = new DataOutputStream(os);
			
			
			while (true) {
				if (stopped) {
					return;
				}
				
				if (FORWARD != 0) {
					String line = "a=" + Double.toString(FORWARD);
					byte[] data = line.getBytes("UTF-8");
					os.write(data, 0, data.length);
					FORWARD = 0;
					
					String sent = new String(data, "UTF-8");
					System.out.print("Server sent: " + sent + "\n");
					
				} else if (ROTATE != 0) {
					String line = "r=" + Double.toString(ROTATE);
					byte[] data = line.getBytes("UTF-8");
					os.write(data, 0, data.length);
					ROTATE = 0;
					
					String sent = new String(data, "UTF-8");
					System.out.print("Server sent: " + sent + "\n");
					
				} else if (STOP){
					String line = "stop";
					byte[] data = line.getBytes("UTF-8");
					os.write(data, 0, data.length);
					STOP = false;
					
					String sent = new String(data, "UTF-8");
					System.out.print("Server sent: " + sent + "\n");
				}
				

				//os.flush();
				
				//out.println(line);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//out.close();
				//socket.close();

				
			}
			
		}
		catch (IOException ex) {
			System.err.println(ex);
			
		}
		
	}
		
	private String generateRandomNumber1to4()
	{
		int n = rand.nextInt(4) + 1;
		return Integer.toString(n);
	}
	
 
	
}