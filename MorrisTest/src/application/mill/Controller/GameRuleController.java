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

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import application.mill.Interfaces.GameException;
import application.mill.Interfaces.ModelInterface;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.Model.MyMove;
import application.mill.Model.MyPlayerInterface;
import application.mill.Model.OptionObject;

/**
 * This class holds methods which check if the rules
 * of the game are fulfilled.
 * None of these methods change the state of the field (or any other
 * given parameter).
 *
 * @version 1.5.2
 */
public class GameRuleController {

    /**
     * checks if the given move is a legal move of setting a stone on the field.
     * <p>
     * Must be called before executing the move.
     *
     * @param field field
     * @param move move the player wishes to execute (only dest matters)
     * @return whether or not the given move is a legal move of setting a stone on the field
     * @throws GameException if destination of move is not a valid position on the field
     * @throws application.mill.Interfaces.GameException 
     */
    public static boolean isLegalSetMove(ModelInterface field, Move move) throws application.mill.Interfaces.GameException {
        return field.getFieldStatus(move.getDest().x, move.getDest().y) == Token.EMPTY;
    }

    /**
     * checks if the given move is a legal move of moving a stone on the field.
     * <p>
     * Must be called before executing the move.
     * <p>
     * WARNING: if the move is null, false will be returned.
     * In this case the thread is interupted by the gui to restart the game.
     * <p>
     * @param field field
     * @param move move the player wishes to execute
     * @param player player which wants to remove the stone 
     * @return whether or not the given move is a legal move of moving a stone on the field.
     * @throws GameException if destination or source of move is not a valid position on the field
     */
    public static boolean isLegalMove(GameField field, Move move, MyPlayerInterface player) throws application.mill.Interfaces.GameException {
        if ((field.getFieldStatus(move.getDest().x, move.getDest().y) == Token.EMPTY)
                && field.getFieldStatus(move.getSource().x, move.getSource().y) == player.getColor()) {
            return isSourceNextToDest(field, move, player.getColor()) || hasThreeStonesLeft(player);

        } else {
            return false;
        }
    }

    /**
     * checks if the given player has three stones on the board.
     *
     * @param player player
     * @return true if the player has three stones on the board.
     */
    public static boolean hasThreeStonesLeft(MyPlayerInterface player) {
        return player.getNumberOfStonesOnBoard() == 3;
    }

    /**
     * checks if the given move is a legal move of removing a stone from the field.
     * <p>
     * It is not relevant if the move was already executed.
     * <p>
     * The position to take the stone from should be saved in the destination
     * of the move.
     *
     * @param field field
     * @param move move the player wishes to execute
     * @param player player which wants to remove the stone
     * @return whether or not the given move is a legal move of removing a stone from the field
     */
    public static boolean isLegalRemoveMove(GameField field, Move move, Token player) {
        Token enemy;
        if (player == Token.BLACK) {
            enemy = Token.WHITE;
        } else {
            enemy = Token.BLACK;
        }
        return !(checkMillHorizontal(move, field, enemy, 3)
                || checkMillVertical(move, field, enemy, 3)
                || checkMillDiagonally(move, field, enemy, 3));
    }

    /**
     * checks if the player opposite of the given player won.
     *
     * @param field field
     * @param enemyPlayer enemyPlayer
     * @return whether or not the given player lost
     */
    public static boolean isWin(GameField field, MyPlayerInterface enemyPlayer) {
        if (enemyPlayer.getNumberOfStonesOnBoard() < 3) {
            return true;
        } else if (noPossibleMove(field, enemyPlayer)) {
            return true;
        }
        return false;
    }

    /**
     * at the moment, this method returns false.
     * If one would like to play with draw, here is the place to implement it.
     *
     * @param field field
     * @return whether the game ended in a draw
     */
    public static boolean isDraw(GameField field) {
        return field.getTurnsWithoutMill() > OptionObject.drawAfter;
    }

    /**
     * returns whether or not it is legal to go from given source to given dest.
     * Only checks if the source is connected directly with the dest, not if
     * the dest is empty, as this is already done above in isLegalMove.
     *
     * @param field field
     * @param move move
     * @param player player
     * @return whether or not the it is legal to go from given source to given dest
     */
    private static boolean isSourceNextToDest(GameField field, Move move, Token player) {
        return isSourceNextToDestHorizontal(field, move)
                || isSourceNextToDestVertical(field, move)
                || isSourceNextToDestDiagonal(move);
    }

