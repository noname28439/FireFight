package game;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;

import settings.Settings;

public class BuildState extends GameState{

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
	
	@Override
	public void start() {
		
		
		
	}

	@Override
	public void stop() {
		
		
	}

	@Override
	public void handleJoin(Player joined) {
		
		
	}

	@Override
	public void handleLeave(Player leaved) {
		
		
	}

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