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

/**
 * This class contains values which may be used to rate moves.
 *
 */
public final class Rating {

    /**
     * a very high value which may be used to indicate a win.
     * It is not guaranteed that this rating will always be the highest or lead
     * to a move instantly.
     */
    public static final int WIN = 5000000;

    /**
     * a very high value which may be used to indicate a mill.
     * It is not guaranteed that this rating will always be the highest or lead
     * to a move instantly.
     */
    public static final int MILL = 10000;

      /**
      * this value is the highest value.
      */
     public static final int VERYHIGH = 1000;
      /**
      * this value is lower than <code>VERYHIGH</code>,
      * but higher than <code>MEDIUM</code>.
      */
     public static final int HIGH = 500;

      /**
      * this value is lower than <code>HIGH</code>,
      * but higher than <code>LOW</code>.
      */
     public static final int MEDIUM = 100;
         /**
      * this value is lower than <code>MEDIUM</code>,
      * but higher than <code>VERYLOW</code>.
      */
     public static final int LOW = 40;
         /**
      * this value is lower than <code>LOW</code>,
      * but higher than <code>ZERO</code>.
      */
     public static final int VERYLOW = 10;
     /**
      * the lowest value.
      */
     public static final int ZERO = 0;
}
