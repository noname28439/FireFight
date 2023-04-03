package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.MainListener;
import util.InGameBuildTools;

public class BuildCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player p = (Player)sender;
		
		if(p.getWorld().getName().startsWith("BaseBlueprint")) {
			
			if(MainListener.playerBuildStartPoints.get(p)==null||MainListener.playerBuildEndPoints.get(p)==null) {
				p.sendMessage(ChatColor.RED+"Du musst erst einen Bereich markieren!");
				return false;
			}
			
			boolean finished = InGameBuildTools.fillRect(p, MainListener.playerBuildStartPoints.get(p), MainListener.playerBuildEndPoints.get(p));
			if(finished) {
				MainListener.playerBuildStartPoints.put(p, null);
				MainListener.playerBuildEndPoints.put(p, null);
				p.sendMessage(ChatColor.GREEN+"Build finished!");
			}
			
		}else {
			p.sendMessage(ChatColor.RED+"Du kannst diesen Befehl nur in der Bauphase einsetzen!");
		}
			

		
		return false;
	}

}
