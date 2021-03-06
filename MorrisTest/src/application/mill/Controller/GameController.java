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

package application.mill.Controller;

//import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.Buffer;
import application.mill.Model.GameField;
import application.mill.Model.MyPlayerInterface;
import application.mill.Model.Queue;
import application.mill.ai.ComputerPlayer;
import application.mill.ai.controller.MoveRater;

/**
 * This class holds the game loop and is responsible for the processing
 * of player input.
 *
 * @version 1.1.2
 */
public class GameController implements Runnable {

    /**
     * whether or not it is possible to move diagonally.
     */
    private static int TOTALNUMBERSTONES;

    /**
     * possible states the game may have.
     */
    public enum GameState {

        /** someone won. */
        WIN,
        /** draw. */
        DRAW,
        /** time to place stones on board. */
        PLACE,
        /** time to move stones around. */
        MOVE,
        /** time to take a stone of the enemy. */
        TAKE
    }
    private GameState currentState;
    private GameState stateBeforeTake;
    private MyPlayerInterface playerOne;
    private MyPlayerInterface playerTwo;
    private MyPlayerInterface currentPlayer;
    private Buffer<Integer[]> userInput;
    private Queue<GameField> fieldBuffer;
    private Queue<String> messageBuffer;
    private GameField field;
    private boolean isP1Com;
    private boolean isP2Com;
    private boolean running;

    /**
     * constructs new game controller.
     * 
     * @param gmBuff gmBuff
     * @param userInput userInput
     * @param messageBuffer messageBuffer
     * @param numberOfStones numberOfStones 
     */
    public GameController(Queue<GameField> gmBuff, Buffer<Integer[]> userInput,
            Queue<String> messageBuffer, int numberOfStones) {
        TOTALNUMBERSTONES = numberOfStones;
        this.userInput = userInput;
        this.fieldBuffer = gmBuff;
        this.messageBuffer = messageBuffer;
        isP1Com = false;
        isP2Com = true;
        field = new GameField();
        running = true;
    }
    
    public GameState getGameState(){
    	return currentState;
    }
    
    public MyPlayerInterface getCurrentPlayer(){
    	return currentPlayer;
    }

    /**
     * instantiates the players.
     */
    private void makePlayers() {
        if (isP1Com) {
            playerOne = new ComputerPlayer("AI", (TOTALNUMBERSTONES / 2));
        } else {
            playerOne = new HumanInputController(userInput, true, "Player 1", TOTALNUMBERSTONES / 2);
        }
        if (isP2Com) {
            playerTwo = new ComputerPlayer("AI", (TOTALNUMBERSTONES / 2));
        } else {
            playerTwo = new HumanInputController(userInput, false, "Player 2", TOTALNUMBERSTONES / 2);
        }
//        if (playerOne instanceof ComputerPlayer) {
//            ((ComputerPlayer) playerOne).setEnemy(playerTwo);
//        }
        playerOne.setColor(Token.BLACK);
        playerTwo.setColor(Token.WHITE);
        currentPlayer = playerOne;
    }

