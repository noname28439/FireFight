package game;

import java.sql.Timestamp;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import main.Main;
import settings.Settings;
import teams.Team;
import teams.TeamManager;

public class BuildState extends GameState{
	
	static World generateWorld() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String worldName = "InGameWorld."+ts.getYear()+"."+ts.getMonth()+"."+ts.getDay()+"."+ts.getHours()+"."+ts.getMinutes();
		WorldCreator wc = new WorldCreator(worldName);
		wc.generator(new VoidChunkGenerator());
		World world = Bukkit.createWorld(wc);
		while(Bukkit.getWorld(worldName)==null) {
			try { Thread.currentThread().sleep(100); } catch (InterruptedException e) {}
		}
		world.getBlockAt(0, 10, 0).setType(Material.COBWEB);
		world.setGameRuleValue("randomTickSpeed", "1");
		world.setGameRuleValue("fallDamage", "false");
		world.setGameRuleValue("doFireTick", "false");
		world.setGameRuleValue("doWeatherCycle", "false");
		world.setGameRuleValue("keepInventory", "true");
		world.getWorldBorder().setSize(100.0);
		return world;
	}
	static void givePlayerRandomBuildItem(Player p) {
		ItemStack toAdd = new ItemStack(TeamManager.getPlayerTeam(p).getButtonMaterial(), new Random().nextInt(5)+1);
		
		int choice = new Random().nextInt(25);
		if(choice==0)
			toAdd = new ItemStack(Material.LADDER, 1);
		if(choice==1)
			toAdd = new ItemStack(Material.DIRT, new Random().nextInt(2)+1);
		if(choice==2)
			toAdd = new ItemStack(Material.JUNGLE_WOOD, new Random().nextInt(2)+1);
		if(choice==3)
			toAdd = new ItemStack(Material.ACACIA_STAIRS, 1);
		if(choice==4)
			toAdd = new ItemStack(Material.BLACK_BANNER, 1);
		if(choice==5)
			toAdd = new ItemStack(Material.DARK_OAK_TRAPDOOR, 1);
		if(choice==6)
			toAdd = new ItemStack(Material.DARK_OAK_FENCE_GATE, 1);
		if(choice==7)
			toAdd = new ItemStack(Material.DARK_OAK_DOOR, 1);
		if(choice==8)
			toAdd = new ItemStack(Material.LADDER, 1);
		if(choice==9)
			if(new Random().nextInt(3)==0) {
				if(new Random().nextInt(Bukkit.getOnlinePlayers().size())== 0) {
					for(Player cp : Bukkit.getOnlinePlayers())
						cp.getInventory().addItem(new ItemStack(Material.TNT, 1));
					toAdd = new ItemStack(Material.OBSIDIAN, 1);
				}
			}
		if(choice==10)
			if(new Random().nextInt(8)==0)
				toAdd = new ItemStack(Material.TARGET, 1);
			else
				toAdd = new ItemStack(Settings.selfRepairBlockMaterial, new Random().nextInt(2));
		if(choice==15)
			toAdd = new ItemStack(Material.SAND, 1);
		if(choice==16)
			toAdd = new ItemStack(Material.SAND, 1);
		if(choice==17)
			toAdd = new ItemStack(Material.GRAVEL, 1);
		if(choice==18)
			if(new Random().nextInt(2)==0)
				toAdd = new ItemStack(Material.ANVIL, 1);
		p.getInventory().addItem(toAdd);
	}
	
	World gameworld;
	public int SchedulerID, leftbuildseconds = Settings.buildSeconds;
	
	@Override
	public void start() {
		//TODO: Set team spawn locations, load team bases...
		
		SchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() { @Override public void run() {
			leftbuildseconds--;
			
			if(leftbuildseconds<=0) {
				GameStateManager.switchGameState(Main.FIGHT_STATE);
			}
			
			for(Player cp : Bukkit.getOnlinePlayers())
				cp.setLevel(leftbuildseconds);
		}}, 1*20, 1*20);
		
		gameworld = generateWorld();
		for(Player cp : Bukkit.getOnlinePlayers()) setupPlayer(cp);
		
	}
	

	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(SchedulerID);
		for(Player cp : Bukkit.getOnlinePlayers()) {
			cp.setAllowFlight(false);
		}
	}

	@Override
	public void handleJoin(Player joined) {
		setupPlayer(joined);
		
	}
	public void setupPlayer(Player toSetup) {
		if(TeamManager.getPlayerTeam(toSetup)==null) { 			//no team --> Spectator
			toSetup.setGameMode(GameMode.SPECTATOR);
			toSetup.teleport(new Location(gameworld, 0, 100, 0));
		}else { 												//in team team --> Spectator
			Team playerTeam = TeamManager.getPlayerTeam(toSetup);
			
			
			toSetup.teleport(TeamManager.getPlayerTeam(toSetup).getRespawnPoint());
			toSetup.setGameMode(GameMode.SURVIVAL);
			toSetup.setAllowFlight(true);
			
			//Adding buildStartItems
			toSetup.getInventory().addItem(new ItemStack(Material.NETHER_STAR));
			toSetup.getInventory().addItem(new ItemStack(playerTeam.getButtonMaterial(), 32));
			toSetup.getInventory().addItem(new ItemStack(Material.TARGET));
			toSetup.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Settings.buildSeconds*20, 5));
			toSetup.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Settings.buildSeconds*20, 5));
		}
	}

	@Override
	public void handleLeave(Player leaved) {
		
		
	}
	
	
	@EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {e.setRespawnLocation(TeamManager.getPlayerTeam(e.getPlayer()).getRespawnPoint());}

}

class VoidChunkGenerator extends ChunkGenerator {
	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunkData = super.createChunkData(world);
		for(int x = 0; x < 16; x++) {for(int z = 0; z < 16; z++) {biome.setBiome(x, z, Biome.PLAINS);}}
		return chunkData;
	}
	@Override public boolean canSpawn(World world, int x, int z) {return true;}
	@Override public Location getFixedSpawnLocation(World world, Random random) {return new Location(world, 0, 100, 0);}
}