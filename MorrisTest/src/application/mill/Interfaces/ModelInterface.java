package application.mill.Interfaces;

import application.mill.Interfaces.GameException;

/**
 * Jedes Spielbrett von Muehle soll dieses Interface erfuellen.
 */
public interface ModelInterface {

	/**
	 * Liefert den Status des gewaehlten Feldes,
	 * wenn diese Feld nicht zum Muehle Spiel gehoert,
	 * wir eine GameException geworfen
	 *
	 * @param spalte - gewuenschte Spalte
	 * @param zeile - gewuenschte Zeile
	 * @return BLACK, WHITE oder EMPTY
	 * @throws GameException
	 * @throws application.mill.Controller.GameException 
	 */
	public Token getFieldStatus(int x, int y) throws GameException, application.mill.Interfaces.GameException;

	/**
	 * Setzt das gegebene Token in das gewuenschte Feld,
	 * wenn diese Feld nicht zum Muehle Spiel gehoert,
	 * wir eine GameException geworfen
	 *
	 * @param x - gewuenschte Spalte
	 * @param y - gewuenschte Zeile
	 * @param tok - gewuenschter Status
	 * @throws GameException
	 * @throws application.mill.Controller.GameException 
	 */
	public boolean setFieldStatus(int x, int y, Token tok) throws GameException, application.mill.Interfaces.GameException;
	
	/**
	 * Entfernt das gegebene Token aus dem gewuenschten Feld,
	 * wenn diese Feld nicht zum Muehle Spiel gehoert,
	 * wir eine GameException geworfen
	 * 
	 * @param player der Spieler,
	 * der einen Stein auf diesem Feld haben soll
	 * 
	 * @param x - gewuenschte Spalte
	 * @param y - gewuenschte Zeile
	 * @return true == Stein entnommen
	 * 		   false == Stein nicht entnommen,
	 *  				da Feld leer oder vom anderen Spieler
	 * @throws GameException
	 * @throws application.mill.Controller.GameException 
	 */
	public boolean removeStoneAtPosition(Playerinterface player, int x, int y) throws GameException, application.mill.Interfaces.GameException;
	
	/** 
	 * Liefert die Breite des Spielfeldes (Anzahl der Felder)
	 */
	public int getWidth();

	/** 
	 * Liefert die Hoehe des Spielfeldes (Anzahl der Felder)  
	 */
	public int getHeight();

}
