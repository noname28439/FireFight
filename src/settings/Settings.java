package settings;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

public class Settings {

	public static Location spawn = new Location(Bukkit.getWorld("world"), 126.5, 70, -41.5);
	
	public static Material selfRepairBlockMaterial = Material.PINK_WOOL;
	
	public static int lobbySeconds = 20;
	
}
