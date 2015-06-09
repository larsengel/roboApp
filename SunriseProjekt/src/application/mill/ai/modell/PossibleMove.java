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
 * A move that could be executed. 
 */
public class PossibleMove extends MyMove {

    private int rating;

    /**
     * constructs new (empty) possible move.
     */
    public PossibleMove() {
        super();
        this.rating= 0;
    }

    /**
     * returns the current rating of this move.
     *
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * adds the given amount to the accumulated rating.
     *
     * @param amount amount to add
     */
    public void addToRating(int amount) {
        this.rating += amount;
    }
    
    /**
     * sets the rating of this move to zero.
     */
    public void resetRating() {
        this.rating = Rating.ZERO;
    }

    /**
     * sets this rating.
     *
     * @param rating rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "PossibleMove{" + "rating=" + rating + "  " + super.toString() + "  "+ '}';
    }


}
