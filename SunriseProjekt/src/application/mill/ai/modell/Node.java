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

package application.mill.ai.modell;

import java.util.ArrayList;
import java.util.List;
import application.mill.Controller.GameRuleController;
import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.ai.controller.heuristics.Catch22;
import application.mill.ai.controller.heuristics.CreateOwnMill;
import application.mill.ai.controller.heuristics.IRater;

/**
 * The <code>Node</code> class stores a possible move as well as a game field
 * As well as a field to which to apply the move.
 */
public class Node {

    private PossibleMove move;
    private GameField field;
    private SimplePlayer executingPlayer; // player to execute this move
    private SimplePlayer otherPlayer; // enemy of executingPlayer
    private SimplePlayer nextExecutingPlayer; // player to execute next move
    private Token aiColor;
    private boolean takeMove;
    private boolean createdMill;
    private List<IRater> ratingMechanisms;

    /**
     * constructs new node.
     * <p>
     * The state of the given field and players will be changed, so if
     * this is not intended, pass copies.
     *
     * @param field game field
     * @param move move to apply to game field
     * @param takeMove whether this is a take move (or a move move)
     * @param executingPlayer player which executes the given move
     * @param aiColor color of the ai
     */
    public Node(GameField field, PossibleMove move, boolean takeMove, SimplePlayer executingPlayer, SimplePlayer otherPlayer, Token aiColor) {
        this.field = field;
        this.move = move;
        this.takeMove = takeMove;
        this.executingPlayer = executingPlayer;
        this.otherPlayer = otherPlayer;
        this.aiColor = aiColor;
        initRating();
    }

    /**
     * initializes the rating mechanisms.
     */
    private final void initRating() {
        ratingMechanisms = new ArrayList<IRater>();
        ratingMechanisms.add(new Catch22());
        ratingMechanisms.add(new CreateOwnMill());
        //ratingMechanisms.add(new SavelyOpenMill());
        //ratingMechanisms.add(new TwoInARow()); // broken
        //ratingMechanisms.add(new BlockEnemyMill());
    }

    /**
     * applies the move saved in this node to the gamefield for the given player.
     * <P>
     * It is assumed, that the move is correct.
     * If the move is a take move, it should be stored in the destination of the move.
     * <P>
     * As a sideeffect this method will determin if the move created a mill and
     * save this. It will also determine the player that gets to move next.
     * <p>
     * If this node stores a take move, the number of stones of the otherPlayer
     * will be lowered by one.
     * @throws application.mill.Controller.GameException 
     */
    public void applyMove() throws application.mill.Interfaces.GameException {
        // execute move and determine if mill was created
        if (takeMove) {
            otherPlayer.lowerNumberOfStonesOnBoard();
            createdMill = false;
            try {
                //field.setFieldStatus(move.getDest().x, move.getDest().y, Token.EMPTY);
                field.removeStoneAtPosition(otherPlayer, move.getDest().x, move.getDest().y);
            } catch (GameException ex) {
                System.out.println("applyMove: take enemy stone" + ex);
            }
        } else {
            try {
                field.applyMove(move, executingPlayer.getColor());
                createdMill = GameRuleController.createdMill(field, move, executingPlayer.getColor());
            } catch (GameException ex) {
            }
        }
        
        // determine next player to make a move
        if (createdMill) {
            nextExecutingPlayer = executingPlayer;
        } else {
           nextExecutingPlayer = otherPlayer;
        }

    }

    /**
     * adds ratings to the move as determined by the rating mechanisms in
     * <code>ratingMechanisms</code>.
     * <p>
     * The move should already be executed.
     */
    public void rateSingleMove() {

        if (!isTakeMove()) {
        for (IRater rater : ratingMechanisms) {
            if (executingPlayer.getColor() == aiColor) {
                move.addToRating(rater.getRating(field, move, executingPlayer.getColor(), createdMill));
            } else {
                move.addToRating(-rater.getRating(field, move, executingPlayer.getColor(), createdMill));
            }

        }
        }
    }

    /**
     * returns true if the move stored in this node created a mill.
     *
     * @return true if the move stored in this node created a mill
     */
    public boolean isCreatedMill() {
        return createdMill;
    }

    /**
     * returns the player that excuted the move.
     *
     * @return player that excuted the move
     */
    public SimplePlayer getExecutingPlayer() {
        return executingPlayer;
    }

    /**
     * returns the player which gets to excute the next move.
     *
     * @return player which gets to excute the next move
     */
    public SimplePlayer getNextExecutingPlayer() {
        return nextExecutingPlayer;
    }

    /**
     * returns true if the saved move is a take move.
     * 
     * @return true if the saved move is a take move 
     */
    public boolean isTakeMove() {
        return takeMove;
    }

    /**
     * returns the stored game field.
     * 
     * @return the stored game field
     */
    public GameField getField() {
        return field;
    }

    /**
     * returns the stored move.
     * 
     * @return the stored move
     */
    public PossibleMove getMove() {
        return move;
    }

    @Override
    public String toString() {
        return executingPlayer + " isTakeMove: " + takeMove + " " + move.toString();
    }
}
