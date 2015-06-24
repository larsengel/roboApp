package application;

import java.util.Arrays;

import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;

public class BoardPoints {
	private Frame center;
	private Frame piece_origin;
	private Frame piece_drop;
	private Frame[][] points;
	private Boolean[][] belongsToGame;
	public Boolean[] field_bool;
	private float angle_board;
	private float distance_0, distance_1, distance_2, distance_3;
	private final int DIAMETER_OF_PIECE = 25;
	public static int nr_of_placed = 0;
	BoardPoints(AbstractFrame _center, float _angle_board, AbstractFrame _piece_origin, AbstractFrame _piece_drop) {
		this.center =  _center.copy();
		this.angle_board = _angle_board;
		distance_0 = 0;
		distance_1 = 30;
		distance_2 = 65;
		distance_3 = 100;
		points = new Frame[7][7];
		piece_origin = _piece_origin.copy();
		piece_drop = _piece_drop.copy();
		field_bool = new Boolean[24];
		Arrays.fill(field_bool, Boolean.FALSE);
		
        belongsToGame = new Boolean[7][7];
        
		for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                // assign belongs to game value
                if ((0 <= x && x <= 6) && (0 <= y && y <= 6)
                        && ((x == y && x != 3 && y != 3)
                        || ((x + y) == 6 && x != 3)
                        || (x == 3 && y != 3)
                        || (y == 3 && x != 3))) {
                    belongsToGame[x][y] = true;
                } else {
                    belongsToGame[x][y] = false;
                }
            }
        }
	}
	
	/**
	 * Method to calculate all the gameboard points
	 * 
	 */
	public void calculateBoard() {
		
			points[0][0] = new Frame();
			points[0][0] = center.copy();
			points[0][0].setX(center.getX() + (-distance_3 	* Math.cos(angle_board) + (-distance_3 * Math.sin(angle_board))));
			points[0][0].setY(center.getY() + ( distance_3 	* Math.sin(angle_board) + (-distance_3 * Math.cos(angle_board))));
			
			points[0][3] = new Frame();
			points[0][3] = center.copy();
			points[0][3].setX(center.getX() + (-distance_3 	* Math.cos(angle_board) + ( distance_0 * Math.sin(angle_board))));
			points[0][3].setY(center.getY() + ( distance_3 	* Math.sin(angle_board) + ( distance_0 * Math.cos(angle_board))));
			
			points[0][6] = new Frame();
			points[0][6] = center.copy();
			points[0][6].setX(center.getX() + (-distance_3 	* Math.cos(angle_board) + ( distance_3 * Math.sin(angle_board))));
			points[0][6].setY(center.getY() + ( distance_3 	* Math.sin(angle_board) + ( distance_3 * Math.cos(angle_board))));
			
			points[1][1] = new Frame();
			points[1][1] = center.copy();
			points[1][1].setX(center.getX() + (-distance_2 	* Math.cos(angle_board) + (-distance_2 * Math.sin(angle_board))));
			points[1][1].setY(center.getY() + ( distance_2 	* Math.sin(angle_board) + (-distance_2 * Math.cos(angle_board))));
			
			points[1][3] = new Frame();
			points[1][3] = center.copy();
			points[1][3].setX(center.getX() + (-distance_2 	* Math.cos(angle_board) + ( distance_0 * Math.sin(angle_board))));
			points[1][3].setY(center.getY() + ( distance_2 	* Math.sin(angle_board) + ( distance_0 * Math.cos(angle_board))));
			
			points[1][5] = new Frame();
			points[1][5] = center.copy();
			points[1][5].setX(center.getX() + (-distance_2 	* Math.cos(angle_board) + ( distance_2 * Math.sin(angle_board))));
			points[1][5].setY(center.getY() + ( distance_2 	* Math.sin(angle_board) + ( distance_2 * Math.cos(angle_board))));
			
			points[2][2] = new Frame();
			points[2][2] = center.copy();
			points[2][2].setX(center.getX() + (-distance_1 	* Math.cos(angle_board) + (-distance_1 * Math.sin(angle_board))));
			points[2][2].setY(center.getY() + ( distance_1 	* Math.sin(angle_board) + (-distance_1 * Math.cos(angle_board))));
			
			points[2][3] = new Frame();
			points[2][3] = center.copy();
			points[2][3].setX(center.getX() + (-distance_1 	* Math.cos(angle_board) + ( distance_0 * Math.sin(angle_board))));
			points[2][3].setY(center.getY() + ( distance_1 	* Math.sin(angle_board) + ( distance_0 * Math.cos(angle_board))));
			
			points[2][4] = new Frame();
			points[2][4] = center.copy();
			points[2][4].setX(center.getX() + (-distance_1 	* Math.cos(angle_board) + ( distance_1 * Math.sin(angle_board))));
			points[2][4].setY(center.getY() + ( distance_1 	* Math.sin(angle_board) + ( distance_1 * Math.cos(angle_board))));
			
			points[3][0] = new Frame();
			points[3][0] = center.copy();
			points[3][0].setX(center.getX() + ( distance_0 	* Math.cos(angle_board) + (-distance_3 * Math.sin(angle_board))));
			points[3][0].setY(center.getY() + ( distance_0 	* Math.sin(angle_board) + (-distance_3 * Math.cos(angle_board))));
			
			points[3][1] = new Frame();
			points[3][1] = center.copy();
			points[3][1].setX(center.getX() + ( distance_0 	* Math.cos(angle_board) + (-distance_2 * Math.sin(angle_board))));
			points[3][1].setY(center.getY() + ( distance_0 	* Math.sin(angle_board) + (-distance_2 * Math.cos(angle_board))));
			
			points[3][2] = new Frame();
			points[3][2] = center.copy();
			points[3][2].setX(center.getX() + ( distance_0 	* Math.cos(angle_board) + (-distance_1 * Math.sin(angle_board))));
			points[3][2].setY(center.getY() + ( distance_0 	* Math.sin(angle_board) + (-distance_1 * Math.cos(angle_board))));
			
			points[3][4] = new Frame();
			points[3][4] = center.copy();
			points[3][4].setX(center.getX() + ( distance_0 	* Math.cos(angle_board) + ( distance_1 * Math.sin(angle_board))));
			points[3][4].setY(center.getY() + ( distance_0 	* Math.sin(angle_board) + ( distance_1 * Math.cos(angle_board))));
			
			points[3][5] = new Frame();
			points[3][5] = center.copy();
			points[3][5].setX(center.getX() + ( distance_0 	* Math.cos(angle_board) + ( distance_2 * Math.sin(angle_board))));
			points[3][5].setY(center.getY() + ( distance_0 	* Math.sin(angle_board) + ( distance_2 * Math.cos(angle_board))));
			
			points[3][6] = new Frame();
			points[3][6] = center.copy();
			points[3][6].setX(center.getX() + ( distance_0 	* Math.cos(angle_board) + ( distance_3 * Math.sin(angle_board))));
			points[3][6].setY(center.getY() + ( distance_0 	* Math.sin(angle_board) + ( distance_3 * Math.cos(angle_board))));
			
			points[4][2] = new Frame();
			points[4][2] = center.copy();
			points[4][2].setX(center.getX() + ( distance_1 	* Math.cos(angle_board) + (-distance_1 * Math.sin(angle_board))));
			points[4][2].setY(center.getY() + (-distance_1 	* Math.sin(angle_board) + (-distance_1 * Math.cos(angle_board))));
			
			points[4][3] = new Frame();
			points[4][3] = center.copy();
			points[4][3].setX(center.getX() + ( distance_1 	* Math.cos(angle_board) + ( distance_0 * Math.sin(angle_board))));
			points[4][3].setY(center.getY() + (-distance_1 	* Math.sin(angle_board) + ( distance_0 * Math.cos(angle_board))));
			
			points[4][4] = new Frame();
			points[4][4] = center.copy();
			points[4][4].setX(center.getX() + ( distance_1 	* Math.cos(angle_board) + ( distance_1 * Math.sin(angle_board))));
			points[4][4].setY(center.getY() + (-distance_1 	* Math.sin(angle_board) + ( distance_1 * Math.cos(angle_board))));
			
			points[5][1] = new Frame();
			points[5][1] = center.copy();
			points[5][1].setX(center.getX() + ( distance_2 	* Math.cos(angle_board) + (-distance_2 * Math.sin(angle_board))));
			points[5][1].setY(center.getY() + (-distance_2 	* Math.sin(angle_board) + (-distance_2 * Math.cos(angle_board))));
			
			points[5][3] = new Frame();
			points[5][3] = center.copy();
			points[5][3].setX(center.getX() + ( distance_2 	* Math.cos(angle_board) + ( distance_0 * Math.sin(angle_board))));
			points[5][3].setY(center.getY() + (-distance_2 	* Math.sin(angle_board) + ( distance_0 * Math.cos(angle_board))));
			
			points[5][5] = new Frame();
			points[5][5] = center.copy();
			points[5][5].setX(center.getX() + ( distance_2 	* Math.cos(angle_board) + ( distance_2 * Math.sin(angle_board))));
			points[5][5].setY(center.getY() + (-distance_2 	* Math.sin(angle_board) + ( distance_2 * Math.cos(angle_board))));
			
			points[6][0] = new Frame();
			points[6][0] = center.copy();
			points[6][0].setX(center.getX() + ( distance_3 	* Math.cos(angle_board) + (-distance_3 * Math.sin(angle_board))));
			points[6][0].setY(center.getY() + (-distance_3 	* Math.sin(angle_board) + (-distance_3 * Math.cos(angle_board))));
			
			points[6][3] = new Frame();
			points[6][3] = center.copy();
			points[6][3].setX(center.getX() + ( distance_3 	* Math.cos(angle_board) + ( distance_0 * Math.sin(angle_board))));
			points[6][3].setY(center.getY() + (-distance_3 	* Math.sin(angle_board) + ( distance_0 * Math.cos(angle_board))));
			
			points[6][6] = new Frame();
			points[6][6] = center.copy();
			points[6][6].setX(center.getX() + ( distance_3 	* Math.cos(angle_board) + ( distance_3 * Math.sin(angle_board))));
			points[6][6].setY(center.getY() + (-distance_3 	* Math.sin(angle_board) + ( distance_3 * Math.cos(angle_board))));
	}
	

	/** GETTER
	 */
	public Frame getPoint(int i, int j) {
		return points[i][j];
	}
	
	public Frame[][] getPoints() {
		return points;
	}
	public Frame getCenter() {
		return center;
	}
	
	public Frame getPieceOrigin() {
		Frame new_dest = piece_origin.copy();
		new_dest.setX(new_dest.getX() + nr_of_placed * DIAMETER_OF_PIECE);
		return new_dest;
	}
	
	public Frame getPieceDrop() {
		return piece_drop;
	}

	public int[] getFieldByNumber(int nr) {
		int count = 0;
		int[] result = new int[2];
		for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
            	if(belongsToGame[x][y]) {
                	if(count ==  nr) {
                		result[0] = x;
                		result[1] = y;
                		return result;
                	}
            		count++;
            	}
            }    
		}
		return null;
	}
	
	public int getFieldByCoords(int _x, int _y) {
		int count = 0;
		for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
            	if(belongsToGame[x][y]) {
                	if(x == _x && y == _y) {
                		return count;
                	}
            		count++;
            	}
            }    
		}
		return -1;
	}
	
	public int compareForChangesPlace(Boolean[] _new_field) {
		for (int i = 0; i <= 23; i++) {
			if(field_bool[i] == Boolean.FALSE && _new_field[i] == Boolean.TRUE) {
				return i;
			}
		}
		return -1;
	}
	
	public int compareForChangesTake(Boolean[] _new_field) {
		for (int i = 0; i <= 23; i++) {
			if(field_bool[i] == Boolean.TRUE && _new_field[i] == Boolean.FALSE) {
				return i;
			}
		}
		return -1;
	}
	public int[] compareForChangesMove(Boolean[] _new_field) {
		int[] result = new int[2];
		for (int i = 0; i <= 23; i++) {
			if(field_bool[i] == Boolean.TRUE && _new_field[i] == Boolean.FALSE) {
				result[0] = i;
			}
		}
		for (int i = 0; i <= 23; i++) {
			if(field_bool[i] == Boolean.FALSE && _new_field[i] == Boolean.TRUE) {
				result[1] = i;
			}
		}
		return result;
	}
}
