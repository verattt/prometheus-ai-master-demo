package meta.SimulatorInterface;

import java.util.Scanner;
import meta.Main;
public class UserInput extends Thread {
	
	private Scanner sc;

	public UserInput() {
		this.sc = new Scanner(System.in);
	}
	
	public void run() {
		while (true) {
			String command = sc.nextLine();
			
			try {
				if (command.equals("stop")) {
					InterfaceToMETA.stop();
				}
				else if (command.charAt(0) == 'r') {
					double rotation = Double.parseDouble(command.substring(2));
					InterfaceToMETA.rotate(rotation);
				}
				else if (command.charAt(0) == 'a') {
					double distance = Double.parseDouble(command.substring(2));
					InterfaceToMETA.goForward(distance);
				}else if(command.charAt(0) == 'o'){
//					double distance = Double.parseDouble(command.substring(2));
					System.out.println(InterfaceToMETA.objectDetected());
				}else if(command.charAt(0) == 't'){
//					double distance = Double.parseDouble(command.substring(2));
					System.out.println(InterfaceToMETA.getRotationRobot());
				}
				else if(command.charAt(0) == 'p'){
//					double distance = Double.parseDouble(command.substring(2));
					System.out.println(InterfaceToMETA.getPosition());
				}
				else if(command.charAt(0) == 'g'){
					String goals = command.substring(2);
//					Main.setGoal(goals);
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}