    /**
     * returns whether or not the it is legal to go from given source to given dest horizontally.
     *
     * @param field field
     * @param move move
     * @return whether or not it is possible to go horizontally as described in move
     */
    private static boolean isSourceNextToDestHorizontal(GameField field, Move move) {
        field.getLegalLeft(move.getSource().x, move.getSource().y);
        if (field.getxOfNeighbor() == move.getDest().x && field.getyOfNeighbor() == move.getDest().y) {
            return true;
        }
        field.getLegalRight(move.getSource().x, move.getSource().y);
        if (field.getxOfNeighbor() == move.getDest().x && field.getyOfNeighbor() == move.getDest().y) {
            return true;
        }
        return false;
    }

    /**
     * returns whether or not the it is legal to go from given source to given dest vertically.
     *
     * @param field field
     * @param move move
     * @return whether or not it is possible to go vertically as described in move
     */
    private static boolean isSourceNextToDestVertical(GameField field, Move move) {
        field.getLegalUp(move.getSource().x, move.getSource().y);
        if (field.getxOfNeighbor() == move.getDest().x && field.getyOfNeighbor() == move.getDest().y) {
            return true;
        }
        field.getLegalDown(move.getSource().x, move.getSource().y);
        if (field.getxOfNeighbor() == move.getDest().x && field.getyOfNeighbor() == move.getDest().y) {
            return true;
        }
        return false;
    }

