import application.mill.Controller.GameController.GameState;
import application.mill.Controller.MainController;
import application.mill.Interfaces.Token;
import application.mill.Model.Buffer;
import application.mill.Model.GameField;
import application.mill.Model.MyPlayerInterface;


public class Main {

	public static void main(String[] args) throws InterruptedException {

		//We declare a new Buffer
		Buffer<Integer[]> inputBuffer = new Buffer<Integer[]>();
		
		MainController gc = new MainController(inputBuffer);
		
		// Starting the game
		gc.startGame();
		gc.processInput(new Integer[] { 0, 0 });
		
		MyPlayerInterface currentPlayer = gc.getCurrentPlayerMain();
		
		System.out.println(currentPlayer);
		

		// We get the current field
		//field = gc.getGameField();
		//Token token = field.getToken(0, 0); // We get the Token on (0, 0)
		//System.out.println(token);
		// We write in the Buffer to give the human moves to the Artificial
		// Intelligence
		//inputBuffer.write(new Integer[] { 0, 0 }); // The human player played on (0, 0)
		//Token new_token = field.getToken(0, 0);
		
		//System.out.println("Old Token: " + token + ", New Token: " + new_token);
		
		// Get the current player
		//MyPlayerInterface currentPlayer = gc.getCurrentPlayerMain();
		// Get the color of the curent player
		//Token color = currentPlayer.getColor(); // color = WHITE or BLACK
		//System.out.println("Current Player: " + currentPlayer + ", Color current Player: " + color);
		

	}

}
