package main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetGameTimeCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] args) {
		Player p = (Player) arg0;
		
		if(!p.isOp()) {
			p.sendMessage(ChatColor.RED+"Du darfst diesen Befehl nicht ausführen!");
			return false;
		}
		
		int time = Integer.valueOf(args[0]);
		
		Main.roundtime = time;
		
		p.sendMessage(ChatColor.GREEN+"Game Time set to "+time+" minutes.");
		
		return false;
	}

}
