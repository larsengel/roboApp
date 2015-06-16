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

import application.mill.Interfaces.Token;

/**
 */
public abstract class AbstractPlayer implements MyPlayerInterface {

    private Token tok;
    private String name;
    private int numberOfStonesOnBoard;
    private int numberOfStonesLeftToPlace;

    /**
    /**
     * constructs a new player.
     *
     * @param name name
     * @param numberOfStones number of stones the player can place on the board
     */
    public AbstractPlayer(String name, int numberOfStones) {
        this.name = name;
        this.numberOfStonesLeftToPlace = numberOfStones;
    }
    
    /**
     * sets the color of the player.
     * WARNING: <code> Token.EMPTY </code> is not allowed
     * and throws an exception.
     * @param spielerfarbe the color of this player
     */
    @Override
    public void setColor(Token spielerfarbe) {
//        if(spielerfarbe == Token.EMPTY){
//            throw new Error("EMPTY is not allowed");
//        } else {
            this.tok = spielerfarbe;
//        }
    }

    /**
     * returns the color of the player.
     * 
     * @return the color
     */
    @Override
    public Token getColor() {
        return tok;
    }

    /**
     * returns the name of the player.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the player.
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractPlayer)) {
            return false;
        }
        final AbstractPlayer other = (AbstractPlayer) obj;
        if (this.tok != other.getColor()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.tok != null ? this.tok.hashCode() : 0);
        hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public int getNumberOfStonesOnBoard() {
        return numberOfStonesOnBoard;
    }

    @Override
    public int getNumberOfStonesLeftToPlace() {
        return numberOfStonesLeftToPlace;
    }

    @Override
    public void lowerNumberOfStonesOnBoard() {
        numberOfStonesOnBoard--;
    }

    @Override
    public void lowerNumberOfStonesLeftToPlace() {
        numberOfStonesLeftToPlace--;
    }

    @Override
    public void raiseNumberOfStonesOnBoard() {
        numberOfStonesOnBoard++;
    }
}
