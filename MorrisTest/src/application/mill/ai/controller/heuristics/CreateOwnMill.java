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
import application.mill.ai.modell.Rating;

/**
 */
public class CreateOwnMill implements IRater {

    @Override
    public int getRating(GameField field, PossibleMove possibleMove, Token player, boolean createdMill) {
        if (createdMill) {
            return Rating.MILL * 3;
        } else {
            return 0;
        }
    }
}
