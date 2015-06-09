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
public class GameFieldNicerCode implements ModelInterface, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6240466320779610748L;
	// the code here is nicer than the one in gamefield, still,
    // from now on changes are to be made in GameField as it is a lot
    // faster than this class.

    private static final int SIZE = 7;
    // save the last changins for the gui updating!
    private int lastUpdatedColumn;
    private int lastUpdatedRow;
    private Token[][] field;
    private boolean[][] belongsToGame;

    /**
     * constructs a new game field.
     */
    public GameFieldNicerCode() {
        field = new Token[SIZE][SIZE];
        belongsToGame = new boolean[SIZE][SIZE];
        initField();
        initGetLegalNeighborsImpl();
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
     * @param x
     * @param y
     * @return
     * @throws GameException
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

        setFieldStatus(x, y, Token.EMPTY);
        return true;
    }

    @Override
    public boolean setFieldStatus(int x, int y, Token tok) throws GameException {
        if (!belongsToGame[x][y]) {
            throw new GameException("not a valid field.");
        }

        field[x][y] = tok;
        return true; 
    }

    @Override
    public Token getFieldStatus(int x, int y) throws GameException {
        if (!belongsToGame[x][y]) {
            throw new GameException("not a valid field.");
        }
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
    }

    private TestLegalNeighbor implLeft;
    private TestLegalNeighbor implRight;
    private TestLegalNeighbor implUp;
    private TestLegalNeighbor implDown;
    private TestLegalNeighbor implDownRight;
    private TestLegalNeighbor implDownLeft;
    private TestLegalNeighbor implUpRight;
    private TestLegalNeighbor implUpLeft;

    private void initGetLegalNeighborsImpl() {
        implLeft = new TestLegalNeighbor() {

            @Override
            public boolean terminationCondition(int tempX, int tempY) {
                return (tempX < 0) || (tempY == 3 && tempX == 3);
            }

            @Override
            public int getChangedX(int oldX) {
                return oldX - 1;
            }

            @Override
            public int getChangedY(int oldY) {
                return oldY;
            }
        };

        implRight = new TestLegalNeighbor() {

            @Override
            public boolean terminationCondition(int tempX, int tempY) {
                return (tempX >= getWidth()) || (tempY == 3 && tempX == 3);
            }

            @Override
            public int getChangedX(int oldX) {
                return oldX + 1;
            }

            @Override
            public int getChangedY(int oldY) {
                return oldY;
            }
        };

        implUp = new TestLegalNeighbor() {

            @Override
            public boolean terminationCondition(int tempX, int tempY) {
                return (tempY < 0) || (tempX == 3 && tempY == 3);
            }

            @Override
            public int getChangedX(int oldX) {
                return oldX;
            }

            @Override
            public int getChangedY(int oldY) {
                return oldY - 1;
            }
        };

        implDown = new TestLegalNeighbor() {

            @Override
            public boolean terminationCondition(int tempX, int tempY) {
                return (tempY >= getHeight()) || (tempX == 3 && tempY == 3);
            }

            @Override
            public int getChangedX(int oldX) {
                return oldX;
            }

            @Override
            public int getChangedY(int oldY) {
                return oldY + 1;
            }
        };

        implDownRight =  new TestLegalNeighbor() {

                @Override
                public boolean terminationCondition(int tempX, int tempY) {
                    return (tempY >= getHeight())
                            || (tempX >= getWidth())
                            || (tempX == 3 && tempY == 3);
                }

                @Override
                public int getChangedX(int oldX) {
                    return oldX + 1;
                }

                @Override
                public int getChangedY(int oldY) {
                    return oldY + 1;
                }
            };

        implUpRight = new TestLegalNeighbor() {

                @Override
                public boolean terminationCondition(int tempX, int tempY) {
                    return (tempY < 0)
                            || (tempX >= getWidth())
                            || (tempX == 3 && tempY == 3);
                }

                @Override
                public int getChangedX(int oldX) {
                    return oldX + 1;
                }

                @Override
                public int getChangedY(int oldY) {
                    return oldY - 1;
                }
            };


        implDownLeft = new TestLegalNeighbor() {

                @Override
                public boolean terminationCondition(int tempX, int tempY) {
                    return (tempY >= getHeight())
                            || (tempX < 0)
                            || (tempX == 3 && tempY == 3);
                }

                @Override
                public int getChangedX(int oldX) {
                    return (oldX - 1);
                }

                @Override
                public int getChangedY(int oldY) {
                    return oldY + 1;
                }
            };

        implUpLeft = new TestLegalNeighbor() {

                @Override
                public boolean terminationCondition(int tempX, int tempY) {
                    return (tempY < 0) || (tempX < 0)
                            || (tempX == 3 && tempY == 3);
                }

                @Override
                public int getChangedX(int oldX) {
                    return (oldX - 1);
                }

                @Override
                public int getChangedY(int oldY) {
                    return (oldY - 1);
                }
            };
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
        return getLegalNeighbor(x, y, implLeft);
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
        return getLegalNeighbor(x, y, implRight);
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
        return getLegalNeighbor(x, y, implUp);
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
        return getLegalNeighbor(x, y, implDown);
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
            return getLegalNeighbor(x, y, implDownRight);
        } else {
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
            return getLegalNeighbor(x, y, implUpRight);
        } else {
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

            return getLegalNeighbor(x, y, implDownLeft);

        } else {
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

            return getLegalNeighbor(x, y, implUpLeft);
        } else {
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
     * returns the status of a neighbor of the given field.
     * <p>
     * If there is no legal field , null will be returned.
     * If x/y is not a legal field, null will be returned as well.
     * <p>
     * As a side-effect <code>xOfNeighbor</code> and <code>yOfNeighbor</code>
     * will be set to the values of the neighbor which was found or to -1
     * if no neighbor was found.
     *
     * @param x x position
     * @param y y position
     * @param where determines to which side the method will check
     * @return legal field next to x/y position
     */
    private Token getLegalNeighbor(int x, int y, TestLegalNeighbor where) {
        
//        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()
//                || !belongsToGame[x][y]) {
//            xOfNeighbor = -1;
//            yOfNeighbor = -1;
//            return null;
//        }
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
            tempY = where.getChangedY(tempY);
            tempX = where.getChangedX(tempX);
            if (where.terminationCondition(tempX, tempY)) {
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
     * returns x value of the last legal neighbor found by getLegalNeighbor.
     * -1 if none was found.
     */
    public int getxOfNeighbor() {
        return xOfNeighbor;
    }

    /**
     * returns y value of the last legal neighbor found by getLegalNeighbor.
     * -1 if none was found.
     */
    public int getyOfNeighbor() {
        return yOfNeighbor;
    }

    private interface TestLegalNeighbor {

        boolean terminationCondition(int tempX, int tempY);

        int getChangedX(int oldX);

        int getChangedY(int oldY);
    }

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
     * @param field
     */
    public void setFieldArray(Token[][] field) {
        this.field = field;
    }

    /**
     * sets array.
     * <p>
     * Used for faster copying.
     *
     * @param belongsToGame
     */
    public void setBelongsToGameArray(boolean[][] belongsToGame) {
        this.belongsToGame = belongsToGame;
    }

    public GameFieldNicerCode getCopy() {
        GameFieldNicerCode gf = new GameFieldNicerCode();

        Token[][] newField = new Token[field.length][];
        for (int i = 0; i < field.length; i++) {
            System.arraycopy(field[i], 0, newField[i] = new Token[field[i].length], 0, field[i].length);
        }
        gf.setFieldArray(newField);

        return gf;
    }

    /**
     * returns an instance of <code>GameField</code> which fields
     * will be set the same as the one given.
     * <p>
     * The result will be the same as the one of the getCopy method, but this
     * method is static and a lot slower.
     */
    public static GameFieldNicerCode getSlowCopy(ModelInterface model) {
        GameFieldNicerCode gf = new GameFieldNicerCode();
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
	public boolean removeStoneAtPosition(Playerinterface player, int x, int y)
			throws application.mill.Interfaces.GameException {
		// TODO Automatisch generierter Methodenstub
		return false;
	}*/
}
