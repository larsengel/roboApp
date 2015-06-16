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

import java.util.logging.Level;
import java.util.logging.Logger;
import application.mill.Controller.GameRuleController;
import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.ai.modell.PossibleMove;
import application.mill.ai.modell.Rating;

/**
 */
public class Catch22 implements IRater {

    @Override
    public int getRating(GameField field, PossibleMove possibleMove, Token player, boolean createdMill) {
        // move makes mill and stone is already in a mill:
        PossibleMove temp = new PossibleMove();
        temp.setDest(possibleMove.getSource().x, possibleMove.getSource().y);
        temp.setSource(possibleMove.getDest().x, possibleMove.getDest().y);
        GameField copy = field.getCopy();
        try {
            copy.applyMove(temp, player);
        } catch (GameException ex) {
            Logger.getLogger(Catch22.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (createdMill && GameRuleController.createdMill(copy, temp, player)) {
                return Rating.MILL * 10;            
        }
        return 0;
    }
}
