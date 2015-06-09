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

import application.mill.Model.MyMove;

/**
 */
public class CutOffHistory {

    // TODO this class is not used for now. complete it and use it.
    
    private KillerMoves[] killerMoves;

    /**
     * constructs new cutoff history with the given depth.
     * 
     * @param depth 
     */
    public CutOffHistory(int depth) {
        killerMoves = new KillerMoves[depth];
    }

    /**
     * init killer moves lists for all depth.
     */
    private final void initKillerMoves() {
        for (int i = 0; i < killerMoves.length; i++) {
            killerMoves[i] = new KillerMoves();
        }
    }

    /**
     * adds given move to killer moves of the given depth.
     * <p>
     * May throw away an old killer move of that depth.
     * 
     * @param depth depth
     * @param move move 
     */
    public void addMove(int depth, MyMove move) {
        killerMoves[depth].addMove(move);
    }

}
