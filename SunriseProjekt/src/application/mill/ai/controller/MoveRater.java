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
package application.mill.ai.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import application.mill.Controller.GameRuleController;
import application.mill.Interfaces.GameException;
import application.mill.Interfaces.Move;
import application.mill.Interfaces.Token;
import application.mill.Model.GameField;
import application.mill.Model.MyMove;
import application.mill.Model.OptionObject;
import application.mill.ai.modell.KillerMoves;
import application.mill.ai.modell.Node;
import application.mill.ai.modell.PossibleMove;
import application.mill.ai.modell.Rating;
import application.mill.ai.modell.SimplePlayer;

/**
 * This class holds methods to determine good take as well as move moves.
 * <p>
 * A maximal search depth may be chosen or a maximal time to calculate these
 * moves.
 * <p>
 * <p>
 * A word on inner functionality:
 * <p>
 * The rating is always as seen from the ai point of view.
 * A positive rating is something good, a negative something bad, no 
 * matter which player actually made the rated move.
 * <p>
 * 
 * 
 * @author 
 */
public class MoveRater extends AbstractMoveRater {

    public static final boolean iterativeDeepening = true; // allowes for use of MAXTIME. produces overhead (~20%?)!
    public static int MAXTIME = 2000; // max time for evaluation (if using iterativeDeepening)
    public static int MAXDEPTH = 6; // depth used for algorithm
    public static final int MAXINT = Integer.MAX_VALUE / 2;
    public static final int MININT = Integer.MIN_VALUE / 2;
    private double time = 0; // for debug. messures time it takes to calc one move
    private SimplePlayer player;
    private SimplePlayer enemy;
    private KillerMoves[] cutoffHistory; // stores last n moves that produced a cutoff for each depth level

    // TODO ich denke, dass SimplePlayer hier nicht wirklich benoetigt wird;
    // muesste ja irgendwie immer ne kopie von erstellt werden usw, ka... 
    // evtl lieber im node numberOfStonesOfX
    /**
     * constructs new move rater.
     * 
     * @param player player
     * @param enemy enemy 
     */
    public MoveRater(SimplePlayer player, SimplePlayer enemy) {
        this.player = player;
        this.enemy = enemy;

    }

    private final void initHistory(int depth) {
        cutoffHistory = new KillerMoves[depth + 1];
        for (int i = 0; i < cutoffHistory.length; i++) {
            cutoffHistory[i] = new KillerMoves();
        }
    }

    /**
     * returns the best take move for the ai this round.
     * 
     * @param model model
     * @return best take move for the ai this round 
     * @throws application.mill.Controller.GameException 
     */
    public Move getNextTakeMove(GameField model, int maxtime) throws application.mill.Interfaces.GameException {
        this.MAXTIME = maxtime;
        initHistory(MAXDEPTH);
        time = System.currentTimeMillis();
        // generate moves ai may make this round
        List<PossibleMove> possibleTakeMoves = generateTakeMoves(model,
                player.getColor());
        // rate all of those moves with alpha beta alg
        for (PossibleMove possibleMove : possibleTakeMoves) {
            Node node = new Node(model.getCopy(), possibleMove,
                    true, player.getCopy(), enemy.getCopy(), player.getColor());
            node.applyMove();
            int alphaBeta = alphaBeta(node, MAXDEPTH, MININT, MAXINT);
            possibleMove.setRating(alphaBeta);
        }

        // get best moves
        Move bestMove = super.getBestMove(possibleTakeMoves);

        if (OptionObject.DEBUG) {
            System.out.println("time calc move: "
                    + (System.currentTimeMillis() - time)
                    + " reached depth: " + MAXDEPTH + '\n');
        }

        return bestMove;
    }

