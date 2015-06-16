package application.mill.Interfaces;
/**
 * Diese Exception wird geworfen, wenn der Spieler
 * ein Feld whlt, welches nicht im Spiel verwendet wird
 *
 */
public class GameException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5665583163113109734L;

	public GameException(String string) {
		super(string);
	}

}
