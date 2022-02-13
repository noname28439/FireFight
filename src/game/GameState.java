package game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class GameState implements Listener{

	public abstract void handleJoin(Player joined);
	public abstract void handleLeave(Player leaved);
	
	public abstract void start();
	public abstract void stop();
		
}

