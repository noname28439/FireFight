package util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;


public class WorldManager {

	public static World loadWorld(String worldName) {
		WorldCreator wc = new WorldCreator(worldName);
		wc.generator(new VoidChunkGenerator());
		World world = Bukkit.createWorld(wc);
		while(Bukkit.getWorld(worldName)==null) {
			try { Thread.currentThread().sleep(100); } catch (InterruptedException e) {}
		}
		return world;
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