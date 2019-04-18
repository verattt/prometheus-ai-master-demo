package nn;

/*
This exception is used to tell the drone that it has crashed into an object that it cannot pass through (ie a wall or another drone)
This object is thrown by the Drone's world representation to the drone
*/
public class crashException extends Exception {


	public crashException(String message) {
		super(message);
	}

	public crashException(String message, Throwable throwable) {
	    super(message, throwable);
	}

}
