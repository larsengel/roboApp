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

import application.mill.Controller.GameRuleController;
import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.ai.modell.PossibleMove;
import application.mill.ai.modell.Rating;

/**
 */
public class BlockEnemyMill extends AbstractRater implements IRater {

    @Override
    public int getRating(GameField field, PossibleMove possibleMove, Token player, boolean createdMill) {
        int rating = 0;
        GameField copy = field.getCopy();
        try {
            copy.applyMove(possibleMove, player);
        } catch (GameException ex) {
        }
        // check if there are 3 enemy stones in a row at the dest (the one of the player after the move and two of the enemy).
        if (GameRuleController.createdMill(copy, possibleMove, super.getEnemy(player))) {
            // blocking two in a row is always a good idea:
            rating = Rating.VERYLOW;

            // TODO clean up ugly code below... also, add diagonal
            if (((field.getLegalLeft(possibleMove.getDest().x, possibleMove.getDest().y) == super.getEnemy(player))
                    || (field.getLegalRight(possibleMove.getDest().x, possibleMove.getDest().y)) == super.getEnemy(player))
                    && (((field.getLegalUp(possibleMove.getDest().x, possibleMove.getDest().y) == super.getEnemy(player))
                    || (field.getLegalDown(possibleMove.getDest().x, possibleMove.getDest().y)) == super.getEnemy(player)))){
                rating = Rating.VERYHIGH;
            }
        }
        return rating;
    }

}
