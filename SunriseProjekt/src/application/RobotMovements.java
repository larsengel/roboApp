package application;

import static com.kuka.roboticsAPI.motionModel.BasicMotions.lin;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.ptp;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.conditionModel.ForceCondition;
import com.kuka.roboticsAPI.executionModel.IFiredConditionInfo;
import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.roboticsAPI.motionModel.IMotionContainer;
import com.kuka.roboticsAPI.uiModel.ApplicationDialogType;
import com.kuka.roboticsAPI.uiModel.IApplicationUI;

public class RobotMovements {
	
	private Tool gripper;
	private float break_force = 15;
	private Frame robot_rest;
	private IApplicationUI appUI;

	RobotMovements(Tool _gripper, AbstractFrame _rest, IApplicationUI _appUI) {
		gripper = _gripper;
		robot_rest = _rest.copy();
		appUI = _appUI;
	}
	
	/**
	 * Move robot to rest position
	 */
	public void moveToRest() {
		savePtpMove(robot_rest);
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
		ForceCondition testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), break_force);
		IMotionContainer movement = gripper.getDefaultMotionFrame()
				.move(lin(destination)
						.breakWhen(testForceCondition)
						.setJointVelocityRel(0.5));
		IFiredConditionInfo firedCondInfo =	movement.getFiredBreakConditionInfo();
		if (firedCondInfo != null) {
			ThreadUtil.milliSleep(1000);
	        appUI.displayModalDialog(ApplicationDialogType.INFORMATION, "App Stopped...","Continue");
			saveLinMove(destination);
		}
	}
	/**
	 * PTP Move method, which stops when a specific force is reached
	 * 
	 * @param destination
	 */
	public void savePtpMove(AbstractFrame destination) {
		ForceCondition testForceCondition = ForceCondition.createSpatialForceCondition(gripper.getDefaultMotionFrame(), break_force);
		IMotionContainer movement = gripper.getDefaultMotionFrame()
				.move(ptp(destination)
						.breakWhen(testForceCondition)
						.setJointVelocityRel(0.5));
		IFiredConditionInfo firedCondInfo =	movement.getFiredBreakConditionInfo();
		if (firedCondInfo != null) {
			ThreadUtil.milliSleep(1000);
	        appUI.displayModalDialog(ApplicationDialogType.INFORMATION, "App Stopped...","Continue");
			savePtpMove(destination);
		}
	}
	public Frame getRest() {
		return robot_rest;
	}
}
