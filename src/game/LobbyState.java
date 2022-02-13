package game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import settings.Settings;

public class LobbyState extends GameState{

	@Override
	public void start() {
		for(Player cp : Bukkit.getOnlinePlayers()) handleJoin(cp);
		
	}

	@Override
	public void stop() {
		for(Player cp : Bukkit.getOnlinePlayers()) cp.getInventory().clear();
		
	}

	@Override
	public void handleJoin(Player joined) {
		if(!joined.isOp()) joined.teleport(Settings.spawn);
	}

	@Override
	public void handleLeave(Player leaved) {
		
	}

	//Forbidden actions
	@EventHandler public void onDrop(PlayerDropItemEvent e) {e.setCancelled(true);}
	
}
