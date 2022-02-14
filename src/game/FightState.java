package game;

import java.sql.Timestamp;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import settings.Settings;
import teams.Team;
import teams.TeamManager;
import util.WorldManager;

public class FightState extends GameState{
	
	static World generateWorld() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String worldName = "InGameWorld."+ts.getYear()+"."+ts.getMonth()+"."+ts.getDay()+"."+ts.getHours()+"."+ts.getMinutes();
		World world = WorldManager.createEmptyWorld(worldName);
		world.getBlockAt(0, 10, 0).setType(Material.COBWEB);
		world.setGameRuleValue("randomTickSpeed", "1");
		world.setGameRuleValue("fallDamage", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.setGameRuleValue("doWeatherCycle", "false");
		world.setGameRuleValue("keepInventory", "true");
		world.getWorldBorder().setSize(100.0);
		return world;
	}
	
	
	World gameworld;
	
	@Override
	public void start() {
		//TODO: Set team spawn locations, load team bases...
		
		for(Player cp : Bukkit.getOnlinePlayers()) setupPlayer(cp);
		
	}
	

	@Override
	public void stop() {
		
		for(Player cp : Bukkit.getOnlinePlayers()) {
			cp.setAllowFlight(false);
		}
	}

	@Override
	public void handleJoin(Player joined) {
		setupPlayer(joined);
		
	}
	public void setupPlayer(Player toSetup) {
		if(TeamManager.getPlayerTeam(toSetup)==null) { 			//no team --> Spectator
			toSetup.setGameMode(GameMode.SPECTATOR);
			toSetup.teleport(new Location(gameworld, 0, 100, 0));
		}else { 												//in team team --> Spectator
			Team playerTeam = TeamManager.getPlayerTeam(toSetup);
			
			
			toSetup.teleport(TeamManager.getPlayerTeam(toSetup).getRespawnPoint());
			toSetup.setGameMode(GameMode.SURVIVAL);
		}
	}

	@Override
	public void handleLeave(Player leaved) {
		
		
	}
	
	
	@EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {e.setRespawnLocation(TeamManager.getPlayerTeam(e.getPlayer()).getRespawnPoint());}

}

