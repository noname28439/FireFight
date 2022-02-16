package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import game.GameStateManager;
import main.Main;

public class TestCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] args) {
		
		Player p = (Player) arg0;
		
		if(!p.isOp()) {
			p.sendMessage(ChatColor.RED+"Verpiss dich du Wicht!");
			return false;
		}
		
		if(args[0].equals("ib")) {
			Player toTest = Bukkit.getPlayer(args[1]);
			p.sendMessage(toTest.getName()+" building "+Main.playerBuilding(toTest));
		}else if(args[0].equals("inv")) {
			p.sendMessage("inv:"+p.getWorld().getBlockAt(p.getLocation()).getType());
			BlockData bd = p.getWorld().getBlockAt(p.getLocation()).getBlockData();
			if(bd instanceof Directional) {
				Directional dir = (Directional) bd;
				dir.setFacing(dir.getFacing().getOppositeFace());
				p.getWorld().getBlockAt(p.getLocation()).setBlockData(bd);
			}
		}else
		
		
		if(GameStateManager.currentGameState==Main.LOBBY_STATE)
			GameStateManager.switchGameState(Main.FIGHT_STATE);
		else
			p.sendMessage(ChatColor.RED+" only in Lobby available!");
		
		return false;
	}

}
