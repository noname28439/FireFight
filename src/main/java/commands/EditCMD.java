package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bases.BaseManager;
import bases.BuildProcess;
import main.Main;

public class EditCMD implements CommandExecutor {

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
		Main.LOBBY_STATE.handleJoin(p);
		int bpid = Integer.valueOf(worldname.split("\\.")[3]);
		int bptime = Integer.valueOf(worldname.split("\\.")[2]);
		
		BaseManager.buildProcesses.add(new BuildProcess(p, bpid, bptime, false));
		
		
		
		return false;
	}
	
}
