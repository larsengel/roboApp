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
package application.mill.ai.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.OptionObject;
import application.mill.ai.modell.PossibleMove;

public abstract class AbstractMoveRater {
    
    /**
     * returns the oposite token.
     * If the token is <code>Token.BLACK</code>
     * <code>Token.WHITE</code> will be returned and inverted.
     * If the passed token is empty, empty will be returned.
     * @param token the current player
     * @return the enemy of this player
     */
    public static Token getEnemy(Token token) {
        if (token == Token.BLACK) {
            return Token.WHITE;
        } else if (token == Token.WHITE) {
            return Token.BLACK;
        }
        return Token.EMPTY;
    }

    /**
     * returns the highest rated move in the list.
     * <p>
     * If more than one have the same rating exists, a random one of those
     * with that value will be returned.
     *
     * @param moves list of moves
     * @return highest rated move
     */
    public Move getBestMove(List<PossibleMove> moves) {
        // print value of all possible moves
        if (OptionObject.DEBUG) {
            for (PossibleMove possibleMove : moves) {
                System.out.println(possibleMove.toString());
            }
        }

        // sort moves according to values
//        if (MAXDEPTH %2 == 0) {
            Collections.sort(moves, new HeuristicComparatorMove());
//        } else {
//            Collections.sort(moves, new HeuristicComparatorMoveLow());
//        }

        // collect all moves with the highest rating
        List<PossibleMove> best = new ArrayList<PossibleMove>();
            int i = 0;
            int firstValue = moves.get(0).getRating();
            while (i < moves.size() && moves.get(i).getRating() == firstValue) {
                      best.add(moves.get(i));
                      i++;
            }
            
            // TODO if more than one move has the same high rating, run all heuristic methods
            // against those moves and choose highest then. else chose random

        // return one of them (randomly)
        Random random = new Random();
        PossibleMove bestMove = best.get(random.nextInt(best.size()));
        if (OptionObject.DEBUG) {
            System.out.println("executing: " + bestMove.toString() + '\n');
        }
        return bestMove;
    }

    protected class HeuristicComparatorMove implements Comparator<PossibleMove> {

        @Override
        public int compare(PossibleMove t, PossibleMove t1) {
            return t1.getRating() - t.getRating();
        }
    }

    private class HeuristicComparatorMoveLow implements Comparator<PossibleMove> {

        @Override
        public int compare(PossibleMove t, PossibleMove t1) {
            return t.getRating() - t1.getRating();
        }
    }
}
