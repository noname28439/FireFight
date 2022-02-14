package game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import settings.Settings;
import teams.Team;
import teams.TeamManager;
import util.ItemBuilder;

public class LobbyState extends GameState{

	static int LobbySchedulerID, lobbycountdown;
	
	@Override
	public void start() {
		for(Player cp : Bukkit.getOnlinePlayers()) handleJoin(cp);
		
		LobbySchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() { @Override public void run() {
			boolean hasEveryTeamOnePlayer = true;
			for(Team ct : Team.values()) if(ct.teamPlayers.size()==0) hasEveryTeamOnePlayer = false;
			
			if(hasEveryTeamOnePlayer)
				lobbycountdown--;
			else
				lobbycountdown = Settings.lobbySeconds;
			
			if(lobbycountdown<=0) {
				GameStateManager.switchGameState(Main.FIGHT_STATE);
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
			joined.setGameMode(GameMode.ADVENTURE);
		}
		joined.getInventory().setItem(4, new ItemBuilder(Material.COMPASS, 1).setDisplayname(ChatColor.DARK_BLUE+"TeamSelector").build());
		joined.getInventory().setItem(2, new ItemBuilder(Material.BRICKS, 1).setDisplayname(ChatColor.GOLD+"BaseManager").build());
		TeamManager.setPlayerTeam(joined.getName(), null);
		for (PotionEffect effect : joined.getActivePotionEffects())
			joined.removePotionEffect(effect.getType());
		
	}

	@Override
	public void handleLeave(Player leaved) {
		TeamManager.setPlayerTeam(leaved.getName(), null);
	}

	//Forbidden actions
	@EventHandler public void onDrop(PlayerDropItemEvent e) {e.setCancelled(true);}
	@EventHandler public void onInvclick(InventoryClickEvent e) {if(!e.getWhoClicked().isOp()) e.setCancelled(true);}
	
	
	@EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Settings.spawn);
	}
	
	
}
