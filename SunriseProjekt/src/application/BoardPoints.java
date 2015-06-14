package application;

import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;

public class BoardPoints {
	private Frame center;
	private Frame[][] points;
	private float angle_board;
	private float distance_0, distance_1, distance_2, distance_3;
	
	BoardPoints(AbstractFrame _center, float _angle_board) {
		this.center =  _center.copy();
		this.angle_board = _angle_board;
		distance_0 = 0;
		distance_1 = 30;
		distance_2 = 65;
		distance_3 = 100;
		points = new Frame[7][7];
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
	
	/**
	 * 
	 * @param i
	 * @param j
	 * @return
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

}