    /**
     * returns whether or not the it is legal to go from given source to given dest diagonally.
     * <p>
     * Only applied if PLAYWITHDIAGONALY in GameController is true.
     *
     * @param move move
     * @return whether or not it is possible to go diagonally as described in move
     */
    private static boolean isSourceNextToDestDiagonal(Move move) {
        if (OptionObject.playWithDiagonalLines) {
            Point source = move.getSource();
            Point dest = move.getDest();

            if (source.x == source.y && dest.x == dest.y) {
                // moving from top left to bottom right
                if (Math.abs(source.x - dest.x) == 1) {
                    // only moving one step
                    return true;
                }

            }
            if ((source.x + source.y) == 6 && (dest.x + dest.y) == 6) {
                // moving from lower left to upper right
                if (Math.abs(source.x - dest.x) == 1 && Math.abs(source.y - dest.y) == 1) {
                    // only moving one step
                    return true;
                }

            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * returns true if the given move created a mill.
     * <p>
     * The move should already be executed.
     * <p>
     * It is assumed, that the move is made by player with the given token
     * and then it is checked, if there are two stones in a line with the one
     * stone that just got moved.
     * <p>
     * Only the destination of the move is relevant, so positions for
     * take or set moves should be saved there.
     *
     * @param field field
     * @param move move the player executed
     * @param player player
     * @return whether or not the given move created a mill
     */
    public static boolean createdMill(GameField field, Move move, Token player) {
        return checkMillHorizontal(move, field, player, 3)
                || checkMillVertical(move, field, player, 3)
                || checkMillDiagonally(move, field, player, 3);
    }

    /**
     * checks if there are a given number of stones in the row of the
     * destination of the given move.
     * // TODO the description below is untrue now. fix that
     * If the move is for example diagonal, n stones diagonal will not return true,
     * as the move did not create that, it was a pre-existing condition.
     *
     * @param possibleMove possibleMove
     * @param model model
     * @param player player
     * @param numberOfStones numberOfStones
     * @return true if the move created n stones in a row
     */
    public static boolean createdNinARow(GameField model, Move possibleMove, Token player, int numberOfStones) {
        return checkMillDiagonally(possibleMove, model, player, numberOfStones)
                || checkMillHorizontal(possibleMove, model, player, numberOfStones)
                || checkMillVertical(possibleMove, model, player, numberOfStones);
    }

    /**
     * returns true if the given move created a mill horizontally.
     * <p>
     * The move should already be executed.
     * <p>
     * It is assumed, that the move is made by player with the given token
     * and then it is checked, if there are two stones in a line with the one
     * stone that just got moved.
     * <p>
     * Only the destination of the move is relevant, so positions for
     * take or set moves should be saved there.
     *
     * @param field field
     * @param move move the player executed
     * @param player player
     * @param numberOfStones  number of stones horizontal
     * @return whether or not the given move created numberOfStones in a horizontal row
     */    
    public static boolean checkMillHorizontal(Move possibleMove, GameField model, Token player, int numberOfStones) {
        int stones = 1; // the stone that was moved
        Point dest = possibleMove.getDest();

        Token legalLeft = model.getLegalLeft(dest.x, dest.y);
        //if (tokenIsEqual(legalLeft, player)) {
        if (player.equals(legalLeft)) {
            stones++;
        } else if (legalLeft != null && numberOfStones == 3) {
            // there cannot be three in a row if there is a legal neighbor that is not the player
            return false;
        }

        int xOfNeighbor = model.getxOfNeighbor();
        int yOfNeighbor = model.getyOfNeighbor();
        Token legalLeftLeft = model.getLegalLeft(xOfNeighbor, yOfNeighbor);
        //if (tokenIsEqual(legalLeftLeft, player)) {
        if (player.equals(legalLeftLeft)) {
            stones++;
        } else if (legalLeftLeft != null && numberOfStones == 3) {
            // there cannot be three in a row if there is a legal neighbor that is not the player
            return false;
        }

        if (stones >= numberOfStones) {
            return true;
        }

        Token legalRight = model.getLegalRight(dest.x, dest.y);
        //if (tokenIsEqual(legalRight, player)) {
        if (player.equals(legalRight)) {
            stones++;
        } else if (legalRight != null && numberOfStones == 3) {
            // there cannot be three in a row if there is a legal neighbor that is not the player
            return false;
        }

        if (stones >= numberOfStones) {
            return true;
        }

        xOfNeighbor = model.getxOfNeighbor();
        yOfNeighbor = model.getyOfNeighbor();
        Token legalRightRight = model.getLegalRight(xOfNeighbor, yOfNeighbor);
        //if (tokenIsEqual(legalRightRight, player)) {
        if (player.equals(legalRightRight)) {
            stones++;
        }

        return stones >= numberOfStones;
    }

    /**
     * returns true if the given move created a mill vertical.
     * <p>
     * The move should already be executed.
     * <p>
     * It is assumed, that the move is made by player with the given token
     * and then it is checked, if there are two stones in a line with the one
     * stone that just got moved.
     * <p>
     * Only the destination of the move is relevant, so positions for
     * take or set moves should be saved there.
     *
     * @param field field
     * @param move move the player executed
     * @param player player
     * @param numberOfStones  number of stones horizontal
     * @return whether or not the given move created numberOfStones in a vertical row
     */    
    public static boolean checkMillVertical(Move possibleMove, GameField model, Token player, int numberOfStones) {
        int stones = 1; // the stone that was moved
        Point dest = possibleMove.getDest();
        Token legalUp = model.getLegalUp(dest.x, dest.y);
        //if (tokenIsEqual(legalUp, player)) {
        if (player.equals(legalUp)) {
            stones++;
        } else if (legalUp != null && numberOfStones == 3) {
            // there cannot be three in a row if there is a legal neighbor that is not the player
            return false;
        }

        int xOfNeighbor = model.getxOfNeighbor();
        int yOfNeighbor = model.getyOfNeighbor();
        Token legalUpUp = model.getLegalUp(xOfNeighbor, yOfNeighbor);
        //if (tokenIsEqual(legalUpUp, player)) {
        if (player.equals(legalUpUp)) {
            stones++;
        } else if (legalUpUp != null && numberOfStones == 3) {
            // there cannot be three in a row if there is a legal neighbor that is not the player
            return false;
        }

        if (stones >= numberOfStones) {
            return true;
        }


        Token legalDown = model.getLegalDown(dest.x, dest.y);
        //if (tokenIsEqual(legalDown, player)) {
        if (player.equals(legalDown)) {
            stones++;
        } else if (legalDown != null && numberOfStones == 3) {
            // there cannot be three in a row if there is a legal neighbor that is not the player
            return false;
        }

        if (stones >= numberOfStones) {
            return true;
        }

        xOfNeighbor = model.getxOfNeighbor();
        yOfNeighbor = model.getyOfNeighbor();
        Token legalDownDown = model.getLegalDown(xOfNeighbor, yOfNeighbor);
        //if (tokenIsEqual(legalDownDown, player)) {
        if (player.equals(legalDownDown)) {
            stones++;
        }

        return stones >= numberOfStones;
    }

    /**
     * returns true if the given move created a mill diagonal.
     * <p>
     * The move should already be executed.
     * <p>
     * It is assumed, that the move is made by player with the given token
     * and then it is checked, if there are two stones in a line with the one
     * stone that just got moved.
     * <p>
     * Only the destination of the move is relevant, so positions for
     * take or set moves should be saved there.
     * <p>
     * If <code>OptionObject.playWithDiagonalLines</code> is set false,
     * this method will always return false.
     *
     * @param field field
     * @param move move the player executed
     * @param player player
     * @param numberOfStones  number of stones horizontal
     * @return whether or not the given move created numberOfStones in a diagonal row
     */    
    public static boolean checkMillDiagonally(Move possibleMove, GameField model, Token player, int numberOfStones) {

        if (OptionObject.playWithDiagonalLines) {
            int x = (int) possibleMove.getDest().getX();
            int y = (int) possibleMove.getDest().getY();
            int stones = 1; // the stone that was moved
            Token place = null;
            // look left above
            for (int i = 0; i < 2; i++) {
                place = model.getLegalUpLeft(x - i, y - i);
                if (player.equals(place)) {
                    stones++;
                } else {
                    break;
                }
            }

            // look right down
            for (int i = 0; i < 2; i++) {
                place = model.getLegalDownRight(x + i, y + i);
                if (player.equals(place)) {
                    stones++;
                } else {
                    break;
                }
            }

            if (stones >= numberOfStones) {
                return true;
            }

            stones = 1; // the stone that was moved
            // look left down
            for (int i = 0; i < 2; i++) {
                place = model.getLegalDownLeft(x - i, y + i);
                if (player.equals(place)) {
                    stones++;
                } else {
                    break;
                }
            }
            // look right up
            for (int i = 0; i < 2; i++) {
                place = model.getLegalUpRight(x + i, y - i);
                if (player.equals(place)) {
                    stones++;
                } else {
                    break;
                }
            }

            return stones >= numberOfStones;
        }
        return false;
    }

    /**
     * returns if given player has stones on the board that are not in a mill.
     *
     * @param field field
     * @param enemyPlayer player (in general, this should be tested for the enemy)
     * @return if given player has stones on the board that are not in a mill
     */
    public static boolean hasFreeStones(GameField field, MyPlayerInterface enemyPlayer) {
        List<MyMove> allStones = getAllStones(field, enemyPlayer);
        for (int i = 0; i < allStones.size(); i++) {
            if (!createdMill(field, allStones.get(i), enemyPlayer.getColor())) {
                return true;
            }
        }
        return false;
    }

    /**
     * collects all stones of the given player and stores them in the dest of a move.
     * The source will be set to -1/-1
     *
     * @param field field
     * @param currentPlayer player
     * @return all stones of the given player on the given field
     */
    private static List<MyMove> getAllStones(GameField field, MyPlayerInterface currentPlayer) {
        List<MyMove> allStones = new ArrayList<MyMove>();
        int width = field.getWidth();
        int height = field.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field.getBelongsToGame(i, j)) { // to avoid instanciation of exception on miss
                    try {
                        if (field.getFieldStatus(i, j) == currentPlayer.getColor()) {
                            MyMove move = new MyMove();
                            move.setDest(i, j);
                            move.setSource(-1, -1);
                            allStones.add(move);
                        }
                    } catch (GameException ex) {
                    }
                }
            }
        }
        return allStones;
    }