    /**
     * returns the best next move as determined by this method.
     *
     * @param model field
     * @return next move
     * @throws application.mill.Controller.GameException 
     */
    public Move getNextMove(GameField model, int maxtime) throws application.mill.Interfaces.GameException {
        this.MAXTIME = maxtime;
        time = System.currentTimeMillis();
        int reachedDepth = 0;

        // moves the ai may actually make this round
        List<PossibleMove> moves = generatePossibleMoves(model, player);

        if (iterativeDeepening) {
            for (int depth = 1;; depth++) {
                // rate copy of moves.
                List<PossibleMove> copymoves = new ArrayList<PossibleMove>(moves);
                Collections.copy(copymoves, moves);
                initHistory(depth);
                rateMoves(copymoves, model, depth);
                if ((System.currentTimeMillis() - time) > MAXTIME) {
                    reachedDepth = depth - 1;
                    break;
                }
                // after all copies of the moves are rated, copy back
                Collections.copy(moves, copymoves);
            }
        } else {
            initHistory(MAXDEPTH);
            rateMoves(moves, model, MAXDEPTH);
            reachedDepth = MAXDEPTH;
        }
        Move bestMove = super.getBestMove(moves);

        if (OptionObject.DEBUG) {
            System.out.println("time calc move: "
                    + (System.currentTimeMillis() - time)
                    + " reached depth: " + reachedDepth + '\n');
        }

        return bestMove;
    }

    /**
     * rates the moves (NOT take moves) in the given moves list.
     *
     * @param moves moves
     * @param model model
     * @param depth depth
     * @throws application.mill.Controller.GameException 
     */
    private void rateMoves(List<PossibleMove> moves, GameField model, int depth) throws application.mill.Interfaces.GameException {
        if (moves.size() == 1) {
            // nothing to rate. one possible move, take it.
            return;
        }

        for (PossibleMove possibleMove : moves) {
            Node node = new Node(model.getCopy(), possibleMove, false, player.getCopy(), enemy.getCopy(), player.getColor());
            // apply move to copy of field
            node.applyMove();
            node.rateSingleMove();
            int alphaBeta = 0;
            alphaBeta = alphaBeta(node, depth, MININT, MAXINT);
            possibleMove.setRating(alphaBeta);
        }
    }

    /**
     * rates the move in the given node.
     *
     * @param node node which holds the move the given player may actually
     * perform this round as well as the field that move created.
     * @param depth desired depth of tree
     * @param alpha starting alpha value (in general, a minimal value should be passed)
     * @param beta starting beta value (in general, a maximal value should be passed)
     * @param currentPlayer current player
     * @return calculated value of the move in the given node
     * @throws application.mill.Controller.GameException 
     */
    protected int alphaBeta(Node node, int depth, int alpha, int beta) throws application.mill.Interfaces.GameException {

        SimplePlayer currentPlayer = node.getNextExecutingPlayer();

        // perform alpha/beta alg:
        if (GameRuleController.isWin(node.getField(), currentPlayer)) { // terminal node
            if (currentPlayer.getColor() == player.getColor()) {
                //System.out.println("PLAYER WIN");
                return Rating.WIN;

            } else {
                //System.out.println("ENEMY WIN");
                return -Rating.WIN;
            }
        } else if (GameRuleController.isDraw(node.getField())) {
            return estimateDrawValue(node);
        }
        if (depth <= 0) { // max depth. return heuristic value
            // System.out.println("returning: " + node.getMove().getRating());
            return node.getMove().getRating(); // the rating in the move is always as seen for player, not current player
        }


        List<Node> children = generateChildren(node, depth - 1);

//        if (children.isEmpty()) {
//            System.out.println("no children!");
//        }
//        for (Node node1 : children) {
//            System.out.println("depth: " + depth + " node: " + node1.toString());
//        }

        if (currentPlayer.getColor() == player.getColor()) {
            for (Node child : children) {
                alpha = Math.max(alpha, alphaBeta(child, depth - 1, alpha, beta));

                if (beta <= alpha) {
                    cutoffHistory[depth].addMove(child.getMove());
                    break; // cutoff
                }
                if (iterativeDeepening) {
                    if ((System.currentTimeMillis() - time) > MAXTIME) {
                        break;
                    }
                }
            }
            return alpha;
        } else {
            for (Node child : children) {
                beta = Math.min(beta, alphaBeta(child, depth - 1, alpha, beta));
                //System.out.println("created mill. setting beta : " + beta);
                if (beta <= alpha) {
                    cutoffHistory[depth].addMove(child.getMove());
                    break; // cutoff
                }
                if (iterativeDeepening) {
                    if ((System.currentTimeMillis() - time) > MAXTIME) {
                        break;
                    }
                }
            }
            return beta;
        }
    }

