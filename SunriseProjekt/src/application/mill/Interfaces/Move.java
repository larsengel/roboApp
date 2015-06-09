package application.mill.Interfaces;

import java.awt.Point;

/** Dies soll die Repraesentation eines Zuges sein.
 *  Wenn man an der Reihe ist, erwartet der Controller
 *  ein solches Objekt als Zug.
 */
public abstract class Move {

    /** Liefert das Ausgangsfeld des Zuges als Point */
    public abstract Point getSource();
    
    /** Liefert das Zielfeld des Zuges als Point */
    public abstract Point getDest();

    /** Ausgabefunktion fuer einen Zug  */
    public void print(){
        System.out.printf("[%d;%d]",getSource().x, getSource().y);
        System.out.print(" ==> ");
        System.out.printf("[%d;%d]   \n",getDest().x, getDest().y);
    }
}
