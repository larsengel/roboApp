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
package application.mill.ai.controller;

import java.util.ArrayList;
import java.util.List;
import application.mill.Controller.GameRuleController;
import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.Model.OptionObject;
import application.mill.ai.modell.PossibleMove;
import application.mill.ai.modell.Rating;
// TODO FUNCTIONALITY this can (and should) still be improved ( a lot)

/**
 *
 * @author sam
 */
public class SetMoveRater extends AbstractMoveRater {

    private List<PossibleMove> moves;
    private int maxtime;

    /**
     * creates all possible set moves for the given field.
     *
     * @param field field
     */
    private List<PossibleMove> initMoves(GameField field) {
        moves = new ArrayList<PossibleMove>();
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeight(); j++) {
                try {
                    Token fieldStatus = field.getFieldStatus(i, j);
                    if (fieldStatus == Token.EMPTY) {
                        PossibleMove possibleMove = new PossibleMove();
                        possibleMove.setSource(-1, -1);
                        possibleMove.setDest(i, j);
                        moves.add(possibleMove);
                    }
                } catch (GameException ex) {
                }
            }
        }
        return moves;
    }

    /**
     * returns the best next set move as determined by this method.
     *
     * @param model field
     * @param turnCounter turnCounter
     * @param player player
     * @return next set move
     */
    public Move getNextSetMove(GameField model, Token player) {
        initMoves(model);
        rateMoves(model, player);
        return super.getBestMove(moves);

    }
     
    /**
     * rates all moves.
     *
     * @param model model
     * @param player player
     */
    private void rateMoves(GameField model, Token player) {
        for (PossibleMove possibleMove : moves) {
            checkNumberOfNeighborFields(possibleMove, model);
            checkNumberOfEmptyFields(possibleMove, model);
            
            // execute move on copy
            GameField copy = model.getCopy();
            try {
                copy.setFieldStatus(possibleMove.getDest().x, possibleMove.getDest().y, player);
            } catch (GameException ex) {
            }
            if (GameRuleController.createdMill(copy, possibleMove, player)) {
                possibleMove.addToRating(Rating.MILL);
            }
            if (GameRuleController.createdMill(copy, possibleMove, getEnemy(player))) {
                possibleMove.addToRating(Rating.MILL / 2);
            }            
        }
    }

    /**
     * checks the number of neighboring fields for the given move.
     * <p>
     * It does not matter if they are already taken.
     * Adds <code>Rating.VERYHIGH</code> for every neighoring field to the
     * rating of the move.
     *
     * @param possibleMove possibleMove
     * @param model model
     */
    private void checkNumberOfNeighborFields(PossibleMove possibleMove, GameField model) {
        int neighbors = model.getAllNeighbors(possibleMove.getDest().x, possibleMove.getDest().y).size();
        possibleMove.addToRating(neighbors * Rating.VERYHIGH);
    }

    /**
     * checks how many empty neighbors this field has.
     * <p>
     * Adds a rating of <code>Rating.LOW</code> for every empty neighbor.
     *
     * @param possibleMove possibleMove
     * @param model model
     */
    private void checkNumberOfEmptyFields(PossibleMove possibleMove, GameField model) {
        int neighbors = 0;
        if (model.getLegalDown(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
            neighbors++;
        }
        if (model.getLegalLeft(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
            neighbors++;
        }
        if (model.getLegalRight(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
            neighbors++;
        }
        if (model.getLegalUp(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
            neighbors++;
        }

        if (OptionObject.playWithDiagonalLines) {
            if (model.getLegalDownLeft(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
                neighbors++;
            }
            if (model.getLegalDownRight(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
                neighbors++;
            }
            if (model.getLegalUpLeft(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
                neighbors++;
            }
            if (model.getLegalUpRight(possibleMove.getDest().x, possibleMove.getDest().y) == Token.EMPTY) {
                neighbors++;
            }
        }
        possibleMove.addToRating(neighbors * Rating.LOW);
    }
}
