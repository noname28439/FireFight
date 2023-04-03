package game;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		joined.setHealth(joined.getMaxHealth());
		joined.setFoodLevel(20);
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
	@EventHandler public void onDrop(PlayerDropItemEvent e) {if(hideAndSeekEnabled(e.getPlayer())) return;e.setCancelled(true);}
	@EventHandler public void onInvclick(InventoryClickEvent e) {if(hideAndSeekEnabled(e.getWhoClicked())) return; if(!e.getWhoClicked().isOp() && !Main.playerBuilding((Player) e.getWhoClicked())) e.setCancelled(true);}
	
	
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
				p.sendMessage(ChatColor.RED+"Du musst ein Team ausgew�hlt haben!");
				return;
			}else {
				if(TeamManager.getPlayerTeam(p.getName())==Team.BLUE) {
					Main.base1worldname = worldname;
				}else {
					Main.base2worldname = worldname;
				}
			}
			p.sendMessage(ChatColor.GREEN+clicked.getItemMeta().getDisplayName()+" erfolgreich als Base f�r Team "+TeamManager.getPlayerTeam(p.getName()).getTeamName()+" festgelegt!");
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
	
	
	
	
	//HideAndSeek
	
	public static boolean hideAndSeekEnabled(Entity p) {
		return (p.getWorld().getName().equals("world") && Settings.hideAndSeek);
	}
	
	
	@EventHandler
	public void OnPlayerDamage(EntityDamageByEntityEvent e) {
		if(!hideAndSeekEnabled(e.getEntity())) return;
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player whoWasHit = (Player) e.getEntity();
            Player whoHit = (Player) e.getDamager();
            //whoHit.sendMessage("Du hast "+e.getDamage()+" Damage gemacht!");
            
            if(whoWasHit.getPotionEffect(PotionEffectType.LUCK)!=null)
				if(whoWasHit.getPotionEffect(PotionEffectType.LUCK).getAmplifier()==200)
					e.setCancelled(true);
            
            if(Main.isHunter(whoWasHit.getName())) {
            	if(!whoWasHit.isBlocking()) {
//	            		whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 255));
		            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3*20, 200));
		            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 5*20, 200));
		            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3*20, 200));
        }}}
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            Player whoWasHit = (Player) e.getEntity();
            Arrow whoHit = (Arrow) e.getDamager();
            
            if(whoWasHit.getPotionEffect(PotionEffectType.LUCK)!=null)
				if(whoWasHit.getPotionEffect(PotionEffectType.LUCK).getAmplifier()==200)
					e.setCancelled(true);
            
            if(Main.isHunter(whoWasHit.getName())) {
            	if(!whoWasHit.isBlocking()) {
//	            		whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 255));
		            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 3*20, 200));
		            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 5*20, 200));
		            whoWasHit.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3*20, 200));
       }}}}
	
	@EventHandler
	public void OnPlayerMove(PlayerMoveEvent e) {
		if(!hideAndSeekEnabled(e.getPlayer())) return;
			
			Location movedFrom = e.getFrom();
	        Location movedTo = e.getTo();
	        if ((movedFrom.getX() != movedTo.getX() || movedFrom.getY() != movedTo.getY() || movedFrom.getZ() != movedTo.getZ()) && e.getPlayer().isOnGround()) {
					if(e.getPlayer().getPotionEffect(PotionEffectType.FIRE_RESISTANCE)!=null) {
						if(e.getPlayer().getPotionEffect(PotionEffectType.FIRE_RESISTANCE).getAmplifier()==200) {
							//e.setTo(new Location(e.getPlayer().getWorld(),movedFrom.getX() ,movedFrom.getY(), movedFrom.getZ(), e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch()));
							e.getPlayer().setWalkSpeed(0f);
							
						}	
					}else {
						e.getPlayer().setWalkSpeed(0.2f);
					}
					
							
	        }
	        if(Main.isHunter(e.getPlayer().getName())) {
	        	if(new Random().nextInt(1000)==0)
	        		e.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_EYE, 1));
	        }else {
	        	
	        }
	        if(new Random().nextInt(500)==0) {
        		Location dropLoc = e.getPlayer().getLocation();
        		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					@Override
					public void run() {
						if(new Random().nextInt(4)==0)
							e.getPlayer().getWorld().dropItem(dropLoc, new ItemStack(Material.ENDER_PEARL));
						else
							e.getPlayer().getWorld().dropItem(dropLoc, new ItemStack(Material.ARROW));
						
					}
				}, (new Random().nextInt(20)+20)*20);
        	}
	}
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		if(!hideAndSeekEnabled(e.getEntity())) return;
		e.getEntity().setGameMode(GameMode.SPECTATOR);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(!hideAndSeekEnabled(e.getPlayer())) return;
		Player p = e.getPlayer();
		if(e.getClickedBlock()!=null) {
			if(e.getClickedBlock().getType().toString().toLowerCase().contains("warped"))
				e.setCancelled(true);
			if(Main.isHunter(p.getName())|| p.getGameMode()==GameMode.CREATIVE) {
				e.setCancelled(false);
			}
		}
	}
	
	
}
