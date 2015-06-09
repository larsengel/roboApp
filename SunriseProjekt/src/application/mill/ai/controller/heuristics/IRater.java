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

package application.mill.ai.controller.heuristics;

import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.ai.modell.PossibleMove;

/**
 * An interface that describes the rating of a move.
 * <p>
 * Implementations of this should never change any of the given parameters.
 */
public interface IRater {

    /**
     * returns the rating of this rating mechanism.
     * <p>
     * If the rating mechanism does not apply to the move, return 0.
     * <p>
     * It should be assumed that the move was already executed in the given
     * field.
     * 
     * @param field field
     * @param possibleMove move
     * @param player player that made the given move
     * @param createdMill whether or not the given move created a mill on the given field
     * @return rating (always as seen from the point of the ai)
     */
    int getRating(GameField field, PossibleMove possibleMove, Token player, boolean createdMill);

}
