package main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import commands.BuildCMD;
import commands.JoinCMD;
import commands.SpawnCMD;
import commands.SpectateCMD;
import commands.TestCMD;
import game.BuildState;
import game.FightState;
import game.GameState;
import game.GameStateManager;
import game.LobbyState;
import teams.TeamManager;

public class Main extends JavaPlugin{
	
	public static JavaPlugin plugin;
	public static PluginManager pm;
	
	public static GameState 
						LOBBY_STATE = new LobbyState(),
						BUILD_STATE = new BuildState(),
						FIGHT_STATE = new FightState();
			
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		//Adding Commmands
		getCommand("test").setExecutor(new TestCMD());
		getCommand("spectate").setExecutor(new SpectateCMD());
		getCommand("build").setExecutor(new BuildCMD());
		getCommand("spawn").setExecutor(new SpawnCMD());
		getCommand("join").setExecutor(new JoinCMD());
		
		
		//Adding Listeners
		pm = Bukkit.getPluginManager();
		pm.registerEvents(new TeamManager(), this);
		
		main();
		
		
	}
	
	public static void main() {
		GameStateManager.switchGameState(LOBBY_STATE);
		
	}
	
	
}
