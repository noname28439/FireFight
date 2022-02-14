package bases;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import game.GameStateManager;
import main.Main;
import settings.Settings;
import teams.TeamManager;
import util.ItemBuilder;

public class BaseManager implements Listener{
	
	public static ArrayList<BuildProcess> buildProcesses = new ArrayList<>();
	
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
	
	
	public void startBuildWorld(Player builder, int worldid, int time) {
		buildProcesses.add(new BuildProcess(builder, worldid, time));
	}
	
	
	public static Inventory getBaseManagerInv() {
		int rows = 3;
		int[] times = new int[] {1, 5, 10, 15, 20, 30, 45, 60, 120};
		Inventory inv = Bukkit.createInventory(null, 9*rows, ChatColor.AQUA+"BaseManager");
		for(int i = 0; i<9; i++) {
			int min = times[i];
			for(int r = 0; r<rows; r++)
				inv.setItem(i+r*9, new ItemBuilder(Material.MUSIC_DISC_PIGSTEP, 1).setDisplayname(ChatColor.AQUA+"Build"+r+" ("+min+"min.)").build());
		}
		
		return inv;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		HumanEntity p = e.getWhoClicked();
		String title = e.getView().getTitle();
		ItemStack clicked = e.getCurrentItem();
		if(clicked==null)
			return;
		if(title.equalsIgnoreCase(ChatColor.AQUA+"BaseManager")) {
			e.setCancelled(true);
			
			int buildid = Integer.valueOf(clicked.getItemMeta().getDisplayName().split(" ")[0].replace(ChatColor.AQUA+"Build", ""));
			int time = Integer.valueOf(clicked.getItemMeta().getDisplayName().split("\\(")[1].replace("min.)", ""));
			
			startBuildWorld((Player)e.getWhoClicked(), buildid, time);
			
			
		}
	}
	@EventHandler
	public void onItemInteract(PlayerInteractEvent e) {
		if((e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) && GameStateManager.currentGameState == Main.LOBBY_STATE && e.getItem()!=null) 
			if(e.getItem().getType() == Material.BRICKS) {e.setCancelled(true); e.getPlayer().openInventory(getBaseManagerInv());}
	}

}
