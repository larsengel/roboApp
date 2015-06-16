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

public class AbstractRater {

    /**
     * returns the oposite token.
     * If the token is <code>Token.BLACK</code>
     * <code>Token.WHITE</code> will be returned and inverted.
     * If the passed token is empty, empty will be returned.
     * @param token the current player
     * @return the enemy of this player
     */
    public Token getEnemy(Token token) {
        if (token == Token.BLACK) {
            return Token.WHITE;
        } else if (token == Token.WHITE) {
            return Token.BLACK;
        }
        return Token.EMPTY;
    }
}
