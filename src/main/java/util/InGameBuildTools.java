package util;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import main.Main;

public class InGameBuildTools {

	
	static ArrayList<Location> getBlockLocationsInRect(Location start, Location end) {
		 ArrayList<Location> result = new ArrayList<>();
		//System.out.println("Input: "+start+" to "+end);
		
				if(start.getBlockX()>end.getBlockX()) {
					int saved = start.getBlockX();
					start.setX(end.getX());
					end.setX(saved);
				}
				if(start.getBlockY()>end.getBlockY()) {
					int saved = start.getBlockY();
					start.setY(end.getY());
					end.setY(saved);
				}
				if(start.getBlockZ()>end.getBlockZ()) {
					int saved = start.getBlockZ();
					start.setZ(end.getZ());
					end.setZ(saved);
				}
				
				//System.out.println("Overwritten: "+start+" to "+end);
				
				for(int x = start.getBlockX();x<=end.getBlockX();x++)
					for(int y = start.getBlockY();y<=end.getBlockY();y++)
						for(int z = start.getBlockZ();z<=end.getBlockZ();z++)
							result.add(new Location(start.getWorld(), x, y, z));
				return result;
	}
	
	public static boolean fillRect(Player p, Location start, Location end) {
		boolean finished = true;
		ArrayList<Location> blockLocs = getBlockLocationsInRect(start, end);
		for(Location cl : blockLocs) {			
			Location currentPlaceLoc = new Location(p.getWorld(), cl.getX(), cl.getY(), cl.getZ());
			if(p.getWorld().getBlockAt(currentPlaceLoc).getType()==Material.AIR) {
				finished = false;
				if(p.getItemInHand().getAmount()>0) {
					finished = true;
					p.getWorld().getBlockAt(currentPlaceLoc).setType(p.getItemInHand().getType());
					p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
				}
			}

		}
		return finished;
	}
	
	
	public static void markArea(Player p,  Location start, Location end, int seconds) {
		
		ArrayList<Location> blockLocs = getBlockLocationsInRect(start, end);
		for(Location cl : blockLocs) {
			Location currentPlaceLoc = new Location(p.getWorld(), cl.getX(), cl.getY(), cl.getZ());
			if(p.getWorld().getBlockAt(currentPlaceLoc).getType()==Material.AIR)
				p.sendBlockChange(currentPlaceLoc, Material.WHITE_STAINED_GLASS.createBlockData());
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				for(Location cl : blockLocs) {
					Location currentPlaceLoc = new Location(p.getWorld(), cl.getX(), cl.getY(), cl.getZ());
					p.sendBlockChange(currentPlaceLoc,p.getWorld().getBlockAt(currentPlaceLoc).getBlockData());
				}
				
			}
		}, seconds*20);
		
		
	}
	
	
	
	
}
