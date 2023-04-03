package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.GameStateManager;
import main.Main;

public class SpawnCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		Player p = (Player) arg0;
		tpSpawn(p);
		return false;
	}

	public static void tpSpawn(Player p) {
		if(GameStateManager.currentGameState == Main.LOBBY_STATE)
			Main.LOBBY_STATE.handleJoin(p);
		else
			p.sendMessage(ChatColor.RED+" only in Lobby available!");
	}
	
}
