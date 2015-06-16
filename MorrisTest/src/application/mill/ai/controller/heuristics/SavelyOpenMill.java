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

import java.awt.Point;
import java.util.List;
import application.mill.Controller.GameRuleController;
import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.ai.modell.PossibleMove;
import application.mill.ai.modell.Rating;

public class SavelyOpenMill extends AbstractRater implements IRater {

    @Override
    public int getRating(GameField field, PossibleMove possibleMove, Token player, boolean createdMill) {
        // check if the move starts from a mill:
        PossibleMove reverseMove = new PossibleMove();
        reverseMove.setDest(possibleMove.getSource().x, possibleMove.getSource().y);
        reverseMove.setSource(possibleMove.getDest().x, possibleMove.getDest().y);
        // do not execute move, as the situation is already as if it would have been executed
        if (GameRuleController.createdMill(field, reverseMove, player)) {
            // check that enemy cannot close mill of ai:
            List<Point> allNeighbors = field.getAllNeighbors(possibleMove.getSource().x, possibleMove.getSource().y);
            for (Point point : allNeighbors) {
                try {
                    if (field.getFieldStatus(point.x, point.y) == getEnemy(player)) {
                        // enemy can close our mill. not good.
                        return -(Rating.MILL / 2);
                    } else {
                        // enemy can not close our mill. good.
                        return Rating.MILL / 2;
                    }
                } catch (GameException ex) {
                }
            }
            return 0;
        } else {
            return 0;
        }
    }
}
