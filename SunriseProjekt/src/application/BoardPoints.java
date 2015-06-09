package application;

import com.kuka.roboticsAPI.geometricModel.AbstractFrame;
import com.kuka.roboticsAPI.geometricModel.Frame;

public class BoardPoints {
	private Frame center;
	private Frame[][] points;
	private float angle_board;
	private float distance;
	
	BoardPoints(AbstractFrame _center, float _angle_board) {
		this.center =  _center.copy();
		this.angle_board = _angle_board;
		distance = 30;
		points = new Frame[7][7];
	}
	
	/**
	 * Method to calculate all the gameboard points
	 * 
	 */
	public void calculateBoard() {
		
			points[0][0] = new Frame();
			points[0][0] = center.copy();
			points[0][0].setX(center.getX() + (3 *distance 	* Math.cos(angle_board) + (3 * distance * Math.sin(angle_board))));
			points[0][0].setY(center.getY() + (-3*distance 	* Math.sin(angle_board) + (3 * distance * Math.cos(angle_board))));
			points[0][1] = new Frame();
			points[0][1] = center.copy();
			points[0][1].setX(center.getX() + (0 			* Math.cos(angle_board) + (3 * distance * Math.sin(angle_board))));
			points[0][1].setY(center.getY() + (-0 			* Math.sin(angle_board) + (3 * distance * Math.cos(angle_board))));
			points[0][2] = new Frame();
			points[0][2] = center.copy();
			points[0][2].setX(center.getX() + (-3*distance 	* Math.cos(angle_board) + (3 * distance * Math.sin(angle_board))));
			points[0][2].setY(center.getY() + (-3*-distance * Math.sin(angle_board) + (3 * distance * Math.cos(angle_board))));

			points[1][0] = new Frame();
			points[1][0] = center.copy();
			points[1][0].setX(center.getX() + (2*distance 	* Math.cos(angle_board) + (2 * distance * Math.sin(angle_board))));
			points[1][0].setY(center.getY() + (-2*distance 	* Math.sin(angle_board) + (2 * distance * Math.cos(angle_board))));
			points[1][1] = new Frame();
			points[1][1] = center.copy();
			points[1][1].setX(center.getX() + (0 			* Math.cos(angle_board) + (2 * distance * Math.sin(angle_board))));
			points[1][1].setY(center.getY() + (-0 			* Math.sin(angle_board) + (2 * distance * Math.cos(angle_board))));
			points[1][2] = new Frame();
			points[1][2] = center.copy();
			points[1][2].setX(center.getX() + (-2*distance 	* Math.cos(angle_board) + (2 * distance * Math.sin(angle_board))));
			points[1][2].setY(center.getY() + (-2*-distance * Math.sin(angle_board) + (2 * distance * Math.cos(angle_board))));
			
			points[2][0] = new Frame();
			points[2][0] = center.copy();
			points[2][0].setX(center.getX() + (distance 	* Math.cos(angle_board) + (distance * Math.sin(angle_board))));
			points[2][0].setY(center.getY() + (-distance 	* Math.sin(angle_board) + (distance * Math.cos(angle_board))));
			points[2][1] = new Frame();
			points[2][1] = center.copy();
			points[2][1].setX(center.getX() + (0 			* Math.cos(angle_board) + (distance * Math.sin(angle_board))));
			points[2][1].setY(center.getY() + (-0 			* Math.sin(angle_board) + (distance * Math.cos(angle_board))));
			points[2][2] = new Frame();
			points[2][2] = center.copy();
			points[2][2].setX(center.getX() + (-1*distance 	* Math.cos(angle_board) + (distance * Math.sin(angle_board))));
			points[2][2].setY(center.getY() + (-1*-distance * Math.sin(angle_board) + (distance * Math.cos(angle_board))));
			
			points[3][0] = new Frame();
			points[3][0] = center.copy();
			points[3][0].setX(center.getX() + (3*distance 	* Math.cos(angle_board) + (0 * Math.sin(angle_board))));
			points[3][0].setY(center.getY() + (3*-distance 	* Math.sin(angle_board) + (0 * Math.cos(angle_board))));
			points[3][1] = new Frame();
			points[3][1] = center.copy();
			points[3][1].setX(center.getX() + (2*distance 	* Math.cos(angle_board) + (0 * Math.sin(angle_board))));
			points[3][1].setY(center.getY() + (2*-distance 	* Math.sin(angle_board) + (0 * Math.cos(angle_board))));
			points[3][2] = new Frame();
			points[3][2] = center.copy();
			points[3][2].setX(center.getX() + (1*distance 	* Math.cos(angle_board) + (0 * Math.sin(angle_board))));
			points[3][2].setY(center.getY() + (1*-distance 	* Math.sin(angle_board) + (0 * Math.cos(angle_board))));
			points[3][3] = new Frame();
			points[3][3] = center.copy();
			points[3][3].setX(center.getX() + (-1*distance 	* Math.cos(angle_board) + (0 * Math.sin(angle_board))));
			points[3][3].setY(center.getY() + (-1*-distance * Math.sin(angle_board) + (0 * Math.cos(angle_board))));
			points[3][4] = new Frame();
			points[3][4] = center.copy();
			points[3][4].setX(center.getX() + (-2*distance 	* Math.cos(angle_board) + (0 * Math.sin(angle_board))));
			points[3][4].setY(center.getY() + (-2*-distance * Math.sin(angle_board) + (0 * Math.cos(angle_board))));
			points[3][5] = new Frame();
			points[3][5] = center.copy();
			points[3][5].setX(center.getX() + (-3*distance 	* Math.cos(angle_board) + (0 * Math.sin(angle_board))));
			points[3][5].setY(center.getY() + (-3*-distance * Math.sin(angle_board) + (0 * Math.cos(angle_board))));

			points[4][0] = new Frame();
			points[4][0] = center.copy();
			points[4][0].setX(center.getX() + (1*distance 	* Math.cos(angle_board) + (-1 * distance * Math.sin(angle_board))));
			points[4][0].setY(center.getY() + (1*-distance 	* Math.sin(angle_board) + (-1 * distance * Math.cos(angle_board))));
			points[4][1] = new Frame();
			points[4][1] = center.copy();
			points[4][1].setX(center.getX() + (0*distance 	* Math.cos(angle_board) + (-1 * distance * Math.sin(angle_board))));
			points[4][1].setY(center.getY() + (0*-distance 	* Math.sin(angle_board) + (-1 * distance * Math.cos(angle_board))));
			points[4][2] = new Frame();
			points[4][2] = center.copy();
			points[4][2].setX(center.getX() + (-1*distance 	* Math.cos(angle_board) + (-1 * distance * Math.sin(angle_board))));
			points[4][2].setY(center.getY() + (-1*-distance * Math.sin(angle_board) + (-1 * distance * Math.cos(angle_board))));
			
			points[5][0] = new Frame();
			points[5][0] = center.copy();
			points[5][0].setX(center.getX() + (2*distance 	* Math.cos(angle_board) + (-2 * distance * Math.sin(angle_board))));
			points[5][0].setY(center.getY() + (2*-distance 	* Math.sin(angle_board) + (-2 * distance * Math.cos(angle_board))));
			points[5][1] = new Frame();
			points[5][1] = center.copy();
			points[5][1].setX(center.getX() + (0*distance 	* Math.cos(angle_board) + (-2 * distance * Math.sin(angle_board))));
			points[5][1].setY(center.getY() + (0*-distance 	* Math.sin(angle_board) + (-2 * distance * Math.cos(angle_board))));
			points[5][2] = new Frame();
			points[5][2] = center.copy();
			points[5][2].setX(center.getX() + (-2*distance 	* Math.cos(angle_board) + (-2 * distance * Math.sin(angle_board))));
			points[5][2].setY(center.getY() + (-2*-distance * Math.sin(angle_board) + (-2 * distance * Math.cos(angle_board))));

			points[6][0] = new Frame();
			points[6][0] = center.copy();
			points[6][0].setX(center.getX() + (3*distance 	* Math.cos(angle_board) + (-3 * distance * Math.sin(angle_board))));
			points[6][0].setY(center.getY() + (3*-distance 	* Math.sin(angle_board) + (-3 * distance * Math.cos(angle_board))));
			points[6][1] = new Frame();
			points[6][1] = center.copy();
			points[6][1].setX(center.getX() + (0*distance 	* Math.cos(angle_board) + (-3 * distance * Math.sin(angle_board))));
			points[6][1].setY(center.getY() + (0*-distance 	* Math.sin(angle_board) + (-3 * distance * Math.cos(angle_board))));
			points[6][2] = new Frame();
			points[6][2] = center.copy();
			points[6][2].setX(center.getX() + (-3*distance 	* Math.cos(angle_board) + (-3 * distance * Math.sin(angle_board))));
			points[6][2].setY(center.getY() + (-3*-distance * Math.sin(angle_board) + (-3 * distance * Math.cos(angle_board))));
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
