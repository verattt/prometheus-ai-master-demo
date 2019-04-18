package meta.SimulatorInterface;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class TcpClient extends Thread {
	
	private boolean stopped = false;
	private Socket socket;
	
	public static String LASTUPDATEDPOSITION;
	public static String LASTUPDATEDSENSORDATA;
	public static String LASTUPDATEDROTATION;
	
	public TcpClient(int port, String address) throws UnknownHostException, IOException {
		this.socket = new Socket(address, port);
		LASTUPDATEDPOSITION = "none";
		LASTUPDATEDSENSORDATA = "none";
		LASTUPDATEDROTATION = "none";
	}
	
	public void halt() {
		this.stopped = true;
	}

	public void run() {
		System.out.println("Client starts receiving data");
		try {
			InputStream in = socket.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			
				while(true) {
					
					byte[] received = new byte[1024];
					int count = 0;
					
					dis.read(received, 0, 1024);
					
					String receivedString = new String(received, "UTF-8");
					
					
					// ----- format received ------------ //
					// right now the string received is of the form
					// x,y,z,theta(car), distance(object detected), theta (object detected)
					
					// parse
					String[] parsedReceivedString = receivedString.split(";");
					
					// update both fields (we received sensor data + position)
					String tempPos = "";
					String tempSensor = "";
					String tempRotation = "";
					if (parsedReceivedString.length > 0) {
						for (int i = 0; i < parsedReceivedString.length; i++) {
							if (i < 3) {
								tempPos += parsedReceivedString[i] + ";";
							} else if (i == 3) {
								tempRotation = parsedReceivedString[i];
							} else {
								tempSensor += parsedReceivedString[i] + ";";
							}
						}
						if (tempPos.length() > 0) {
							LASTUPDATEDPOSITION = tempPos;
//							System.out.println("tempPos: " + tempPos);
						}
						if (tempSensor.length() > 0) {
							LASTUPDATEDSENSORDATA = tempSensor;
//							System.out.println("tempSensor: " + tempSensor);
						}
						if (tempRotation.length() > 0) {
							LASTUPDATEDROTATION = tempRotation;
//							System.out.println("tempRotation: " + tempRotation);
						}

					}

					try {
						Thread.sleep(20);
					} catch (InterruptedException interruptedE) {
						interruptedE.printStackTrace();
					}
				}
		} 
		catch (IOException ex) {
			System.err.println(ex);
				
		}
			
		
		
	}

	
}