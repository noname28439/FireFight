package game;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import bases.BaseManager;
import commands.SpawnCMD;
import main.Main;
import settings.Settings;
import teams.Team;
import teams.TeamManager;
import util.ItemBuilder;

public class LobbyState extends GameState{

	static int LobbySchedulerID, lobbycountdown;
	
	
	@Override
	public void start() {
		for(Player cp : Main.getAllPlayers()) {
			handleJoin(cp);
			TeamManager.setPlayerTeam(cp.getName(), null);
		}
		
		LobbySchedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() { @Override public void run() {
			boolean hasEveryTeamOnePlayer = true;
			for(Team ct : Team.values()) if(ct.teamPlayers.size()==0) hasEveryTeamOnePlayer = false;
			
			if(hasEveryTeamOnePlayer)
				lobbycountdown--;
			else
				lobbycountdown = Settings.lobbySeconds;
			
			if(lobbycountdown<=0) {
				GameStateManager.switchGameState(Main.FIGHT_STATE);
			}
			
			for(Player cp : Main.getAllPlayers())
				cp.setLevel(lobbycountdown);
		}}, 0, 20);
		
	}

	@Override
	public void stop() {
		for(Player cp : Main.getAllPlayers()) cp.getInventory().clear();
		Bukkit.getScheduler().cancelTask(LobbySchedulerID);
	}

	@Override
	public void handleJoin(Player joined) {
		
		joined.getInventory().clear();
		joined.setGameMode(GameMode.ADVENTURE);
		joined.teleport(Settings.spawn);
		joined.getInventory().setItem(4, new ItemBuilder(Material.COMPASS, 1).setDisplayname(ChatColor.GOLD+"TeamSelector").build());
		joined.getInventory().setItem(2, new ItemBuilder(Material.BRICKS, 1).setDisplayname(ChatColor.GOLD+"BaseManager").build());
		joined.getInventory().setItem(3, new ItemBuilder(Material.GOLDEN_SHOVEL, 1).setDisplayname(ChatColor.GOLD+"BaseSelector").build());
		joined.getInventory().setItem(0, new ItemBuilder(Material.CAMPFIRE, 1).setDisplayname(ChatColor.GOLD+"Spawn").build());
		if(joined.isOp()) {
			joined.getInventory().setItem(8, new ItemBuilder(Material.TRIDENT, 1).setDisplayname(ChatColor.GOLD+"TimeSelector").build());
		}
		for (PotionEffect effect : joined.getActivePotionEffects())
			joined.removePotionEffect(effect.getType());
		
	}

	@Override
	public void handleLeave(Player leaved) {
		TeamManager.setPlayerTeam(leaved.getName(), null);
	}

	//Forbidden actions
	@EventHandler public void onDrop(PlayerDropItemEvent e) {e.setCancelled(true);}
	@EventHandler public void onInvclick(InventoryClickEvent e) {if(!e.getWhoClicked().isOp() && !Main.playerBuilding((Player) e.getWhoClicked())) e.setCancelled(true);}
	
	
	@EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Settings.spawn);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		String title = e.getView().getTitle();
		ItemStack clicked = e.getCurrentItem();
		Player p = (Player) e.getWhoClicked();
		if(clicked==null)
			return;
		
		if(title.equalsIgnoreCase(ChatColor.AQUA+"TimeSelector")) {
			e.setCancelled(true);
			if(p.isOp()) {
				Main.roundtime = clicked.getAmount();
				p.sendMessage(ChatColor.GREEN+"Game Time set to "+Main.roundtime+" minutes.");
				p.closeInventory();
			}
		}
		
		if(title.equalsIgnoreCase(ChatColor.AQUA+"BaseSelector")) {
			e.setCancelled(true);
			
			if(clicked.getType()!=Material.MUSIC_DISC_PIGSTEP) {
				p.sendMessage(ChatColor.RED+"Diese Map ist nicht in der passenden Zeit gebaut!");
				return;
			}
			
			
			if(clicked.getEnchantmentLevel(Enchantment.LUCK)!=1) {
				p.sendMessage(ChatColor.RED+"Diese Map ist nicht bebaut!");
				return;
			}
			
			int buildid = BaseManager.blueprintIdFromName(clicked.getItemMeta().getDisplayName());
			int buildtime = BaseManager.blueprintTimeFromName(clicked.getItemMeta().getDisplayName());
			
			String worldname = BaseManager.buildDataToWorldName(p, buildid, buildtime);
			
			
			if(TeamManager.getPlayerTeam(p.getName())==null) {
				p.sendMessage(ChatColor.RED+"Du musst ein Team ausgewählt haben!");
				return;
			}else {
				if(TeamManager.getPlayerTeam(p.getName())==Team.BLUE) {
					Main.base1worldname = worldname;
				}else {
					Main.base2worldname = worldname;
				}
			}
			p.sendMessage(ChatColor.GREEN+clicked.getItemMeta().getDisplayName()+" erfolgreich als Base für Team "+TeamManager.getPlayerTeam(p.getName()).getTeamName()+" festgelegt!");
			p.closeInventory();
			
		}
	}
	
	
	@EventHandler
	public void onItemInteract(PlayerInteractEvent e) {
		if((e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) && GameStateManager.currentGameState == Main.LOBBY_STATE && e.getItem()!=null) {
			if(e.getItem().getType()==Material.CAMPFIRE) { SpawnCMD.tpSpawn(e.getPlayer());}
			if(e.getItem().getType() == Material.GOLDEN_SHOVEL) {e.setCancelled(true); openBaseManagerInv(e.getPlayer());}
			if(e.getItem().getType() == Material.TRIDENT) {e.setCancelled(true); if(e.getPlayer().isOp()) {openTimeManagerInv(e.getPlayer());}}
		}
			
	}
	
	
	public static void openBaseManagerInv(Player toOpen) {
		int rows = 3;
		
		Inventory inv = Bukkit.createInventory(null, 9*rows, ChatColor.AQUA+"BaseSelector");
		for(int i = 0; i<9; i++) {
			int min = Main.times[i];
			for(int r = 0; r<rows; r++) {
				ItemBuilder item = new ItemBuilder(Main.roundtime>=min ? Material.MUSIC_DISC_PIGSTEP : Material.MUSIC_DISC_11, 1).setDisplayname(ChatColor.AQUA+"Build"+r+" ("+min+"min.)");
				if(new File(Bukkit.getServer().getWorldContainer(), BaseManager.buildDataToWorldName(toOpen, r, min)).exists())
					item.addEnchantment(Enchantment.LUCK, 1);
				inv.setItem(i+r*9, item.build());
			}
				
		}
		
		toOpen.openInventory(inv);
	}
	
	public static void openTimeManagerInv(Player toOpen) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.AQUA+"TimeSelector");
		for(int i = 0; i<9; i++) {
			int min = Main.times[i];
			inv.setItem(i, new ItemBuilder(Material.EMERALD, min).addEnchantment(Enchantment.LUCK, 1).build());
			
				
		}
		
		toOpen.openInventory(inv);
	}
	
}
