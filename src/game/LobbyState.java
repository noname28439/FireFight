package game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import main.Main;
import settings.Settings;
import teams.Team;
import util.ItemBuilder;

public class LobbyState extends GameState{

	static int LobbySchedulerID, lobbycountdown;
	
	@Override
	public void start() {
		for(Player cp : Bukkit.getOnlinePlayers()) handleJoin(cp);
		
		LobbySchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() { @Override public void run() {
			boolean hasEveryTeamOnePlayer = true;
			for(Team ct : Team.values()) if(ct.getTeamPlayers().size()==0) hasEveryTeamOnePlayer = false;
			
			if(hasEveryTeamOnePlayer) 
				lobbycountdown--;
			else
				lobbycountdown = Settings.lobbySeconds;
			
			if(lobbycountdown<=0) {
				GameStateManager.switchGameState(Main.BUILD_STATE);
			}
			
			for(Player cp : Bukkit.getOnlinePlayers())
				cp.setLevel(lobbycountdown);
		}}, 0, 20);
		
	}

	@Override
	public void stop() {
		for(Player cp : Bukkit.getOnlinePlayers()) cp.getInventory().clear();
		Bukkit.getScheduler().cancelTask(LobbySchedulerID);
	}

	@Override
	public void handleJoin(Player joined) {
		if(!joined.isOp()) {
			joined.teleport(Settings.spawn);
			joined.getInventory().clear();
		}
		joined.getInventory().setItem(4, new ItemBuilder(Material.COMPASS, 1).setDisplayname(ChatColor.DARK_BLUE+"TeamSelector").build());
		
	}

	@Override
	public void handleLeave(Player leaved) {
		
	}

	//Forbidden actions
	@EventHandler public void onDrop(PlayerDropItemEvent e) {e.setCancelled(true);}
	@EventHandler public void onInvclick(InventoryClickEvent e) {if(!e.getWhoClicked().isOp()) e.setCancelled(true);}
	
	
	@EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Settings.spawn);
	}
}
