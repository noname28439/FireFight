package commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bases.BaseManager;
import bases.BuildProcess;
import main.Main;
import util.WorldManager;

public class OverwriteCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = (Player) arg0;
		
		if(Main.playerBuilding(p)) {
			p.sendMessage(ChatColor.RED+"Du kannst diesen Befehl gerade nicht nutzen!");
			return false;
		}
		
		if(!p.getWorld().getName().startsWith("BaseBlueprint")) {
			p.sendMessage(ChatColor.RED+"Du kannst diesen Befehl gerade nicht nutzen!");
			return false;
		}
		
		String worldname = p.getWorld().getName();
		File worldFolder = p.getWorld().getWorldFolder();
		for(Player cp : p.getWorld().getPlayers())
			Main.LOBBY_STATE.handleJoin(cp);
		Bukkit.unloadWorld(worldname, false);
		System.out.println("Worlddelete "+worldname+" --> "+deleteDirectory(worldFolder));
		int bpid = Integer.valueOf(worldname.split("\\.")[3]);
		int bptime = Integer.valueOf(worldname.split("\\.")[2]);
		
		BaseManager.buildProcesses.add(new BuildProcess(p, bpid, bptime, true));
		
		
		
		return false;
	}
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
}
