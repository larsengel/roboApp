package application;



import application.mill.Controller.GameController.GameState;
import application.mill.Controller.MainController;
import application.mill.Interfaces.Token;
import application.mill.Model.Buffer;
import application.mill.Model.GameField;
import application.mill.Model.MyPlayerInterface;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.DigitalOutIOGroup;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
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
	private BoardPoints board_points;
	
	public void initialize() {
		kuka_Sunrise_Cabinet_1 = getController("KUKA_Sunrise_Cabinet_1");
		robot = (LBR) getRobot(kuka_Sunrise_Cabinet_1, "LBR_iiwa_7_R800_1");
		gripper = getApplicationData().createFromTemplate("Greifer");
		digitOut = new DigitalOutIOGroup(kuka_Sunrise_Cabinet_1);
		gripper.attachTo(robot.getFlange());
		robot_movements = new RobotMovements(gripper);	
		robot_interactions = new RobotInteractions(gripper, digitOut, robot_movements);
		
		}

	public void run() {
		
		/** You need those commands*/		
		//We declare a new Buffer
		Buffer<Integer[]> inputBuffer = new Buffer<Integer[]>();
		
		// 
		MainController gc = new MainController(inputBuffer, getApplicationUI());
		
		// Starting the game
		gc.startGame();
		
		board_points = new BoardPoints(getApplicationData().getFrame("/board_center"), 0);
		board_points.calculateBoard();
		
		for (Frame[] arr2: board_points.getPoints()) {
		    for (Frame val: arr2) {
		    	robot_movements.savePtpMove(val);
				ThreadUtil.milliSleep(00);
		    }		    	
		}		
		
		/*
        for (int i = 1; i <= 9; i++) {
    		ForceCondition testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), 15.0);
    		getObserverManager().waitFor(testForceCondition);
    		robot_interactions.getPart(getApplicationData().getFrame("/pointA"));
    		robot_interactions.putPart(getApplicationData().getFrame("/pointB"));
        }*/
        
        //robot_interactions.placePieces(getApplicationData().getFrame("/piece_origin"), getApplicationData().getFrame("/pointB"), getObserverManager());
		
		/** Those commands are just examples */

		// GameField field = new GameField();

		// We get the current field
		// field = gc.getGameField();

		// Token token = field.getToken(0, 0); // We get the Token on (0, 0)
		// Token = EMPTY or WHITE or BLACK

		// We write in the Buffer to give the human moves to the Artificial
		// Intelligence
		// inputBuffer.write(new Integer[] { 0, 0 }); // The human player played on
													// (0, 0)

		// Get the current player
		// MyPlayerInterface currentPlayer = gc.getCurrentPlayerMain();

		//	Token color = currentPlayer.getColor();
		// color = WHITE or BLACK

		// get GameState
		// GameState gameState = gc.getGameStateMain();
		// gameState = TAKE (only when mill) or PLACE (at the beginning of the
		// game) or MOVE (move form one position to an other)
		// or WIN (when someone won the game)
	}

	/**
	 * Auto-generated method stub. Do not modify the contents of this method.
	 */
	public static void main(String[] args) {
		YourApplication app = new YourApplication();
		app.runApplication();
	}
}
