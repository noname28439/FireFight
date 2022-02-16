package bases;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import settings.Settings;
import util.WorldManager;

public class BuildProcess {
	
	int schedulerID;
	int leftbuildseconds;
	World buildWorld;
	public String player;
	
	public Player getBuilder() {
		return Bukkit.getPlayer(player);
	}
	
	public BuildProcess(Player builder, int buildid, int buildtime) {
		player = builder.getName();
		leftbuildseconds = buildtime*60;
		buildWorld = WorldManager.createEmptyWorld(BaseManager.buildDataToWorldName(builder, buildid, buildtime));
		buildWorld.getBlockAt(0, 5, -20).setType(Material.PINK_WOOL);
		
		for(int x = -10; x<10; x++)
			for(int y = 3; y<30; y++)
				buildWorld.getBlockAt(x, y, 20).setType(Material.RED_WOOL);
		
		setupPlayer(builder);
		
		
		schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() { @Override public void run() {
			leftbuildseconds--;
			
			if(getBuilder()==null || getBuilder().getWorld()!=buildWorld) {
				finishBuild();
				return;
			}
				
			
			BaseManager.givePlayerRandomBuildItem(getBuilder());
			
			if(leftbuildseconds<=0) {
				finishBuild();
			}
			
			
			
			getBuilder().setLevel(leftbuildseconds);
			
		}}, 1*20, 1*20);
	}
	
	void setupPlayer(Player toSetup){
		toSetup.teleport(new Location(buildWorld, 0.5, 10, -19.5));
		toSetup.setGameMode(GameMode.SURVIVAL);
		toSetup.setAllowFlight(true);
		
		//Adding buildStartItems
		toSetup.getInventory().clear();
		toSetup.getInventory().addItem(new ItemStack(Material.NETHER_STAR));
		toSetup.getInventory().addItem(new ItemStack(Material.WHITE_WOOL, 32));
		toSetup.getInventory().addItem(new ItemStack(Material.TARGET));
		toSetup.getInventory().addItem(new ItemStack(Material.TNT));
		toSetup.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, leftbuildseconds*20, 5));
		toSetup.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, leftbuildseconds*20, 5));
	}
	
	public void finishBuild() {
		System.out.println("Finishing build from "+player);
		if(getBuilder()!=null)
			Main.LOBBY_STATE.handleJoin(getBuilder());
		BaseManager.buildProcesses.remove(this);
		Bukkit.getScheduler().cancelTask(schedulerID);
	}
	
}
