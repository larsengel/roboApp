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
package application.mill.ai;

import application.mill.Interfaces.GameException;
import application.mill.Interfaces.ModelInterface;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.AbstractPlayer;
import application.mill.Model.GameField;
import application.mill.Model.MyMove;
import application.mill.Model.MyPlayerInterface;
import application.mill.ai.modell.SimplePlayer;
import application.mill.ai.controller.AbstractMoveRater;
import application.mill.ai.controller.MoveRater;
import application.mill.ai.controller.SetMoveRater;

public class ComputerPlayer extends AbstractPlayer implements MyPlayerInterface {

    private SetMoveRater setMoveRater;
    private MoveRater moveRater;
    private SimplePlayer enemy;
    private SimplePlayer player;
    private int turnCounter;
    private int oldStonesOnBoard;

    public ComputerPlayer() {
        this("TIDE", 18);
        turnCounter = 0;
    }

    /**
     * constructs new computer player.
     * 
     * @param name name
     * @param numberOfStones numberOfStones
     * @param enemy enemy of this player
     */
    public ComputerPlayer(String name, int numberOfStones) {
        super(name, numberOfStones);
        enemy = new SimplePlayer(numberOfStones);
        player = new SimplePlayer(numberOfStones);
        setMoveRater = new SetMoveRater();
        moveRater = new MoveRater(player, enemy);
        this.oldStonesOnBoard = 0;
    }

    @Override
    public void setColor(Token spielerfarbe) {
        super.setColor(spielerfarbe);
        player.setColor(spielerfarbe);
        enemy.setColor(AbstractMoveRater.getEnemy(spielerfarbe));
    }

//    @Override
//    public Move getNextMove(ModelInterface model, int turnCounter) {
//        player.setNumberOfStones(this.getNumberOfStonesOnBoard());
//        return moveRater.getNextMove(makeGameField(model, turnCounter));
//    }
//
//    @Override
//    public Move getNextSetMove(ModelInterface model, int turnCounter) {
//        player.setNumberOfStones(this.getNumberOfStonesOnBoard());
//        return setMoveRater.getNextSetMove(makeGameField(model, turnCounter), turnCounter, super.getColor());
//    }
//
//    @Override
//    public Move getRemoveMove(ModelInterface model) {
//        player.setNumberOfStones(this.getNumberOfStonesOnBoard());
//        Move move = moveRater.getNextTakeMove(makeGameField(model, 0));
//        enemy.lowerNumberOfStonesOnBoard();
//        return move;
//    }
    @Override
    public Move getNextSetMove(ModelInterface model, int time) throws application.mill.Interfaces.GameException {
        return getNextSetMove(model);
    }

    //@Override
    public Move getNextSetMove(ModelInterface model) throws application.mill.Interfaces.GameException {
        player.setNumberOfStones(this.getNumberOfStonesOnBoard());
        return setMoveRater.getNextSetMove(makeGameField(model, 0), super.getColor());
    }

    //@Override
    public Move getNextMove(ModelInterface model) throws application.mill.Interfaces.GameException {
        return getNextMove(model, 5000);
    }

    @Override
    public Move getNextMove(ModelInterface model, int time) throws application.mill.Interfaces.GameException {
        adjustTurnCounter(model);
        // TODO adjust this so that it is not turnCounter but time. get the number of turns without mill from somewhere else...
        player.setNumberOfStones(this.getNumberOfStonesOnBoard());

       // System.out.println("field counter: " + ((GameField) model).getTurnsWithoutMill() + " ai counter: " + turnCounter);
        Move nextMove = moveRater.getNextMove(makeGameField(model, turnCounter), time);
        turnCounter++;
        //  int turnCounter = 0; // <-- here real turns without mill needed
        return nextMove;
    }

    @Override
    public Move getRemoveMove(ModelInterface model) throws application.mill.Interfaces.GameException {
        return getRemoveMove(model, MoveRater.MAXTIME);
    }

    //@Override
    public Move getRemoveMove(ModelInterface model, int time) throws application.mill.Interfaces.GameException {
        // TODO adjust this so that it is not turnCounter but time.
        player.setNumberOfStones(this.getNumberOfStonesOnBoard());
        // int turnCounter = 0; // <-- here real turns without mill needed
        turnCounter = 0;
        Move move = moveRater.getNextTakeMove(makeGameField(model, turnCounter), time);
        enemy.lowerNumberOfStonesOnBoard();

        // TODO only for manager: set source to dest as manager reads from source
        ((MyMove) move).setSource(move.getDest().x, move.getDest().y);
        return move;
    }

    /**
     * creates a <code>GameField</code> from the given model.
     * <p>
     * This serves two purposes: The controller may change the field at will,
     * and also we make sure that we can pass a <code>GameField</code> to it.
     *
     * @param model model
     * @return gameField gameField
     * @throws application.mill.Controller.GameException 
     */
    private GameField makeGameField(ModelInterface model, int turnsWithoutMill) throws application.mill.Interfaces.GameException {
        GameField gf = new GameField();
        int width = model.getWidth();
        int height = model.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    gf.setFieldStatus(i, j, model.getFieldStatus(i, j));
                } catch (GameException ex) {
                }
            }

        }
        gf.setTurnsWithoutMill(turnsWithoutMill);
        return gf;
    }
    
    
    private void adjustTurnCounter(ModelInterface field) throws application.mill.Interfaces.GameException{
        int newStonesOnBoard = countAiStones(field);
        if(this.oldStonesOnBoard == newStonesOnBoard){
            turnCounter++;
        } else {
            this.oldStonesOnBoard = newStonesOnBoard;
            turnCounter = 0;
        }
    }
    
    /**
     * counts the stones of the ai on the gamefield.
     * @param field the field to count on
     * @return the number of stones
     * @throws application.mill.Controller.GameException 
     */
    private int countAiStones(ModelInterface field) throws application.mill.Interfaces.GameException {
        int stones = 0;
        int width = field.getWidth();
        int height = field.getHeight();
        for (int column = 0; column < width; column++) {
            for (int line = 0; line < height; line++) {
                try {
                    if(field.getFieldStatus(column, line).equals(this.getColor())){
                        stones++;
                    }
                } catch (GameException ex) {
                }
            }
        }
        return stones;
    }
}
