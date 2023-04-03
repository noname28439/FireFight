package game;

import org.bukkit.event.HandlerList;

import main.Main;

public class GameStateManager {

	public static GameState currentGameState;
	
	public static void switchGameState(GameState newGS) {
		if(currentGameState!=null) {
			currentGameState.stop();
			HandlerList.unregisterAll(currentGameState);
		}
		currentGameState = newGS;
		Main.pm.registerEvents(currentGameState, Main.plugin);
		if(currentGameState!=null) currentGameState.start();
		System.out.println("Switched GameState to "+currentGameState.getClass().getName());
	}
}