    private int estimateDrawValue(Node node) {
//        SimplePlayer executingPlayer = node.getExecutingPlayer();
//        SimplePlayer otherPlayer = this.getEnemy(executingPlayer);
//
//        SimplePlayer ai;
//        SimplePlayer enemy;
//        if (executingPlayer.getColor() == player.getColor()) {
//            ai = executingPlayer;
//            enemy = otherPlayer;
//        } else {
//            enemy = executingPlayer;
//            ai = otherPlayer;
//        }
//
//        int diff = enemy.getNumberOfStonesOnBoard() - ai.getNumberOfStonesOnBoard();
//        return diff * Rating.MILL;
        
        return 0;



//        if (ai.getNumberOfStonesOnBoard() > enemy.getNumberOfStonesOnBoard()) {
//            return -(Rating.WIN / 2); // draw even though ai has more stones is half as bad as losing
//        } else {
//            return (Rating.WIN / 2); // draw when ai has less (or equal) stones is half as good as winning
//        }
    }

    /**
     * generates a list of child-nodes for the given node and player.
     * <p>
     * For all possible moves for the given player on the field in the given
     * node a node will be created.
     * The move will be executed on a copy of the field, which will be stored in the move.
     * The node will be rated based on the heuristics given in the
     * <code>rateSingleMove</code> method and the value of the passed node will
     * be subtracted.
     * The children will also be sorted to achieve an early cutoff.
     *
     * @param node node
     * @param currentPlayer currentPlayer
     * @param currentDepth currentDepth 
     * @return list of child-nodes for the given node and player
     * @throws application.mill.Controller.GameException 
     */
    private List<Node> generateChildren(Node node, int depthOfChildren) throws application.mill.Interfaces.GameException {

        List<PossibleMove> moves;
        boolean isTakeMove;

        if (node.isCreatedMill()) {
            moves = generateTakeMoves(node.getField(), node.getNextExecutingPlayer().getColor());
            isTakeMove = true;
        } else {
            moves = this.generatePossibleMoves(node.getField(), node.getNextExecutingPlayer());
            isTakeMove = false;
        }

        List<Node> children = new ArrayList<Node>();
        for (PossibleMove possibleMove : moves) {
            // apply move to field:
            possibleMove.addToRating(node.getMove().getRating());
            Node child = new Node(node.getField().getCopy(), possibleMove, isTakeMove, node.getNextExecutingPlayer().getCopy(), getEnemy(node.getNextExecutingPlayer()).getCopy(), player.getColor());
            child.applyMove();
            child.rateSingleMove();
            children.add(child);
        }


        if (node.isCreatedMill()) {

            // sort nodes by heuristic value (for early cutoffs)
            Collections.sort(children, new HeuristicComparator());

            // move killer moves to front (for early cutoffs)
            List<Node> killerMoves = new ArrayList<Node>();
            for (int i = 0; i < children.size(); i++) {
                Node child = children.get(i);
                List<MyMove> oldkillerMoves = cutoffHistory[depthOfChildren].getKillerMoves();
                for (MyMove killerMove : oldkillerMoves) {
                    if (child.getMove().equals(killerMove)) {
                        killerMoves.add(child);
                    }
                }
            }

            for (Node killerMove : killerMoves) {
                children.remove(killerMove);
                children.add(0, killerMove);

            }
        }

        return children;

    }

