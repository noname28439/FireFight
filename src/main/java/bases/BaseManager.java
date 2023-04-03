package bases;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import game.GameStateManager;
import main.Main;
import settings.Settings;
import util.ItemBuilder;
import util.WorldManager;

public class BaseManager implements Listener{
	
	public static ArrayList<BuildProcess> buildProcesses = new ArrayList<>();
	
	public static String buildDataToWorldName(Player builder, int buildid, int buildtime) {
		return "BaseBlueprint."+builder.getUniqueId()+"."+buildtime+"."+buildid;
	}
	
	static void givePlayerRandomBuildItem(Player p) {
		ItemStack toAdd = new ItemStack(Material.WHITE_WOOL, new Random().nextInt(5)+1);
		
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
			if(new Random().nextInt(2)==0)
				toAdd = new ItemStack(Material.ANVIL, 1);
			else
				toAdd = new ItemStack(Material.OBSIDIAN, 1);
		if(choice==10)
			if(new Random().nextInt(2)==0)
				toAdd = new ItemStack(Material.SPRUCE_FENCE, 1);
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
		if(choice==19)
			toAdd = new ItemStack(Settings.selfRepairBlockMaterial, 1);
		if(choice==20)
			toAdd = new ItemStack(Material.TORCH, new Random().nextInt(2)+1);
		p.getInventory().addItem(toAdd);
	}
	
	
	public void openBuildWorld(Player builder, int worldid, int time) {
		World buildword = WorldManager.loadWorld(BaseManager.buildDataToWorldName(builder, worldid, time));
		buildword.setTime(1000);
		buildword.setWeatherDuration(0);
		buildword.setGameRuleValue("doDaylightCycle", "false");
		buildword.setGameRuleValue("doWeatherCycle", "false");
		builder.setGameMode(GameMode.SPECTATOR);
		builder.teleport(new Location(buildword, 0, 10, 0));
		builder.sendMessage(ChatColor.GREEN+"Du kannst /edit nutzen, um diese Welt zu bearbeiten!");
		builder.sendMessage(ChatColor.GRAY+"Du kannst /overwrite nutzen, um diese Welt neu zu bebauen!");
	}
	
	
	public static void openBaseManagerInv(Player toOpen) {
		int rows = 3;
		int[] times = Main.times;
		Inventory inv = Bukkit.createInventory(null, 9*rows, ChatColor.AQUA+"BaseManager");
		for(int i = 0; i<9; i++) {
			int min = times[i];
			for(int r = 0; r<rows; r++) {
				ItemBuilder item = new ItemBuilder(Material.MUSIC_DISC_PIGSTEP, 1).setDisplayname(ChatColor.AQUA+"Build"+r+" ("+min+"min.)");
				if(new File(Bukkit.getServer().getWorldContainer(), buildDataToWorldName(toOpen, r, min)).exists())
					item.addEnchantment(Enchantment.LUCK, 1);
				inv.setItem(i+r*9, item.build());
			}
				
		}
		
		toOpen.openInventory(inv);
	}
	
	public static int blueprintIdFromName(String name) {
		return Integer.valueOf(name.split(" ")[0].replace(ChatColor.AQUA+"Build", ""));
	}
	public static int blueprintTimeFromName(String name) {
		return Integer.valueOf(name.split("\\(")[1].replace("min.)", ""));
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		String title = e.getView().getTitle();
		ItemStack clicked = e.getCurrentItem();
		if(clicked==null)
			return;
		if(title.equalsIgnoreCase(ChatColor.AQUA+"BaseManager")) {
			e.setCancelled(true);
			
			int buildid = blueprintIdFromName(clicked.getItemMeta().getDisplayName());
			int time = blueprintTimeFromName(clicked.getItemMeta().getDisplayName());
			
			openBuildWorld((Player)e.getWhoClicked(), buildid, time);
			
		}
	}
	@EventHandler
	public void onItemInteract(PlayerInteractEvent e) {
		if((e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) && GameStateManager.currentGameState == Main.LOBBY_STATE && e.getItem()!=null) 
			if(e.getItem().getType() == Material.BRICKS) {e.setCancelled(true); openBaseManagerInv(e.getPlayer());}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(!p.getWorld().getName().startsWith("BaseBlueprint")) return;
		
		
		if(e.getBlock().getLocation().getBlockZ()>-10)
			e.setCancelled(true);
		if(e.getBlock().getLocation().getBlockZ()<-Settings.maxBuildDepth)
			e.setCancelled(true);
		if(e.getBlock().getLocation().getBlockX()>Settings.maxBuildWidth||e.getBlock().getLocation().getBlockX()<-Settings.maxBuildWidth)
			e.setCancelled(true);
		
		if(e.getBlock().getLocation().getBlockY()>Settings.maxBuildHeight) {
			e.setCancelled(true);
			p.sendMessage("H�r auf Skybases zu Bauen, du Schei�kind!");
		}
	}
	
	@EventHandler
	public void onBlockbreakClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!p.getWorld().getName().startsWith("BaseBlueprint")) return;
		if(!Main.playerBuilding(p)) return;
		if(e.getClickedBlock()==null) return;
		if(e.getItem()==null) return;
		
		//check if block is out of buildrange
		if(e.getClickedBlock().getLocation().getBlockZ()>-10)
			e.setCancelled(true);
		if(e.getClickedBlock().getLocation().getBlockZ()<-Settings.maxBuildDepth)
			e.setCancelled(true);
		if(e.getClickedBlock().getLocation().getBlockX()>Settings.maxBuildWidth||e.getClickedBlock().getLocation().getBlockX()<-Settings.maxBuildWidth)
			e.setCancelled(true);
		
		if(e.getClickedBlock().getLocation().getBlockY()>Settings.maxBuildHeight) {
			e.setCancelled(true);
		}
		
		if(e.getAction()==Action.LEFT_CLICK_BLOCK)
			if(e.getItem().getType()==Material.NETHER_STAR && !e.isCancelled()) {
				for(ItemStack cis: e.getClickedBlock().getDrops())
					p.getInventory().addItem(cis);
				e.getClickedBlock().setType(Material.AIR);
			}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(!p.getWorld().getName().startsWith("BaseBlueprint")) return;
		
		if(e.getBlock().getLocation().getBlockZ()>-10)
			e.setCancelled(true);
		if(e.getBlock().getLocation().getBlockZ()<-Settings.maxBuildDepth)
			e.setCancelled(true);
		if(e.getBlock().getLocation().getBlockX()>Settings.maxBuildWidth||e.getBlock().getLocation().getBlockX()<-Settings.maxBuildWidth)
			e.setCancelled(true);
		
		if(e.getBlock().getLocation().getBlockY()>Settings.maxBuildHeight) {
			e.setCancelled(true);
		}
		

	}
}
