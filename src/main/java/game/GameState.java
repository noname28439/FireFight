package game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public abstract class GameState implements Listener{

	public abstract void handleJoin(Player joined);
	public abstract void handleLeave(Player leaved);
	
	public abstract void start();
	public abstract void stop();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		handleJoin(e.getPlayer());
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer());
	}
	
}

