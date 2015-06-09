package application.mill.Interfaces;

import application.mill.Interfaces.GameException;



/**
 * Jeder Spieler soll dieses Interface erfuellen.
 */
public interface Playerinterface {

	/**
	 * Hiermit soll ein Spieler benachrichtigt werden, 
	 * dass dieser am Zug ist.
	 * Und nach den Regeln von Phase 1. Spielen soll
	 * 
	 * @param model - Der aktuelle Spielfeldzustand
	 * @return - Den gewaehlten/berechneten Move
	 * @throws GameException 
	 */
	public Move getNextSetMove(ModelInterface model, int turnCounter) throws GameException;
	
	/**
	 * Hiermit soll ein Spieler benachrichtigt werden, 
	 * dass dieser am Zug ist.
	 * Und nach den Regeln von Phase 2. Spielen soll
	 * 
	 * @param model - Der aktuelle Spielfeldzustand
	 * @return - Den gewaehlten/berechneten Move
	 * @throws GameException 
	 */
	public Move getNextMove(ModelInterface model, int turnCounter) throws GameException;
	
	/**
	 * Hiermit soll ein Spieler benachrichtigt werden, 
	 * dass dieser am Zug ist.
	 * Und dem Gegner einen Stein wegnehmen darf
	 * 
	 * @param model - Der aktuelle Spielfeldzustand
	 * @return - Den gewaehlten/berechneten Move
	 * @throws GameException 
	 */
	public Move getRemoveMove(ModelInterface model) throws GameException;
	
	/**
	 * Weist dem Spieler eine Farbe zu.
	 * 
	 * @param spielerfarbe Token.RED oder Token.BLUE
	 */
	public void setColor(Token spielerfarbe);

	/**
	 * Gibt die Farbe des Spieler zurueck.
	 * 
	 * @return spielerfarbe des Spielers
	 *	       Token.RED oder Token.BLUE
	 */
	public Token getColor();
}
