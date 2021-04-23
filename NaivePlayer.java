package put.ai.games.naiveplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;


public class NaivePlayer extends Player {
	public static void main(String[] args) {
		
	}


    public class Evaluator {
        public Double evaluate(Board board, Player.Color color){
            List<List<Integer>> coordinates = new put.ai.games.naiveplayer.NaivePlayer.Evaluator().getCoordinate(board, color);
            Double result = 0.0;
            for(int i = 0; i < coordinates.size()-1; i++){
                for(int j = i+1; j < coordinates.size(); j++){
                    Integer distance = calculate_distance(coordinates.get(i), coordinates.get(j));
                    if (distance < 2) result += 3;
                    else result -= distance;
                    
                }
            }
            Player.Color opponentColor;
            if(color.equals(Player.Color.PLAYER1)) {
            	opponentColor = Player.Color.PLAYER2;
            }
            else {
            	opponentColor = Player.Color.PLAYER1;
            }
             
            
            Integer opponentMoves = board.getMovesFor(opponentColor).size();
            Integer myMoves = board.getMovesFor(color).size();
            Integer numberOfOpponentPawns = getCoordinate(board, opponentColor).size();
            Integer numberOfMyPawns = getCoordinate(board, opponentColor).size();
            
            Integer diffMoves = myMoves - opponentMoves;
            Integer diffPawns = numberOfOpponentPawns - numberOfMyPawns;
            result += diffMoves*3;
            result += diffPawns*2;
            return result;
        }



        public List<List<Integer>> getCoordinate(Board board, Player.Color color){
        	int size = board.getSize();
        	List<List<Integer>> coordinates = new ArrayList<List<Integer>>();
            for(int row = 0; row < size; row++){
                for(int col = 0; col < size; col++){
                    if(board.getState(col, row).equals(color)) {
                    	List<Integer> coordinate = new ArrayList<Integer>();
                    	coordinate.add(col);
                    	coordinate.add(row);
                    	coordinates.add(coordinate);
                    }
                }
            }
            return coordinates;
        }
        
        private Integer calculate_distance(List<Integer> a, List<Integer> b){
            return (int) Math.round(Math.sqrt(Math.pow((a.get(0) - b.get(0)),2) + Math.pow((a.get(1) - b.get(1)),2)));
        }
    }
    

    public class AlphaBeta {
        public Move getBestMove(Board board, Player.Color color){
            List<Move> moves = board.getMovesFor(color);
            double max = Double.NEGATIVE_INFINITY;
            Move bestMove = null;
            Boolean isMaxPlayer = true;
            Integer maxDepth = 3;
            for(Move move : moves){
                Board newBoard = board.clone();
                newBoard.doMove(move);
                Double score = miniMax(newBoard, isMaxPlayer, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,  color);
                if(score > max){
                    bestMove = move;
                    max = score;
                }
            }
            return bestMove;
        }

        private Double miniMax(Board board, Boolean isMaxPlayer,Integer depth, Double alpha, Double beta,  Player.Color color){
            if(depth == 0)
                return new put.ai.games.naiveplayer.NaivePlayer.Evaluator().evaluate(board, getColor());
            
            
            List<Move> moves = board.getMovesFor(color);

            if (color.equals(Player.Color.PLAYER1))
            	color = Player.Color.PLAYER2;
            else
            	color = Player.Color.PLAYER1;

            if(isMaxPlayer){
 
            	double maxResult = Double.NEGATIVE_INFINITY;
                for (Move move : moves){
                    Board nextBoard = board.clone();
                    nextBoard.doMove(move);
                    Double result = miniMax(nextBoard, false, depth - 1, alpha, beta,  color);
                    maxResult = Math.max(result, maxResult);
                    alpha = Math.max(result, alpha);
                    if(alpha >= beta)
                        break;
                }
                return maxResult;
            }
            else{
                Double minResult = Double.POSITIVE_INFINITY;
                for (Move move : moves){
                    Board nextBoard = board.clone();
                    nextBoard.doMove(move);
                    Double result = miniMax(nextBoard, true, depth - 1, alpha, beta,  color);
                    minResult = Math.min(result, minResult);
                    beta = Math.min(beta, result);
                    if(alpha >= beta)
                        break;
                }
                return minResult;
            }
        }
    }


    @Override
    public String getName() {
        return "Martyna Mirkiewicz 141285 Michal Olszewski 141292";
    }


    @Override
    public Move nextMove(Board b) {
        return new put.ai.games.naiveplayer.NaivePlayer.AlphaBeta().getBestMove(b, getColor());
    }
}