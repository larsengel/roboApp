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

import application.mill.Interfaces.ModelInterface;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.MyPlayerInterface;

/**
 * This is a reduced implementation of MyPlayerInterface.
 */
public class SimplePlayer implements MyPlayerInterface{
    
    private int numberOfStones;
    private Token color;
    
    public SimplePlayer(int numberOfStones){
        this.numberOfStones = numberOfStones;
    }

    @Override
    public int getNumberOfStonesOnBoard() {
        return numberOfStones;
    }

    @Override
    public int getNumberOfStonesLeftToPlace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void lowerNumberOfStonesOnBoard() {
        numberOfStones--;
    }

    @Override
    public void raiseNumberOfStonesOnBoard() {
        numberOfStones++;
    }

    @Override
    public void lowerNumberOfStonesLeftToPlace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Move getNextSetMove(ModelInterface model, int turnCounter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Move getNextMove(ModelInterface model, int turnCounter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Move getRemoveMove(ModelInterface model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setColor(Token spielerfarbe) {
        this.color = spielerfarbe;
    }

    @Override
    public Token getColor() {
        return color;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
   public SimplePlayer getCopy(){
       SimplePlayer temp = new SimplePlayer(this.getNumberOfStonesOnBoard());
       temp.setColor(this.getColor());
       return temp;
   }

    public void setNumberOfStones(int numberOfStones) {
        this.numberOfStones = numberOfStones;
    }

    public Move getNextSetMove(ModelInterface model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Move getNextMove(ModelInterface model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Move getRemoveMove(ModelInterface model, int time) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