    /**
     * creates all possible take moves for the given field.
     *
     * @param field field
     * @param player player to take stone
     * @return all possible take moves for the given field
     * @throws application.mill.Controller.GameException 
     */
    private List<PossibleMove> generateTakeMoves(GameField field, Token player) throws application.mill.Interfaces.GameException {
        Token enemy = AbstractMoveRater.getEnemy(player);

        List<PossibleMove> moves = new ArrayList<PossibleMove>();
        for (int i = 0; i < field.getWidth(); i++) {
            for (int j = 0; j < field.getHeight(); j++) {
                try {
                    Token fieldStatus = field.getFieldStatus(i, j);
                    if (fieldStatus == enemy) {
                        PossibleMove possibleMove = new PossibleMove();
                        possibleMove.setSource(-1, -1);
                        possibleMove.setDest(i, j);
                        if (!GameRuleController.createdMill(field, possibleMove, enemy)) {
                            moves.add(possibleMove);
                        }
                    }
                } catch (GameException ex) {
                }
            }
        }
        return moves;
    }

    /**
     * creates all possible moves for the given field.
     * <p>
     * If the given player has only three stones left, not
     * all moves will be generated.
     * In this case, only those moves that create a mill for the
     * player or block one of the enemy will be considered.
     * If none of the possible moves can do this, a random move will be chosen
     * and returned.
     *
     *
     * @param field field
     * @param player player
     * @return all possible moves for the given player on the given field
     * @throws application.mill.Controller.GameException 
     */
    private List<PossibleMove> generatePossibleMoves(GameField field, SimplePlayer player) throws application.mill.Interfaces.GameException {
        // TODO number of stones is not reduced in MoveRater!
        if (player.getNumberOfStonesOnBoard() > 3) {
            List<PossibleMove> moves = new ArrayList<PossibleMove>();
            int width = field.getWidth();
            int height = field.getHeight();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (field.getBelongsToGame(i, j)) { // to avoid instanciation of exception on miss
                        try {
                            Token fieldStatus = field.getFieldStatus(i, j);
                            if (fieldStatus == player.getColor()) {
                                // check all neighbors for empty. for every empty neighbor, create and add new move
                                List<Point> allNeighbors = field.getAllNeighbors(i, j);
                                for (Point point : allNeighbors) {
                                    if (field.getFieldStatus(point.x, point.y) == Token.EMPTY) {
                                        PossibleMove possibleMove = new PossibleMove();
                                        possibleMove.setSource(i, j);
                                        possibleMove.setDest(point.x, point.y);
                                        moves.add(possibleMove);
                                    }
                                }
                            }
                        } catch (GameException ex) {
                        }
                    }
                }
            }
            return moves;
        } else {
            return generateGoodJumpingMoves(field, player);
        }
    }

    /**
     * returns a list of all jumping moves that produce or block a mill.
     * <p>
     * If no such move(s) exist, a list with one random move in it will
     * be returned.
     * 
     * @param field field
     * @param player player
     * @return list of all jumping moves that produce or block a mill
     * @throws application.mill.Controller.GameException 
     */
    private List<PossibleMove> generateGoodJumpingMoves(GameField field, SimplePlayer player) throws application.mill.Interfaces.GameException {
        PossibleMove losingMove = null;
        List<PossibleMove> moves = new ArrayList<PossibleMove>();
        int width = field.getWidth();
        int height = field.getHeight();
        // find player's stones
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field.getBelongsToGame(i, j)) { // to avoid instanciation of excetion on miss
                    try {
                        Token fieldStatus = field.getFieldStatus(i, j);
                        if (fieldStatus == player.getColor()) {
                            // check all fields for empty. for every empty field, create and add new move
                            for (int i2 = 0; i2 < width; i2++) {
                                for (int j2 = 0; j2 < height; j2++) {
                                    if (field.getBelongsToGame(i2, j2)) { // to avoid instanciation of excetion on miss
                                        Token fieldStatusTo = field.getFieldStatus(i2, j2);
                                        if (fieldStatusTo == Token.EMPTY) {
                                            PossibleMove possibleMove = new PossibleMove();
                                            possibleMove.setSource(i, j);
                                            possibleMove.setDest(i2, j2);
                                            // we really only want the move if it can create or block a mill:
                                            GameField copy = field.getCopy();
                                            copy.applyMove(possibleMove, player.getColor());
                                            if (GameRuleController.createdMill(field, possibleMove, player.getColor())) {
                                                moves.add(possibleMove);
                                            } else if (GameRuleController.createdMill(field, possibleMove, getEnemy(player.getColor()))
                                                    && (neighborOfXisX(field, possibleMove, super.getEnemy(player.getColor()))
                                                    || enemy.getNumberOfStonesOnBoard() <= 3)) {
                                                moves.add(possibleMove);
                                            } else if (losingMove == null) {
                                                losingMove = possibleMove;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } catch (GameException ex) {
                    }
                }
            }
        }
        if (moves.isEmpty()) {
            // no moves that block or create a mill, return a random move
            moves.add(losingMove);
        }
        return moves;
    }

    private List<PossibleMove> generateGoodJumpingMovesOLD(GameField field, SimplePlayer player) throws application.mill.Interfaces.GameException {
        PossibleMove losingMove = null;
        List<PossibleMove> moves = new ArrayList<PossibleMove>();
        int width = field.getWidth();
        int height = field.getHeight();
        // find player's stones
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field.getBelongsToGame(i, j)) { // to avoid instanciation of excetion on miss
                    try {
                        Token fieldStatus = field.getFieldStatus(i, j);
                        if (fieldStatus == player.getColor()) {
                            // check all fields for empty. for every empty field, create and add new move
                            for (int i2 = 0; i2 < width; i2++) {
                                for (int j2 = 0; j2 < height; j2++) {
                                    if (field.getBelongsToGame(i2, j2)) { // to avoid instanciation of excetion on miss
                                        Token fieldStatusTo = field.getFieldStatus(i2, j2);
                                        if (fieldStatusTo == Token.EMPTY) {
                                            PossibleMove possibleMove = new PossibleMove();
                                            possibleMove.setSource(i, j);
                                            possibleMove.setDest(i2, j2);
                                            // we really only want the move if it can create or block a mill:
                                            GameField copy = field.getCopy();
                                            copy.applyMove(possibleMove, player.getColor());
                                            if (GameRuleController.createdMill(field, possibleMove, player.getColor())) {
                                                moves.add(possibleMove);
                                            } else if (GameRuleController.createdMill(field, possibleMove, getEnemy(player.getColor()))
                                                    && (neighborOfXisX(field, possibleMove, super.getEnemy(player.getColor()))
                                                    || enemy.getNumberOfStonesOnBoard() <= 3)) {
                                                moves.add(possibleMove);
                                            } else if (losingMove == null) {
                                                losingMove = possibleMove;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } catch (GameException ex) {
                    }
                }
            }
        }
        moves.add(losingMove);
        return moves;
    }

    // checks if there is a neighbor at the dest of the move with the given token.
    // TODO FIXME it should not count those that would be part of a mill.
    // (this method is meant to specify canBlockEnemyMill. it should return true, if the enemy could create a mill at the dest.)
    // as it is, it is completely useless (from where it is called, it will always return true)
    private boolean neighborOfXisX(GameField field, PossibleMove possibleMove, Token token) throws application.mill.Interfaces.GameException {
        List<Point> allNeighbors = field.getAllNeighbors(possibleMove.getDest().x, possibleMove.getDest().y);
        for (Point point : allNeighbors) {
            try {
                if (field.getFieldStatus(point.x, point.y) == token) {
                    return true;
                }
            } catch (GameException ex) {
            }

        }
        return false;
    }

    /**
     * returns the player that is not the given player.
     *
     * @param currentPlayer
     * @return opposite player
     */
    private SimplePlayer getEnemy(SimplePlayer currentPlayer) {
//        if (currentPlayer.equals(this.player)) {
//            return enemy;
//        } else if (currentPlayer.equals(this.enemy)) {
//            return player;
//        } else {
//            return null; // unreachable
//        }
        if (currentPlayer.getColor() == player.getColor()) {
            return enemy;
        } else if (currentPlayer.getColor() == enemy.getColor()) {
            return player;
        } else {
            return null; // unreachable
        }
    }

    /**
     * Comparator to sort nodes according to the value of the move they are
     * holding (high values at beginning of list).
     */
    private class HeuristicComparator implements Comparator<Node> {

        @Override
        public int compare(Node t, Node t1) {
            return t1.getMove().getRating() - t.getMove().getRating();
        }
    }
}
