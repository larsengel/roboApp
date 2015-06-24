import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ExceptionResponse;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;


public class ModbusTest {
	
	private InetAddress addr;
	private int port = Modbus.DEFAULT_PORT;
	
	ModbusTest(String _addr) throws UnknownHostException {
		addr = InetAddress.getByName(_addr);
	}
	
	 private ReadMultipleRegistersResponse readRegisters(int register, int length) {
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
	
	
	
	public Boolean[] getFromCamera() {
		    try {

		        ReadMultipleRegistersResponse response;
		        Boolean[] boardpoint_status = new Boolean[24];
		        response = readRegisters(30010, 24);
		        for(int i = 23, j = 0; i >= 0; i--, j++) {
		        	int val = response.getRegisterValue(i);
		        	if(val == 22127) {
		        		boardpoint_status[j] = true;
		        	} else {
		        		boardpoint_status[j] = false;
		        	}
		        	System.out.println(j + " " + boardpoint_status[j]);
		        }
		        return boardpoint_status;
		    } catch (Exception ex) {
		      ex.printStackTrace();
		      return null;
		    }
		  }
	
	public static void main(String[] args) throws UnknownHostException {
		ModbusTest modbusTest = new ModbusTest("localhost");
		//modbusTest.getFromCamera();
		
        Boolean[][] belongsToGame = new Boolean[7][7];
        
		for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                //field[x][y] = Token.EMPTY;

                // assign belongs to game value
                if ((0 <= x && x <= 6) && (0 <= y && y <= 6)
                        && ((x == y && x != 3 && y != 3)
                        || ((x + y) == 6 && x != 3)
                        || (x == 3 && y != 3)
                        || (y == 3 && x != 3))) {
                    belongsToGame[x][y] = true;
                    //System.out.println(x + " " + y);

                } else {
                    belongsToGame[x][y] = false;
                }
            }    
		}
		int search = 7;
		int count = 0;
		outer:
		for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {

            	if(belongsToGame[x][y]) {
                	if(count ==  search) {
                		System.out.println(x + " " + y + " " + search + " " + count);
                		break outer;
                	}
            		System.out.println(x + " " + y + " " + search + " " + count);
            		count++;
            	}
            }    
		}
	}
}
