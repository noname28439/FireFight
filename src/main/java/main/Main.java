package main;

import java.util.ArrayList;

import commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import bases.BaseManager;
import bases.BuildProcess;
import game.FightState;
import game.GameState;
import game.GameStateManager;
import game.LobbyState;
import game.MainListener;
import teams.TeamManager;
import util.DBHandler;

public class Main extends JavaPlugin{
	
	public static JavaPlugin plugin;
	public static PluginManager pm;
	
	public static GameState 
						LOBBY_STATE = new LobbyState(),
						FIGHT_STATE = new FightState();

	public static final String[] mgtBases = {};
	
	public static int roundtime = 0;
	public static int[] times = new int[] {1, 2, 3, 5, 10, 15, 20, 30, 60};
	
	public static String locationToString(Location input) {
		return "["+input.getX()+"|"+input.getY()+"|"+input.getZ()+"]";
	}
	
	public static boolean isHunter(String name) {
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
		Objective obj = board.getObjective("Sucher");
		for(OfflinePlayer cp : Bukkit.getOfflinePlayers()) {
			if(cp.getName().equalsIgnoreCase(name)) {
				if(obj.getScore(cp).getScore()==1)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	public static boolean playerBuilding(Player p) {
		for(BuildProcess cbp : BaseManager.buildProcesses) {
			if(cbp.player.equals(p.getName()))
				return true;
		}
		return false;
	}
	
	public static BuildProcess getPlayerBuild(Player p) {
		for(BuildProcess cbp : BaseManager.buildProcesses)
			if(cbp.player.equals(p.getName()))
				return cbp;
		return null;
	}
	
	public static ArrayList<Player> getAllPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		for(Player cp : Bukkit.getOnlinePlayers()) if(!playerBuilding(cp)) players.add(cp);
		return players;
	}
	
	@Override
	public void onEnable() {

		plugin = this;

		DBHandler.load();

		getCommand("test").setExecutor(new TestCMD());
		getCommand("spectate").setExecutor(new SpectateCMD());
		getCommand("build").setExecutor(new BuildCMD());
		getCommand("spawn").setExecutor(new SpawnCMD());
		getCommand("join").setExecutor(new JoinCMD());
		getCommand("overwrite").setExecutor(new OverwriteCMD());
		getCommand("setgametime").setExecutor(new SetGameTimeCMD());
		getCommand("edit").setExecutor(new EditCMD());
		getCommand("view").setExecutor(new ViewCMD());
		getCommand("setplayerteam").setExecutor(new SetPlayerTeamCmd());
		
		
		pm = Bukkit.getPluginManager();
		pm.registerEvents(new TeamManager(), this);
		pm.registerEvents(new BaseManager(), this);
		pm.registerEvents(new MainListener(), this);
		
		main();
		
		
	}

	@Override
	public void onDisable() {
		DBHandler.unload();
		saveConfig();
	}
	
	public static void main() {
		GameStateManager.switchGameState(LOBBY_STATE);
		
	}
	
	
}
