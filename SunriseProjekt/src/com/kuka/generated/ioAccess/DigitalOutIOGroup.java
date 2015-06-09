package com.kuka.generated.ioAccess;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.ioModel.AbstractIOGroup;
import com.kuka.roboticsAPI.ioModel.IOTypes;

/**
 * Automatically generated class to abstract I/O access to I/O group <b>DigitalOut</b>.<br>
 * <i>Please, do not modify!</i>
 * <p>
 * <b>I/O group description:</b><br>
 * ./.
 */
public class DigitalOutIOGroup extends AbstractIOGroup
{
	/**
	 * Constructor to create an instance of class 'DigitalOut'.<br>
	 * <i>This constructor is automatically generated. Please, do not modify!</i>
	 *
	 * @param controller
	 *            the controller, which has access to the I/O group 'DigitalOut'
	 */
	public DigitalOutIOGroup(Controller controller)
	{
		super(controller, "DigitalOut");

		addDigitalOutput("GripperOpen", IOTypes.BOOLEAN, 1);
		addDigitalOutput("GripperClose", IOTypes.BOOLEAN, 1);
	}

	/**
	 * Gets the value of the <b>digital output '<i>GripperOpen</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'GripperOpen'
	 */
	public boolean getGripperOpen()
	{
		return getBooleanIOValue("GripperOpen", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>GripperOpen</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'GripperOpen'
	 */
	public void setGripperOpen(java.lang.Boolean value)
	{
		setDigitalOutput("GripperOpen", value);
	}

	/**
	 * Gets the value of the <b>digital output '<i>GripperClose</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @return current value of the digital output 'GripperClose'
	 */
	public boolean getGripperClose()
	{
		return getBooleanIOValue("GripperClose", true);
	}

	/**
	 * Sets the value of the <b>digital output '<i>GripperClose</i>'</b>.<br>
	 * <i>This method is automatically generated. Please, do not modify!</i>
	 * <p>
	 * <b>I/O direction and type:</b><br>
	 * digital output
	 * <p>
	 * <b>User description of the I/O:</b><br>
	 * ./.
	 * <p>
	 * <b>Range of the I/O value:</b><br>
	 * [false; true]
	 *
	 * @param value
	 *            the value, which has to be written to the digital output 'GripperClose'
	 */
	public void setGripperClose(java.lang.Boolean value)
	{
		setDigitalOutput("GripperClose", value);
	}

}
