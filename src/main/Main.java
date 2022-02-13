package main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import commands.BuildCMD;
import commands.JoinCMD;
import commands.SpawnCMD;
import commands.SpectateCMD;
import commands.TestCMD;

public class Main extends JavaPlugin{
	
	public static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		
		plugin = this;
		
		/*
		   test:
		   spectate:
		   build:
		   spawn:
		   join:
		 */
		getCommand("test").setExecutor(new TestCMD());
		getCommand("spectate").setExecutor(new SpectateCMD());
		getCommand("build").setExecutor(new BuildCMD());
		getCommand("spawn").setExecutor(new SpawnCMD());
		getCommand("join").setExecutor(new JoinCMD());
		
		
		
		
	}
	
}
