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

package application.mill.ai.modell;

import java.util.ArrayList;
import java.util.List;
import application.mill.Model.MyMove;

/**
 * This class stores the last n moves (of a certain depth) that produced
 * a cutoff.
 * The idea behind this is, that these moves a more likely than others 
 * to produce a cutoff for a node on the same level (killer heuristic).
 */
public class KillerMoves {

    private static final int SIZE = 2;

    private List<MyMove> killerMoves;

    /**
     * constructs new killer moves.
     */
    public KillerMoves() {
        this.killerMoves = new ArrayList<MyMove>();
    }
    
    /**
     * resets the killer moves.
     */
    public void reset() {
        killerMoves.clear();
    }

    /**
     * adds a move.
     * <p>
     * If this list already saves the maximal number of moves, one move will
     * be deleted first.
     * 
     * @param move move
     */
    public void addMove(MyMove move) {
        if (killerMoves.size() < SIZE) {
            killerMoves.add(move);
        } else {
            killerMoves.remove(0);
            killerMoves.add(move);
        }
    }

    /**
     * returns the saved moves.
     * 
     * @return the saved moves
     */
    public List<MyMove> getKillerMoves() {
        return killerMoves;
    }
}
