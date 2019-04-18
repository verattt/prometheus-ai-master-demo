package meta.SimulatorInterface;

public class InterfaceToMETA {

	
	public static void goForward(double distance) {
		TcpServer.FORWARD = distance;
	}
	
	public static void rotate(double degrees) {
		TcpServer.ROTATE = degrees;
	}
	
	public static String getPosition() {
		return TcpClient.LASTUPDATEDPOSITION;
	}
	
	public static String getRotationRobot() {
		return TcpClient.LASTUPDATEDROTATION;
	}
	
	public static String objectDetected() {
		return TcpClient.LASTUPDATEDSENSORDATA;
	}
	
	public static void stop() {
		TcpServer.STOP = true;
	}
	
}