package application;



import application.mill.Controller.MainController;
import application.mill.Model.Buffer;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.DigitalOutIOGroup;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import com.kuka.roboticsAPI.applicationModel.tasks.RoboticsAPITask;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Tool;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * Test
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class YourApplication extends RoboticsAPIApplication {
	private Controller kuka_Sunrise_Cabinet_1;
	private Tool gripper;
	private LBR robot;
	DigitalOutIOGroup digitOut;
	private RobotMovements robot_movements;
	private RobotInteractions robot_interactions;
	public static BoardPoints board_points;
	public Logger logger;
	
	public void initialize() {
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		robot = (LBR) getRobot(kuka_Sunrise_Cabinet_1, "LBR_iiwa_7_R800_1");
		gripper = getApplicationData().createFromTemplate("Greifer");
		digitOut = new DigitalOutIOGroup(kuka_Sunrise_Cabinet_1);
		gripper.attachTo(robot.getFlange());
		robot_movements = new RobotMovements(gripper, getApplicationData().getFrame("/robot_rest"), getApplicationUI());	
		robot_interactions = new RobotInteractions(gripper, digitOut, robot_movements, getObserverManager(), getApplicationData().getFrame("/wink"));
		logger = new Logger(getLogger());
	}
	
	public void run() {
		
		board_points = new BoardPoints(getApplicationData().getFrame("/board_center"), 0, 
				getApplicationData().getFrame("/piece_origin"), 
				getApplicationData().getFrame("/piece_drop"));
		board_points.calculateBoard();	
		BoardPoints.nr_of_placed = 0;
		
		//modbus_client.getValue();
		
		//We declare a new Buffer
		Buffer<Integer[]> inputBuffer = new Buffer<Integer[]>();
		MainController gc = new MainController(inputBuffer, getApplicationUI(), robot_interactions);
		// Starting the game
		gc.startGame();
		
		while (gc.getGameStateMain() == null || !gc.getGameStateMain().equals("WIN") || !gc.getGameStateMain().equals("DRAW")) {
			ThreadUtil.milliSleep(1000);
		}
	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		YourApplication app = new YourApplication();
		app.runApplication();
	}
}
