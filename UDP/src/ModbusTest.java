import java.net.InetAddress;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ExceptionResponse;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;


public class ModbusTest {
	public static ReadMultipleRegistersResponse readRegisters(InetAddress addr, int port,
		      int register, int length) {
		    TCPMasterConnection connection = null;
		    ModbusTCPTransaction transaction = null; // the transaction
		    ReadMultipleRegistersRequest request = null; // the request

		    try {
		      // Open the connection
		      connection = new TCPMasterConnection(addr);
		      connection.setPort(port);
		      connection.connect();

		      // Prepare the request
		      request = new ReadMultipleRegistersRequest(register, length);

		      // Prepare the transaction
		      transaction = new ModbusTCPTransaction(connection);
		      transaction.setRequest(request);
		      transaction.execute();
		      ModbusResponse response = transaction.getResponse();

		      // Close the connection
		      connection.close();

		      if (response instanceof ReadMultipleRegistersResponse) {
		        return (ReadMultipleRegistersResponse) response;
		      }
		      else if (response instanceof ExceptionResponse) {
		        System.err.println("Got Modbus exception response, code: "
		            + ((ExceptionResponse) response).getExceptionCode());
		        return null;
		      }
		      else {
		        System.err.println("Got strange Modbus reply.");
		        return null;
		      }
		    }
		    catch (Exception e) {
		      System.err.println("Got exception: " + e.getMessage());
		      e.printStackTrace();
		      return null;
		    }
		  }
	
	
	
	public void getFromCamera() {
		    try {
		        /* Variables for storing the parameters */
		        InetAddress addr = null; // the slave's address
		        int port = Modbus.DEFAULT_PORT;
		        ReadMultipleRegistersResponse response;
		        addr = InetAddress.getByName("localhost");

		        Boolean[] boardpoint_status = new Boolean[24];
		        
		        response = readRegisters(addr, port, 30010, 24);
		        for(int i = 0; i < 24; i++) {
		        	int val = response.getRegisterValue(i);
		        	if(val == 22127) {
		        		boardpoint_status[i] = true;
		        	} else {
		        		boardpoint_status[i] = false;

		        	}
		        	System.out.println(i + ": " + boardpoint_status[i]);
		        }
		    } catch (Exception ex) {
		      ex.printStackTrace();
		    }
		  }//main
	
	public static void main(String[] args) {
		ModbusTest modbusTest = new ModbusTest();
		modbusTest.getFromCamera();
	}

}
