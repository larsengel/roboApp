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
package application.mill.Model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import application.mill.Interfaces.GameException;
import application.mill.Interfaces.ModelInterface;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Playerinterface;
import application.mill.Interfaces.Token;

/**
 * This class holds a game field for the game mill.
 *
 * @version 1.6
 */
public class GameField implements ModelInterface, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2077692122078833079L;
	// comment: the get legalX methods basically all have the same code.
    // handling this with interfaces produces much nicer code,
    // but it slows down the method by almost 50%!

    private static final int SIZE = 7;
    // save the last changins for the gui updating!
    private int lastUpdatedColumn;
    private int lastUpdatedRow;
    private Token[][] field;
    private boolean[][] belongsToGame;
    // TODO die neuen interfaces sind da :( turnsWithoutMill muss nicht nur hier, sondern eigentlich auch im player gezaehlt werden...
    private int turnsWithoutMill; // a turn is defined as a move move (not place or take)

    /**
     * constructs a new game field.
     */
    public GameField() {
        field = new Token[SIZE][SIZE];
        belongsToGame = new boolean[SIZE][SIZE];
        initField();
    }
    
    public GameField(GameField gameField){
    	field = gameField.field;
    	belongsToGame = gameField.belongsToGame;
    	initField();
    }

    /**
     * returns if the given point actually belongs to the game (meaning that
     * a player may - under certain circumstances - place a stone there).
     *
     * @param x x
     * @param y y
     * @return if the given point actually belongs to the game
     */
    public boolean getBelongsToGame(int x, int y) {
        return belongsToGame[x][y];
    }

    @Override
    public int getHeight() {
        return SIZE;
    }

    @Override
    public int getWidth() {
        return SIZE;
    }

    /**
     * initiate the two dimensional array with
     * <code> Token.EMPTY </code>.
     */
    private void initField() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                field[x][y] = Token.EMPTY;

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
     * returns the last updated column.
     *
     * @return the last updated column
     */
    public int getLastUpdatedColumn() {
        return lastUpdatedColumn;
    }

    /**
     * returns the last updated row.
     * 
     * @return the last updated row
     */
    public int getLastUpdatedRow() {
        return lastUpdatedRow;
    }

    /**
     * removes a stone of the given player at the x/y position.
     * Returns true if this was possible, false otherwise.
     *
     * @param player the player to take a stone from
     * @param x x
     * @param y y
     * @return could remove stone?
     * @throws GameException GameException
     */
    @Override
    public boolean removeStoneAtPosition(Playerinterface player, int x, int y) throws GameException {
        if (!belongsToGame[x][y]) {
            throw new GameException("not a valid field.");
        }

        Token enemy;
        if (player.getColor() == Token.BLACK) {
            enemy = Token.WHITE;
        } else {
            enemy = Token.BLACK;
        }

        if (getFieldStatus(x, y) == Token.EMPTY
                || getFieldStatus(x, y) == enemy) {
            return false;

        }

        turnsWithoutMill = 0;
        setFieldStatus(x, y, Token.EMPTY);
        return true;
    }

    @Override
    public boolean setFieldStatus(int x, int y, Token tok) throws GameException {
        if (!belongsToGame[x][y]) {
            throw new GameException("not a valid field.");
        }

        field[x][y] = tok;
        return true; // TODO CHECK check return. (da steht nicht, was man zurueckgeben soll, also geb ich immer true zurueck...)
    }

    @Override
    public Token getFieldStatus(int x, int y) throws GameException {
        if (!belongsToGame[x][y]) {
            throw new GameException("not a valid field.");
        }
        return field[x][y];
    }
    
    public Token getToken(int x, int y){
    	return field[x][y];
    }

    /**
     * applies the given move for the given player.
     * <p>
     * A source as well as a destination must be given.
     *
     * @param move move
     * @param player player
     * @throws GameException GameException
     */
    public void applyMove(Move move, Token player) throws GameException {
        setFieldStatus(move.getSource().x, move.getSource().y, Token.EMPTY);
        setFieldStatus(move.getDest().x, move.getDest().y, player);
        turnsWithoutMill++;
    }

    /**
     * returns the status of the next legal field to the left of
     * the given position.
     * <p>
     * If there is no legal field left of it, null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field left of x/y position
     */
    public Token getLegalLeft(int x, int y) {
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempX = tempX - 1;
            if ((tempX < 0) || (tempY == 3 && tempX == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
    }

    /**
     * returns the status of the next legal field to the right of
     * the given position.
     * <p>
     * If there is no legal field right of it, null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field right of x/y position
     */
    public Token getLegalRight(int x, int y) {
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempX = tempX + 1;
            if ((tempX >= getWidth()) || (tempY == 3 && tempX == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
    }

    /**
     * returns the status of the next legal field upwards of
     * the given position.
     * <p>
     * If there is no legal field above it, null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field above x/y position
     */
    public Token getLegalUp(int x, int y) {
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempY = tempY - 1;
            if ((tempY < 0) || (tempX == 3 && tempY == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
    }

    /**
     * returns the status of the next legal field below the given position.
     * <p>
     * If there is no legal field below it, null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field below x/y position
     */
    public Token getLegalDown(int x, int y) {
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempY = tempY + 1;
            if ((tempY >= getHeight()) || (tempX == 3 && tempY == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
    }

    /**
     * returns the status of the next legal field diagonally
     * lower-right of the given position.
     * <p>
     * If there is no legal field , null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field diagonally lower-right of x/y position
     */
    public Token getLegalDownRight(int x, int y) {
        //only if the x posi. is equals the y posi. it is allowed to look up left!
        if (x == y) {
                    // it is basically never <0 or >= width, so don't waste time checking
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempY = tempY + 1;
            tempX = tempX + 1;
            if ((tempY >= getHeight())
                            || (tempX >= getWidth())
                            || (tempX == 3 && tempY == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
        } else {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
    }

    /**
     * returns the status of the next legal field diagonally
     * upper-right of the given position.
     * <p>
     * If there is no legal field , null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field diagonally upper-right of x/y position
     */
    public Token getLegalUpRight(int x, int y) {
        if (x == 6 - y) {
        // it is basically never <0 or >= width, so don't waste time checking
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempY = tempY - 1;
            tempX = tempX + 1;
            if ((tempY < 0)
                            || (tempX >= getWidth())
                            || (tempX == 3 && tempY == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
        } else {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
            return null;
        }
    }

    /**
     * returns the status of the next legal field diagonally
     * lower-left of the given position.
     * <p>
     * If there is no legal field , null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field diagonally lower-left of x/y position
     */
    public Token getLegalDownLeft(int x, int y) {
        if (x == 6 - y) {
        // it is basically never <0 or >= width, so don't waste time checking
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
        tempY = tempY + 1;
        tempX = tempX - 1;
            if ((tempY >= getHeight())
                            || (tempX < 0)
                            || (tempX == 3 && tempY == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
        } else {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
    }

    /**
     * returns the status of the next legal field diagonally
     * upper-left of the given position.
     * <p>
     * If there is no legal field , null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     *
     * @param x x position
     * @param y y position
     * @return legal field diagonally upper-left of x/y position
     */
    public Token getLegalUpLeft(int x, int y) {
        //only if the x posi. is equals the y posi. it is allowed to look up left!
        if (x == y) {
        // it is basically never <0 or >= width, so don't waste time checking
        try {
            if (!belongsToGame[x][y]) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            }
        } catch (ArrayIndexOutOfBoundsException iob) {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
        int tempY = y;
        int tempX = x;
        while (true) {
            tempY = tempY - 1;
            tempX = tempX - 1;
            if ((tempY < 0) || (tempX < 0)
                            || (tempX == 3 && tempY == 3)) {
                xOfNeighbor = -1;
                yOfNeighbor = -1;
                return null;
            } else {
                if (belongsToGame[tempX][tempY]) { // to avoid instanciation of exception on miss
                    xOfNeighbor = tempX;
                    yOfNeighbor = tempY;
                    return field[tempX][tempY];
                }
            }
        }
        } else {
            xOfNeighbor = -1;
            yOfNeighbor = -1;
            return null;
        }
    }
    /**
     * x value of the last legal neighbor found by getLegalNeighbor.
     * -1 if none was found.
     */
    private int xOfNeighbor;
    /**
     * y value of the last legal neighbor found by getLegalNeighbor.
     * -1 if none was found.
     */
    private int yOfNeighbor;

    /**
     * returns x value of the last legal neighbor found by getLegalNeighbor.
     * -1 if none was found.
     * 
     * @return x
     */
    public int getxOfNeighbor() {
        return xOfNeighbor;
    }

    /**
     * returns y value of the last legal neighbor found by getLegalNeighbor.
     * -1 if none was found.
     * 
     * @return y
     */
    public int getyOfNeighbor() {
        return yOfNeighbor;
    }

    /**
     * returns all legal neighboring fields to the one defined by the
     * given x/y coordinates.
     * 
     * @param x x
     * @param y y
     * @return all legal neighboring fields 
     */
    public List<Point> getAllNeighbors(int x, int y) {
        List<Point> neighbors = new ArrayList<Point>();

        if (getLegalDown(x, y) != null) {
            neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
        }
        if (getLegalLeft(x, y) != null) {
            neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
        }
        if (getLegalRight(x, y) != null) {
            neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
        }
        if (getLegalUp(x, y) != null) {
            neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
        }

        if (OptionObject.playWithDiagonalLines) {
            if (getLegalDownLeft(x, y) != null) {
                neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
            }
            if (getLegalDownRight(x, y) != null) {
                neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
            }
            if (getLegalUpLeft(x, y) != null) {
                neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
            }
            if (getLegalUpRight(x, y) != null) {
                neighbors.add(new Point(getxOfNeighbor(), getyOfNeighbor()));
            }
        }
        return neighbors;

    }

    /**
     * sets array.
     * <p>
     * Used for faster copying.
     *
     * @param field field
     */
    public void setFieldArray(Token[][] field) {
        this.field = field;
    }

    /**
     * sets array.
     * <p>
     * Used for faster copying.
     *
     * @param belongsToGame belongsToGame
     */
    public void setBelongsToGameArray(boolean[][] belongsToGame) {
        this.belongsToGame = belongsToGame;
    }

    public int getTurnsWithoutMill() {
        return turnsWithoutMill;
    }

    public void setTurnsWithoutMill(int turnsWithoutMill) {
        this.turnsWithoutMill = turnsWithoutMill;
    }

    /**
     * returns a hard copy of this game field.
     * 
     * @return hard copy
     */
    public GameField getCopy() {
        GameField gf = new GameField();

        Token[][] newField = new Token[field.length][];
        for (int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, newField[i] = new Token[field[i].length], 0, field[i].length);
        }
        gf.setFieldArray(newField);

        gf.setTurnsWithoutMill(turnsWithoutMill);

        return gf;
    }

    /**
     * returns an instance of <code>GameField</code> which fields
     * will be set the same as the one given.
     * <p>
     * The result will be the same as the one of the getCopy method, but this
     * method is static and a lot slower.
     * <p>
     * Also, the turnsWithoutMill field will not be set.
     * 
     * @param model model
     * @return copy of model
     */
    public static GameField getSlowCopy(ModelInterface model) {
        GameField gf = new GameField();
        int height = model.getHeight();
        int width = model.getWidth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    gf.setFieldStatus(i, j, model.getFieldStatus(i, j));
                } catch (GameException ex) {
                }
            }

        }
        return gf;
    }
/*
	@Override
	public boolean removeStoneAtPosition(Playerinterface player, int x, int y) {
		// TODO Automatisch generierter Methodenstub
		return false;
	}*/
}
