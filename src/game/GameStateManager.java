package game;

public class GameStateManager {

	public static GameState currentGameState;
	
	public static void switchGameState(GameState newGS) {
		if(currentGameState!=null) currentGameState.stop();
		currentGameState = newGS;
		if(currentGameState!=null) currentGameState.start();
	}
	
	
	
}