    /**
     * starts the game.
     * @throws GameException 
     * @throws IOException 
     */
    public void play() throws GameException, IOException {
        while (running) {
            switch (currentState) {                
                case WIN:
                    MyPlayerInterface enemy;
                    if (currentPlayer.equals(playerOne)) {
                        enemy = playerTwo;
                    } else {
                        enemy = playerOne;
                    }
                    sendToGui(enemy.getColor() + " won ");
                    running = false;
                    break;
                
                case DRAW:
                    sendToGui(" draw ");
                    running = false;
                    break;
                
                case PLACE:
                    if (!checkEndPlace()) {
                        sendToGui(currentPlayer.getColor() + "'s turn to "
                                + currentState);
                        if (currentPlayer.getColor().equals(Token.BLACK)) {
	                        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	                	    int x = Integer.parseInt(bufferRead.readLine());
	                	    bufferRead = new BufferedReader(new InputStreamReader(System.in));
	                	    int y = Integer.parseInt(bufferRead.readLine());
	                	    this.userInput.write(new Integer[] { y, x });
                        }
                	    placeStones();              
                        drawToGui();
                    }
                    break;
                
                case MOVE:
                    sendToGui(currentPlayer.getColor() + "'s turn to " + currentState);
                    if (checkEndMove()) {
                        break;
                    }
                    if (currentPlayer.getColor().equals(Token.BLACK)) {
                        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                	    int x_1 = Integer.parseInt(bufferRead.readLine());
                	    bufferRead = new BufferedReader(new InputStreamReader(System.in));
                	    int y_1 = Integer.parseInt(bufferRead.readLine());
                	    bufferRead = new BufferedReader(new InputStreamReader(System.in));
                	    int x_2 = Integer.parseInt(bufferRead.readLine());
                	    bufferRead = new BufferedReader(new InputStreamReader(System.in));
                	    int y_2 = Integer.parseInt(bufferRead.readLine());
                	    this.userInput.write(new Integer[] { y_1, x_1 });
                	    this.userInput.write(new Integer[] { y_2, x_2 });
                    }
                    moveStones();
                    drawToGui();
                    break;
                case TAKE:
                    sendToGui(currentPlayer.getColor() + "'s turn to " + currentState);
                    if (currentPlayer.getColor().equals(Token.BLACK)) {
                        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                	    int x = Integer.parseInt(bufferRead.readLine());
                	    bufferRead = new BufferedReader(new InputStreamReader(System.in));
                	    int y = Integer.parseInt(bufferRead.readLine());
                	    this.userInput.write(new Integer[] { y, x });
                    }
                    takeStone();
                    drawToGui();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * kills the thread running this controller.
     */
    public void kill() {
        running = false;
    }
    
    /**
     * constructs and starts the thread running this controller.
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * should be called when it is time to place stones on the board.
     * <p>
     * The move of the current player is read, verified and executed.
     * If a mill is created the current state will be set to TAKE.
     * Otherwise the current player will be changed to the next player if the move
     * could be verified.
     * If the move could not be verified, the current state will not be changed,
     * not will the player be changed.
     * @throws GameException 
     */
    private void placeStones() throws GameException {
        Move move = currentPlayer.getNextSetMove(field, field.getTurnsWithoutMill());
        try {
            boolean legalSetMove = GameRuleController.isLegalSetMove(field, move);
            if (legalSetMove) {
                field.setFieldStatus(move.getDest().x, move.getDest().y, currentPlayer.getColor());
                if (currentPlayer.getColor().equals(Token.WHITE)) {
                	System.out.println("Robots move:");
                    System.out.println(move.getDest().x + ", " + move.getDest().y + ", " + currentPlayer.getColor());
                } else {
                    System.out.println(move.getDest().x + ", " + move.getDest().y + ", " + currentPlayer.getColor());
                }
                currentPlayer.lowerNumberOfStonesLeftToPlace();
                currentPlayer.raiseNumberOfStonesOnBoard();
                if (GameRuleController.createdMill(field, move, currentPlayer.getColor())) {
                    stateBeforeTake = currentState;
                    currentState = GameState.TAKE;
                } else {
                    changePlayer();
                }
            } else {
                sendToGui("Not a legal move!");
            }
        } catch (GameException ex) {
            sendToGui("Not a legal move!");
        }
    }

    /**
     * should be called when it is time to move the stones.
     * <p>
     * The move of the current player is read, verified and executed.
     * If a mill is created the current state will be set to TAKE.
     * Otherwise the current player will be changed to the next player if the move
     * could be verified.
     * If the move could not be verified, the current state will not be changed,
     * not will the player be changed.
     * @throws GameException 
     */
    private void moveStones() throws GameException {
        Move move = currentPlayer.getNextMove(field, MoveRater.MAXTIME);
        
        if (currentPlayer instanceof ComputerPlayer) {
            showMoveInGUI(field, move);
        }
        
        try {
            if (GameRuleController.isLegalMove(field, move, currentPlayer)) {
                field.applyMove(move, currentPlayer.getColor());
                if (currentPlayer.getColor().equals(Token.WHITE)) {
                	System.out.println("Robots move:");
                    System.out.println(move.getSource().x + ", " + move.getSource().y + ", " + currentPlayer.getColor());
                    System.out.println(move.getDest().x + ", " + move.getDest().y + ", " + currentPlayer.getColor());
                } else {
                    System.out.println(move.getSource().x + ", " + move.getSource().y + ", " + currentPlayer.getColor());
                    System.out.println(move.getDest().x + ", " + move.getDest().y + ", " + currentPlayer.getColor());
                }
                if (GameRuleController.createdMill(field, move, currentPlayer.getColor())) {
                    stateBeforeTake = currentState;
                    currentState = GameState.TAKE;
                } else {
                    changePlayer();
                }
            } else {
                sendToGui("Not a legal move!");
            }
        } catch (GameException ex) {
            sendToGui("Not a legal move!");
        }
    }

    /**
     * should be called when it is time to take a stone.
     * <p>
     * The move of the current player is read, verified and executed.
     * The current player will be changed to the next player if the move
     * could be verified and the state before this will be reset.
     * 
     * If there are no stones of the enemy that are not in a mill,
     * only the state and the player will be changed, nothing else will happen.
     * @throws GameException 
     */
    private void takeStone() throws GameException {
        MyPlayerInterface enemy;
        if (currentPlayer.equals(playerOne)) {
            enemy = playerTwo;
        } else {
            enemy = playerOne;
        }
        
        if (GameRuleController.hasFreeStones(field, enemy)) {
            
            Move move = currentPlayer.getRemoveMove(field);
            if (GameRuleController.isLegalRemoveMove(field, move, currentPlayer.getColor())) {
                try {
                    
                    boolean couldRemoveStoneAtPosition = field.removeStoneAtPosition(enemy, move.getDest().x, move.getDest().y);
                    if (currentPlayer.getColor().equals(Token.WHITE)) {
                    	System.out.println("Robot takes piece:");
                        System.out.println(move.getDest().x + ", " + move.getDest().y + ", " + currentPlayer.getColor());
                    } else {
                        System.out.println(move.getDest().x + ", " + move.getDest().y + ", " + currentPlayer.getColor());
                    }
                    if (!couldRemoveStoneAtPosition) {
                        sendToGui("Not a legal move!");
                        return;
                    }                    
                    
                } catch (GameException ex) {
                    sendToGui("Not a legal move!");
                    return;
                }
            } else {
                sendToGui("Not a legal move!");
                return;
            }
            changePlayer();
            currentPlayer.lowerNumberOfStonesOnBoard();
            currentState = stateBeforeTake;
        } else {
            // TODO POSS_ADDITION depending on the rules that are used,
            // allow player to take stone out of enemy mill (not implemented)
            changePlayer();
            currentState = stateBeforeTake;
        }
    }

    /**
     * checks if it is time to move stones around and changes current state accordingly.
     */
    private boolean checkEndPlace() {
        if (playerOne.getNumberOfStonesLeftToPlace() == 0
                && playerTwo.getNumberOfStonesLeftToPlace() == 0) {
            currentState = GameState.MOVE;
            return true;
        }
        return false;
    }

    /**
     * checks if the game is over and changes current state accordingly.
     *
     * @return whether or not the game is over.
     */
    private boolean checkEndMove() {
        if (GameRuleController.isWin(field, currentPlayer)) {
            currentState = GameState.WIN;
            return true;
        } else if (GameRuleController.isDraw(field)) {
            currentState = GameState.DRAW;
            return true;
        }
        return false;
    }

    /**
     * changes the player.
     */
    private void changePlayer() {
        if (currentPlayer.equals(playerOne)) {
            currentPlayer = playerTwo;
        } else {
            currentPlayer = playerOne;
        }
    }

    /**
     * returns height of field.
     *
     * @return height of field
     */
    public int getHeight() {
        return field.getHeight();
    }

    /**
     * returns width of field.
     *
     * @return width of field
     */
    public int getWidth() {
        return field.getWidth();
    }
    
    @Override
    public void run() {
        currentState = GameState.PLACE;
        makePlayers();
        try {
			play();
		} catch (GameException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * checks if the given point exists in the mill game.
     *
     * @param x x
     * @param y y
     * @return whether or not the given point exists in the mill game
     */
    public boolean getBelongsToGame(int x, int y) {
        GameField gameField = (GameField) this.field;
        return gameField.getBelongsToGame(x, y);
    }
    
    /**
     * sends a message to the gui.
     * 
     * @param msg msg
     */
    private void sendToGui(String msg) {
        messageBuffer.write(msg);
    }
    
    /**
     * sends the field to the field buffer, so that the gui may read from
     * it and draw the field.
     */
    private void drawToGui() {
        fieldBuffer.write(field.getCopy());
    }
    
    private void showMoveInGUI(GameField field, Move move) {
        // TODO VIEW somehow represent ai moves in gui
    }
    
    /**
     * sets if player1 is computer.
     * 
     * @param isP1Com isP1Com 
     */
    public void setIsP1Com(boolean isP1Com) {
        this.isP1Com = isP1Com;
    }
    
    /**
     * sets if player2 is computer.
     * 
     * @param isP2Com isP2Com 
     */
    public void setIsP2Com(boolean isP2Com) {
        this.isP2Com = isP2Com;
    }
}