    /**
     * checks if there are moves possible for the given player.
     *
     * @param field field
     * @param enemyPlayer player (in general, this should be tested for the enemy)
     * @return false if there are still moves possible, true otherwise
     */
    private static boolean noPossibleMove(GameField field, MyPlayerInterface enemyPlayer) {
        int width = field.getWidth();
        int height = field.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field.getBelongsToGame(i, j)) { // to avoid instanciation of exception on miss
                    try {
                        if (field.getFieldStatus(i, j) == enemyPlayer.getColor()) {
                            MyMove move = new MyMove();
                            move.setDest(i, j);
                            move.setSource(i, j);
                            if (isMovePossible(field, move, enemyPlayer)) {
                                return false;
                            }
                        }
                    } catch (GameException ex) {
                    }
                }
            }
        }
        return true;
    }

    /**
     * checks if the stone - which position should be stored in the given source
     * of the given move - can still move according to the rules.     *
     *
     * @param field field
     * @param move move (source should store position of stone to test for possible movement)
     * @param enemyPlayer player (in general, this should be tested for the enemy)
     * @return true if there is a legal move for the given stone, false otherwise
     */
    private static boolean isMovePossible(GameField field, MyMove move, MyPlayerInterface enemyPlayer) {
        if (enemyPlayer.getNumberOfStonesOnBoard() > 3) {
            List<Point> allNeighbors = field.getAllNeighbors(move.getSource().x, move.getSource().y);
            for (Point point : allNeighbors) {
                try {
                    if (field.getFieldStatus(point.x, point.y) == Token.EMPTY) {
                        return true;
                    }
                } catch (GameException ex) {
                }

            }
        } else {
            int width = field.getWidth();
            int height = field.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    move.setDest(i, j);
                    try {
                        if (isLegalMove(field, move, enemyPlayer)) {
                            return true;
                        }
                    } catch (GameException ex) {
                    }
                }
            }

        }
        return false;
    }
}
