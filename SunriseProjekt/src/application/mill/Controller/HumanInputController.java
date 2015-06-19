/*    
    Copyright (C) 2012 http://software-talk.org/ (developer@software-talk.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package application.mill.Controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import application.mill.Interfaces.Move;
//import java.awt.event.MouseEvent;

import application.mill.Interfaces.GameException;
import application.mill.Interfaces.ModelInterface;
import application.mill.Interfaces.Playerinterface;
//import application.mill.Interfaces.Token;
import application.mill.Model.AbstractPlayer;
import application.mill.Model.Buffer;
import application.mill.Model.MyMove;
//import application.mill.view.GameLabel;

/**
 * This class reads input from a human player.
 * @version 1.0.2
 */
public class HumanInputController extends AbstractPlayer implements Playerinterface {

    private Buffer<Integer[]> buff; // stores one user mouse event
    //private boolean playsLeftMouseButton;

    /**
     * constructs new HumanInputController.
     * @param buff buff
     * @param playsLeftMouseButton playsLeftMouseButton
     * @param name name
     * @param numberOfStones numberOfStones to place
     */
    public HumanInputController(Buffer<Integer[]> buff, boolean playsLeftMouseButton,
            String name, int numberOfStones) {
        super(name, numberOfStones);
        this.buff = buff;
        //this.playsLeftMouseButton = playsLeftMouseButton;
    }
    private MyMove nextMove;

    public Move getNextMove(ModelInterface model) {
        return getNextMove(model, 0);
    }

    /**
     * returns a move of a stone on the board.
     * <p>
     * @param model model
     * @param turnCounter turnCounter
     * @return move of a stone on the board
     */
    @Override
    public Move getNextMove(ModelInterface model, int turnCounter) {
        nextMove = new MyMove();
        getNextMoveSource(model);
        //getNextMoveDest();
        return nextMove;
    }

    /**
     * adds a source to the <code>nextMove</code> from player input.
     * 
     * @param model model
     */
    private void getNextMoveSource(ModelInterface model) {
        while (true) {
            try {
                Integer[] temp = buff.read();
                // GameLabel temp = (GameLabel) event.getComponent();
                nextMove.setSource(temp[1], temp[0]);
                nextMove.setDest(temp[3], temp[2]);
                System.out.println("input :" + temp);
                if (model.getFieldStatus(nextMove.getSource().x, nextMove.getSource().y) == super.getColor()) {

                  // temp.setIsSelected(true); // marks label with border
                        //temp.updateUI();
                        break;
                }
            } catch (GameException ex) {
                Logger.getLogger(HumanInputController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * adds a dest to the <code>nextMove</code> from player input.
     * <p>
     **/
    @SuppressWarnings("unused")
	private void getNextMoveDest() {
        while (true) {
            Integer[] temp = buff.read();
            System.out.println("input2 :" + temp);

            nextMove.setDest(temp[1], temp[0]);
//                temp.setIsSelected(true); // marks label with border
//                temp.updateUI();
                break;
            
        }
    }

    public Move getNextSetMove(ModelInterface model) {
        return getNextSetMove(model, 0);
    }

    /**
     * returns a position to place a stone on the board.
     * <p>
     * The position will be saved in the destination of the move.
     * <p>
     * @param model model
     * @param turnCounter turnCounter
     * @return position (in destination of the move)
     */
    @Override
    public Move getNextSetMove(ModelInterface model, int turnCounter) {
        while (true) {
        	Integer[] temp = buff.read();
        	System.out.println("input3 :" + temp[1] + temp[0]);
                MyMove move = new MyMove();
                move.setDest(temp[1], temp[0]);
                return move;
        }
    }

    /**
     * returns a position to remove a stone on the board.
     * <p>
     * The position will be saved in the destination of the move.
     * <p>
     * WARNING: null will be returned if the <code>MouseEvent</code> is null.
     * In this case the thread is interupted by the gui to restart game.
     * <p>
     * @param model model
     * @return position (in destination of the move)
     */
    @Override
    public Move getRemoveMove(ModelInterface model) {
        return getRemoveMove(model, 0);
    }

    public Move getRemoveMove(ModelInterface model, int time) {
    	while (true) {
        	Integer[] temp = buff.read();
        	System.out.println("input4 :" + temp);
                MyMove move = new MyMove();
                move.setDest(temp[1], temp[0]);
                return move;
        }
    }
}
