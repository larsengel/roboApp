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

import application.mill.Interfaces.Playerinterface;

/**
 * An extended interface for a player which specifies methods
 * needed for the number of stones.
 */
public interface MyPlayerInterface extends Playerinterface {

    /**
     * returns number of stones player has on the board.
     *
     * @return number of stones player has on the board
     */
    int getNumberOfStonesOnBoard();

    /**
     * returns number of stones player can still place on the board.
     *
     * @return number of stones player can still place on the board
     */
    int getNumberOfStonesLeftToPlace();

    /**
     * lowers number of stones player has on the board by one.
     */
    void lowerNumberOfStonesOnBoard();

    /**
     * raises number of stones player has on the board by one.
     */
    void raiseNumberOfStonesOnBoard();

    /**
     * lowers number of stones player can still place on the board.
     */
    void lowerNumberOfStonesLeftToPlace();
}
