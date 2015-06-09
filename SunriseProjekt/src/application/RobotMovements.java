package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;

public class RobotMovements {
	
	private Tool gripper;
	private float break_force = 15;
	private ForceCondition testForceCondition;

	RobotMovements(Tool _gripper) {
		gripper = _gripper;
		testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), break_force);
	}
	
	/**
	 * Move near to a specific location
	 * 
	 * @param destination
	 * @param distance
	 */
	public void moveNear(AbstractFrame destination, double distance) {
		Frame temp = destination.copy();
		temp.setZ(temp.getZ() - distance);
		saveLinMove(temp);
	}
	
	/**
	 * Lin Move method, which stops when a specific force is reached
	 * 
	 * @param destination
	 */
	public void saveLinMove(AbstractFrame destination) {
		gripper.getDefaultMotionFrame().move(lin(destination).breakWhen(testForceCondition).setJointVelocityRel(0.75));
		
	}
	/**
	 * PTP Move method, which stops when a specific force is reached
	 * 
	 * @param destination
	 */
	public void savePtpMove(AbstractFrame destination) {
        gripper.getDefaultMotionFrame().move(ptp(destination).breakWhen(testForceCondition).setJointVelocityRel(0.75));
		
	}
}
