package bases;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import settings.Settings;
import teams.TeamManager;
import util.WorldManager;

public class BuildProcess {
	
	int schedulerID;
	int leftbuildseconds;
	World buildWorld;
	
	public BuildProcess(Player builder, int buildid, int buildtime) {
		
		buildWorld = WorldManager.createEmptyWorld("BaseBlueprint."+builder.getUniqueId()+"."+buildtime+"."+buildid);
		buildWorld.getBlockAt(0, 5, 0).setType(Material.PINK_WOOL);
		
		setupPlayer(builder);
		
		leftbuildseconds = buildtime;
		schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() { @Override public void run() {
			leftbuildseconds--;
			
			if(leftbuildseconds<=0) {
				finishBuild();
			}
			
			builder.setLevel(leftbuildseconds);
			
		}}, 1*20, 1*20);
	}
	
	void setupPlayer(Player toSetup){
		toSetup.teleport(new Location(buildWorld, 0, 10, 0));
		toSetup.setGameMode(GameMode.SURVIVAL);
		toSetup.setAllowFlight(true);
		
		//Adding buildStartItems
		toSetup.getInventory().clear();
		toSetup.getInventory().addItem(new ItemStack(Material.NETHER_STAR));
		toSetup.getInventory().addItem(new ItemStack(Material.WHITE_WOOL, 32));
		toSetup.getInventory().addItem(new ItemStack(Material.TARGET));
		toSetup.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Settings.buildSeconds*20, 5));
		toSetup.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Settings.buildSeconds*20, 5));
	}
	
	public void finishBuild() {
		BaseManager.buildProcesses.remove(this);
		Bukkit.getScheduler().cancelTask(schedulerID);
	}
	
}
