package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.GameStateManager;
import main.Main;

public class TestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		
		Player p = (Player) arg0;
		
		if(!p.isOp()) {
			p.sendMessage(ChatColor.RED+"Verpiss dich du Wicht!");
			return false;
		}
		
		if(GameStateManager.currentGameState==Main.LOBBY_STATE)
			GameStateManager.switchGameState(Main.BUILD_STATE);
		else
			p.sendMessage(ChatColor.RED+" only in Lobby available!");
		
		return false;
	}

}
