package de.fh_kiel.cimtt.robotik;


import com.kuka.common.ThreadUtil;
import com.kuka.generated.ioAccess.DigitalOutIOGroup;
import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.geometricModel.Tool;

public class Gripper{
	public DigitalOutIOGroup DigOut;
	
	public Gripper(Controller controller, Tool tool){
		DigOut = new DigitalOutIOGroup(controller);
	}

	public void close(){
		DigOut.setGripperOpen(false);
		DigOut.setGripperClose(true);
		ThreadUtil.milliSleep(200);
	}
	
	public void open(){
		DigOut.setGripperClose(false);
		DigOut.setGripperOpen(true);
		ThreadUtil.milliSleep(200);
	}
}
