package game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import settings.Settings;

public class LobbyState extends GameState{


	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	//Forbidden actions
	@EventHandler public void onDrop(PlayerDropItemEvent e) {e.setCancelled(true);}

	@Override
	public void handleJoin(PlayerJoinEvent e, Player joined) {
		joined.teleport(Settings.spawn);
	}

	@Override
	public void handleLeave(PlayerQuitEvent e, Player leaved) {
		
	}

}
