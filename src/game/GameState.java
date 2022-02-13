package game;

import org.bukkit.entity.Player;

public abstract class GameState {

	public abstract void handleJoin(Player joined);
	public abstract void handleLeave(Player leaved);
	
	public abstract void start();
	public abstract void stop();
	
	
	
}
