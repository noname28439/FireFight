package main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import bases.BaseManager;
import bases.BuildProcess;
import commands.BuildCMD;
import commands.JoinCMD;
import commands.SpawnCMD;
import commands.SpectateCMD;
import commands.TestCMD;
import game.FightState;
import game.GameState;
import game.GameStateManager;
import game.LobbyState;
import game.MainListener;
import teams.TeamManager;

public class Main extends JavaPlugin{
	
	public static JavaPlugin plugin;
	public static PluginManager pm;
	
	public static GameState 
						LOBBY_STATE = new LobbyState(),
						FIGHT_STATE = new FightState();
	
	public static String base1worldname = "BaseBlueprint.default";
	public static String base2worldname = "BaseBlueprint.default";
	
	public static int roundtime = 1;
	public static int[] times = new int[] {1, 5, 10, 15, 20, 30, 45, 60, 120};
	
	public static String locationToString(Location input) {
		return "["+input.getX()+"|"+input.getY()+"|"+input.getZ()+"]";
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
		
		//Adding Commmands
		getCommand("test").setExecutor(new TestCMD());
		getCommand("spectate").setExecutor(new SpectateCMD());
		getCommand("build").setExecutor(new BuildCMD());
		getCommand("spawn").setExecutor(new SpawnCMD());
		getCommand("join").setExecutor(new JoinCMD());
		getCommand("overwrite").setExecutor(new OverwriteCMD());
		getCommand("setgametime").setExecutor(new SetGameTimeCMD());
		
		
		//Adding Listeners
		pm = Bukkit.getPluginManager();
		pm.registerEvents(new TeamManager(), this);
		pm.registerEvents(new BaseManager(), this);
		pm.registerEvents(new MainListener(), this);
		
		main();
		
		
	}
	
	public static void main() {
		GameStateManager.switchGameState(LOBBY_STATE);
		
	}
	
	
}
