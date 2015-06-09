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
package application.mill.Model;

import java.awt.Point;
import application.mill.Interfaces.Move;

/**
 * A move from one point to the other.
 *
 */
public class MyMove extends Move {
    
    private Point source;
    private Point dest;

    /**
     * constructs new move.
     */
    public MyMove() {
        super();
    }

    /**
     * sets the source of this move.
     *
     * @param x x
     * @param y y
     */
    public void setSource(int x, int y) {
        //source = new Point(x, y);
        if (source == null) {
            source = new Point(x, y);
        } else {
            source.x = x;
            source.y = y;
        }

    }

    /**
     * sets the destination of this move.
     *
     * @param x x
     * @param y y
     */
    public void setDest(int x, int y) {
        //dest = new Point(x, y);
        if (dest == null) {
            dest = new Point(x, y);
        } else {
            dest.x = x;
            dest.y = y;
        }
    }

    @Override
    public Point getDest() {
        return dest;
    }

    @Override
    public Point getSource() {
        return source;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final MyMove other = (MyMove) obj;
        if (this.source == null && other.source != null) {
            return false;
        } else if (this.source != null && !this.source.equals(other.source)) {
            return false;
        }

        if (this.dest == null && other.dest != null) {
            return false;
        } else if (this.dest != null && !this.dest.equals(other.dest)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 43 * hash + (this.dest != null ? this.dest.hashCode() : 0);
        return hash;
    }

    

    @Override
    public String toString() {
        if (source != null && dest != null) {
            return "MyMove{" + "source=(" + source.x + ", " + source.y + "), dest=(" + dest.x + ", " + dest.y + ")'}'";
        } else if (dest != null) {
            return "MyMove{" + "source=(none), dest=(" + dest.x + ", " + dest.y + ")'}'";
        } else {
            return "MyMove{" + "source=(none), dest=(none)'}'";
        }
    }
}
