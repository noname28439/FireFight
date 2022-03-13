package commands;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewCMD implements CommandExecutor {

	static void TravelWorldAddIndex(Player p, int steps) {
		File[] worldFiles = new File(".").listFiles(new FileFilter() {
		    @Override
		    public boolean accept(File file) {
		        return file.isDirectory()&&file.getName().startsWith("InGameWorld");
		    }
		});
		
		List<String> worldList = new ArrayList<String>();
		for(File cf : worldFiles) worldList.add(cf.getName());
		
		
		//List<World> worldList = Bukkit.getServer().getWorlds();
		
		
		int currentWorldIndex = worldList.indexOf(p.getWorld().getName());
		
		int targetWorldIndex = currentWorldIndex+steps;
		if(targetWorldIndex<0)
			targetWorldIndex = worldList.size()+targetWorldIndex;
		if(targetWorldIndex>=worldList.size())
			targetWorldIndex = targetWorldIndex-worldList.size();
		
		p.sendMessage("traveling to World "+String.valueOf(worldList.get(targetWorldIndex))+" [Index: "+String.valueOf(targetWorldIndex)+"/"+worldList.size()+"]");
		loadAndTravelWorld(p, worldList.get(targetWorldIndex));
			
	}
	
	static void travedWorld(Player p, World target) {
		p.setGameMode(GameMode.SPECTATOR);
		p.teleport(new Location(target, 0, 20, 0));
	}
	
	static void loadAndTravelWorld(Player p, String target) {
		World created = new WorldCreator(target).createWorld();
		travedWorld(p, created);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		Player p = (Player)sender;
		
		if(args.length==2) {
			if(args[0].equalsIgnoreCase("wn")) {
				loadAndTravelWorld(p, args[1]);
			}
		}else if(args.length==1) {
			if(args[0].equalsIgnoreCase("wb"))
				TravelWorldAddIndex(p, -1);
			if(args[0].equalsIgnoreCase("wf"))
				TravelWorldAddIndex(p, 1);
		}
			
					
		
		return false;
	}

}