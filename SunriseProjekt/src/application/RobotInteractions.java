package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.linRel;

import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.DigitalOutIOGroup;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.conditionModel.ICondition;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;

public class RobotInteractions {
	private Tool gripper;
	private DigitalOutIOGroup digitOut;
	private RobotMovements robot_movements;

	
	RobotInteractions(Tool _gripper, DigitalOutIOGroup _digitOut, RobotMovements _robot_movements) {
		gripper = _gripper;
		digitOut = _digitOut;
		robot_movements = _robot_movements;
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
		ICondition testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), 7.5);
	    gripper.move(linRel(0.0, 0.0, 100.0).breakWhen(testForceCondition).setJointVelocityRel(0.1));
	    gripper.move(linRel(0.0, 0.0, -5).setJointVelocityRel(0.1));
	    close();
	    robot_movements.savePtpMove(destination);
	    gripper.move(linRel(0.0, 0.0, 100.0).breakWhen(testForceCondition).setJointVelocityRel(0.1));
	    gripper.move(linRel(0.0, 0.0, -5).setJointVelocityRel(0.1));
	    open();
	    robot_movements.moveToRest();
	}
}
