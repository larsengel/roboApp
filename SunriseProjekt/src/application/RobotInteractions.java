package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.DigitalOutIOGroup;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.conditionModel.ObserverManager;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;

public class RobotInteractions {
	private Tool gripper;
	private DigitalOutIOGroup digitOut;
	private RobotMovements robot_movements;
	private ObserverManager observer_manager;
	private Frame wink;

	
	RobotInteractions(Tool _gripper, DigitalOutIOGroup _digitOut, RobotMovements _robot_movements, ObserverManager _observer_manager, AbstractFrame _wink) {
		gripper = _gripper;
		digitOut = _digitOut;
		robot_movements = _robot_movements;
		observer_manager = _observer_manager;
		wink = _wink.copy();
	}
	
	/**
	 * Method to close the gripper
	 */
	public void close() {
		digitOut.setGripperOpen(false);
		digitOut.setGripperClose(true);
		ThreadUtil.milliSleep(200);
	}
    
	/**
	 * Method to open the gripper
	 */
	public void open() {
		digitOut.setGripperClose(false);
		digitOut.setGripperOpen(true);
		ThreadUtil.milliSleep(200);
	}
	
	/**
	 * Method to move a game piece from one position to another
	 * 
	 * @param origin
	 * @param destination
	 */
	public void movePiece(AbstractFrame origin, AbstractFrame destination) {
		robot_movements.savePtpMove(origin);
		open();
		ICondition testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), 10);
	    gripper.move(linRel(0.0, 0.0, 100.0).breakWhen(testForceCondition).setJointVelocityRel(0.05));
	    gripper.move(linRel(0.0, 0.0, -2).setJointVelocityRel(0.1));
	    close();
	    gripper.move(linRel(0.0, 0.0, -50).setJointVelocityRel(0.07));
	    robot_movements.savePtpMove(destination);
		testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), 10);
	    gripper.move(linRel(0.0, 0.0, 100.0).breakWhen(testForceCondition).setJointVelocityRel(0.05));
	    gripper.move(linRel(0.0, 0.0, -2).setJointVelocityRel(0.07));
	    open();
	    gripper.move(linRel(0.0, 0.0, -50).setJointVelocityRel(0.07));
	    robot_movements.moveToRest();
	}
	
	public void waitforPlayerTouch() {
		ICondition testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), 20);

		observer_manager.waitFor(testForceCondition);
		ThreadUtil.milliSleep(500);

	}
	
	public void wink() {
		gripper.move(ptp(wink));
		robot_movements.moveToRest();
	}
}
