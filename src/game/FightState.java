package game;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import main.Main;
import settings.Settings;
import teams.Team;
import teams.TeamManager;
import util.ItemBuilder;
import util.WorldManager;

public class FightState extends GameState{
	
	static World generateWorld() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String worldName = "InGameWorld."+ts.getYear()+"."+ts.getMonth()+"."+ts.getDay()+"."+ts.getHours()+"."+ts.getMinutes();
		World world = WorldManager.loadWorld(worldName);
		world.getBlockAt(0, 10, 0).setType(Material.COBWEB);
		world.setGameRuleValue("randomTickSpeed", "1");
		world.setGameRuleValue("fallDamage", "false");
		world.setGameRuleValue("doFireTick", "true");
		world.setGameRuleValue("doWeatherCycle", "false");
		world.setGameRuleValue("keepInventory", "true");
		world.getWorldBorder().setSize(150.0);
		return world;
	}
	
	public static void dropBuildItem(Location dropLoc) {

		Random random = new Random();
		
		ItemStack[] lootTable = new ItemStack[] {
				new ItemStack(Material.SLIME_BALL, random.nextInt(3)+1),
				new ItemStack(Material.REDSTONE, random.nextInt(5)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.DARK_OAK_BUTTON, random.nextInt(4)+1),
				new ItemStack(Material.CRIMSON_DOOR, random.nextInt(2)+1),
				new ItemStack(Material.WARPED_PRESSURE_PLATE, random.nextInt(2)+1),
				new ItemStack(Material.LEVER, random.nextInt(2)+1),
				new ItemStack(Material.REPEATER, random.nextInt(2)+1),
				new ItemStack(Material.OBSERVER, random.nextInt(2)+1),
				new ItemStack(Material.DISPENSER, random.nextInt(2)+1),
				new ItemStack(Material.SCAFFOLDING, random.nextInt(6)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.PINK_WOOL, random.nextInt(4)+1),
				new ItemStack(Material.CHICKEN_SPAWN_EGG, random.nextInt(2)+1),
				new ItemStack(Material.WHEAT_SEEDS, random.nextInt(9)+1),
				new ItemStack(Material.ENDER_PEARL, 1),
				new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
				new ItemStack(Material.BLAZE_ROD, 1),
				new ItemStack(Material.SHIELD, 1),
				new ItemStack(Material.OBSIDIAN, 1),
				new ItemStack(Material.BELL, random.nextInt(2)+1),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(10)+5),
				new ItemStack(Material.DRAGON_HEAD, 1),
				new ItemStack(Material.DIAMOND, random.nextInt(2)+1),
				new ItemStack(Material.COBWEB, 1),
				new ItemStack(Material.CAULDRON, 1),
				new ItemStack(Material.BIRCH_BOAT, 1),
				new ItemStack(Material.MINECART, 1),
				new ItemStack(Material.CHEST, 1),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(2)+2),
				new ItemStack(Material.DIAMOND_SWORD, 1),
				new ItemStack(Material.SPECTRAL_ARROW, random.nextInt(2)+1),
				new ItemStack(Material.FEATHER, random.nextInt(2)+1),
				new ItemStack(Material.FEATHER, random.nextInt(2)+1),
				new ItemStack(Material.FEATHER, random.nextInt(2)+1),
				new ItemStack(Material.RAIL, random.nextInt(5)+2),
				new ItemStack(Material.POWERED_RAIL, random.nextInt(1)+2),
				new ItemStack(Material.BREAD, random.nextInt(2)+3),
				new ItemStack(Material.BREAD, random.nextInt(2)+3),
				new ItemStack(Material.BREAD, random.nextInt(2)+3),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.HONEYCOMB, 1),
				new ItemStack(Material.HONEYCOMB, 1),
				new ItemStack(Material.BIRCH_LOG, random.nextInt(5)+3)
				};
		
		
		ItemStack result = lootTable[new Random().nextInt(lootTable.length)];
		
		dropLoc.getWorld().dropItem(dropLoc, result);
		
	}
	
	World gameworld;
	int FightTickerID,RepairTickerID;
	int playerStartHP;
	public HashMap<Block, Integer> blockDelays = new HashMap<>();
	public ArrayList<Block> toSelfRepairBlocks = new ArrayList<>();
	public HashMap<String, Integer> playerLives = new HashMap<>();
	
	@Override
	public void start() {
		
		/*
		 //KrawattenFreak
		 BaseBlueprint.6a4c3e2d-8449-4e04-83ab-02400cc281ed.1.0	
		 
		 //LeeDo
		 BaseBlueprint.43b6b049-77f7-4796-899b-67dc84fdcd18.5.0	
		 BaseBlueprint.43b6b049-77f7-4796-899b-67dc84fdcd18.5.2
		 
		 */
		
		String base2name = "BaseBlueprint.6a4c3e2d-8449-4e04-83ab-02400cc281ed.1.0";
		String base1name = "BaseBlueprint.43b6b049-77f7-4796-899b-67dc84fdcd18.5.2";
		
		World base1world = WorldManager.loadWorld(base1name);
		World base2world = WorldManager.loadWorld(base2name);
		
		
		playerStartHP = Settings.playerHP;
		
		gameworld = generateWorld();
		
		
		for(int x = -Settings.maxBuildWidth-2; x<Settings.maxBuildWidth+2; x++)
			for(int y = 0; y<=Settings.maxBuildHeight; y++)
				for(int z = -Settings.maxBuildDepth; z<=-10; z++) {
					
					loadWorldBlock(x, y, z, base1world, gameworld, false);
					loadWorldBlock(x, y, z, base2world, gameworld, true);
					
				}
		
		for(Player cp : Bukkit.getOnlinePlayers()) setupPlayer(cp);
		
		FightTickerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
				
				@Override
				public void run() {
					
					ArrayList<Block> toRem = new ArrayList<>();
					for(Block key : blockDelays.keySet()) {
						if(!blockDelays.containsKey(key))
							break;
						blockDelays.put(key, blockDelays.get(key)-1);
						if(blockDelays.get(key)<0)
							toRem.add(key);
					}
					for(Block key : toRem)
						blockDelays.remove(key);
					
					
				
				}
			}, 1*20, 1*20);
		RepairTickerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				ArrayList<Block> toReplaceBlocks = new ArrayList<>();
				for(Block cb : toSelfRepairBlocks) {
					boolean hasNeighbours = false;
					for(int x = 0; x<3;x++)
						for(int y = 0; y<3;y++)
							for(int z = 0; z<3;z++) {
								Location lookupLocation = cb.getLocation().add(x-1, y-1, z-1);
								if(cb.getWorld().getBlockAt(lookupLocation).getType()!=Material.AIR)
									hasNeighbours = true;
							}
					
					if(hasNeighbours)
						toReplaceBlocks.add(cb);
				}
				
				for(Block cb : toReplaceBlocks)
					if(cb.getType()==Material.AIR || cb.getType()==Material.FIRE)
						cb.setType(Settings.selfRepairBlockMaterial);
				
			}
		}, 1*20, 5*20);
	}
	
	public void loadWorldBlock(int x, int y, int z, World origin, World toPlacein, boolean invert) {
		Location ct = new Location(gameworld, x, y, invert ? -z : z);
		
		Block original = origin.getBlockAt(x, y, z);
		Material newMaterial = original.getType();
		if(original.getType()==Material.TARGET)
			if(invert)
				Team.RED.setRespawnPoint(ct.clone().add(0.5, 1, 0.5));
			else
				Team.BLUE.setRespawnPoint(ct.clone().add(0.5, 1, 0.5));
		if(original.getType()==Settings.selfRepairBlockMaterial) toSelfRepairBlocks.add(gameworld.getBlockAt(ct));
		try {
			BlockData bd = original.getState().getBlockData();
			if(invert) {
				System.out.println("invrtin "+Main.locationToString(ct));
				if(bd instanceof Directional) {
					Directional dir = (Directional) bd;
					if(dir.getFacing() == BlockFace.NORTH || dir.getFacing() == BlockFace.SOUTH)
						dir.setFacing(dir.getFacing().getOppositeFace());
					bd = dir;
					
				}
			}
			gameworld.getBlockAt(ct).setBlockData(bd);
			if(original.getType()==Material.WHITE_WOOL) {
				if(invert)
					gameworld.getBlockAt(ct).setType(Material.RED_WOOL);
				else
					gameworld.getBlockAt(ct).setType(Material.BLUE_WOOL);
			}
			//gameworld.getBlockAt(ct)..getBlockData()
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(FightTickerID);
		Bukkit.getScheduler().cancelTask(RepairTickerID);
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
			toSetup.teleport(calculatePlayerRespawnPoint(toSetup));
			toSetup.setGameMode(GameMode.SURVIVAL);
			toSetup.setHealth(20);
			
			ItemStack bow = new ItemStack(Material.BOW, 1);
			ItemMeta bowMeta = bow.getItemMeta();
			bowMeta.addEnchant(Enchantment.ARROW_INFINITE,1, true);
			bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK,3, true);
			bow.setItemMeta(bowMeta);
			toSetup.getInventory().addItem(bow);
			toSetup.getInventory().addItem(new ItemStack(Material.ARROW, 1));
			toSetup.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
			toSetup.getInventory().addItem(new ItemStack(Material.GRAY_WOOL, 32));
			toSetup.getInventory().addItem(new ItemStack(Material.FEATHER, 1));
			
			//toSetup.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 2));
		}
	}

	@Override
	public void handleLeave(Player left) {
		handleTeamPlayerLoss(left);
		
	}
	
	public void handleTeamPlayerLoss(Player gone) {
		Bukkit.broadcastMessage(ChatColor.DARK_RED+gone.getName()+" ist ausgeschieden");
		
		
		//Check if one team has won
		ArrayList<Team> aliveTeams = new ArrayList<>();
		for(Team ct : Team.values()) {
			boolean hasPlayersAlive = false;
			for(Player cp: ct.getTeamPlayerList()) {
				if(cp.getGameMode()==GameMode.SURVIVAL)
					hasPlayersAlive = true;
			}
			if(hasPlayersAlive)
				aliveTeams.add(ct);
		}
		
		if(aliveTeams.size()==1) {
			Bukkit.broadcastMessage(ChatColor.GREEN+"Team "+aliveTeams.get(0).getTeamName()+" hat das Match gewonnen!");
			gameworld.setGameRuleValue("doFireTick", "false");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, ()->{GameStateManager.switchGameState(Main.LOBBY_STATE);}, 10*20);
		}
	}
	
	public Location calculatePlayerRespawnPoint(Player player) {
		
		Team playerTeam = TeamManager.getPlayerTeam(player.getName());
		Location respawnP = playerTeam.getRespawnPoint();
		if(respawnP==null)
			if(playerTeam==Team.BLUE)
				return new Location(gameworld, 0, 10, -20);
			else
				return new Location(gameworld, 0, 10, 20);
		else
			return respawnP;
	}
	
	
	@EventHandler public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		Location respawnLoc = calculatePlayerRespawnPoint(e.getPlayer());
		Block standon = respawnLoc.getBlock().getRelative(0, -1, 0);
		if(standon.getType()==Material.AIR) standon.setType(Material.WHITE_WOOL);
		e.setRespawnLocation(respawnLoc);
		}
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		if(Main.playerBuilding(e.getEntity())) return;
		Player p = e.getEntity();
		
		//give tnt to killer
		if(p.getKiller()!=null) {
			Player killer = p.getKiller();
			killer.getInventory().addItem(new ItemBuilder(Material.TNT, 1).build());
		}
		
		
		
		if(!playerLives.containsKey(p.getName())){
			playerLives.put(p.getName(), playerStartHP);
		}
		
		playerLives.put(p.getName(), playerLives.get(p.getName())-1);
		int playerHP = playerLives.get(p.getName());
		Bukkit.broadcastMessage(ChatColor.RED+p.getName()+" hat noch "+playerHP+" HP...");
		if(playerHP<=0) {
			p.setGameMode(GameMode.SPECTATOR);
			handleTeamPlayerLoss(p);
		}else {
			//p.getWorld().getBlockAt(e.getRespawnLocation().clone().add(0, -5, 0)).setType(Material.PINK_WOOL);
		}
		
		
	}
	
	@EventHandler
	public void onRepairBlockPlace(BlockPlaceEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		if(e.getBlock().getType()==Settings.selfRepairBlockMaterial) {
			toSelfRepairBlocks.add(e.getBlock());
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		Player p = e.getPlayer();
		
			if(e.getBlock().getType()==Material.TNT)
				e.getBlock().getLocation().getWorld().createExplosion(e.getBlock().getLocation(), 2, false);
		
		if(e.getBlock().getType()==Settings.selfRepairBlockMaterial)
			if(toSelfRepairBlocks.contains(e.getBlock()))
				toSelfRepairBlocks.remove(e.getBlock());
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		Player p = e.getPlayer();
		
		
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
		if(e.getClickedBlock().getType()==Material.BOOKSHELF) {
			if(!blockDelays.containsKey(e.getClickedBlock())) {
				dropBuildItem(e.getClickedBlock().getLocation().add(0, 1, 0));
				blockDelays.put(e.getClickedBlock(), 5);
			}
		}
		
		
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK||e.getAction()==Action.RIGHT_CLICK_AIR) {
			if(e.getPlayer().getItemInHand().getType()==Material.FEATHER) {
				int size = 15;
				for(int x = -size; x<size;x++) 
					for(int y = -size; y<size;y++) 
						for(int z = -size; z<size;z++) {
							if(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().add(x, y, z)).getType()==Material.FIRE)
								e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation().add(x, y, z)).setType(Material.GREEN_WOOL);
						}
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getWorld().getBlockAt(e.getPlayer().getLocation()).getType() == Material.CAULDRON) {
				if(e.getPlayer().getItemInHand().getType()==Material.ARROW) {
					if(e.getPlayer().getFoodLevel()>1) {
						//e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 100));
						e.getPlayer().launchProjectile(Arrow.class);
						if(new Random().nextInt(2)==0)
							e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel()-1);
					}
					
				}
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.HEART_OF_THE_SEA) {
				
				Team targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
				while(targetTeam==TeamManager.getPlayerTeam(p))
					targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
				
				Player targetPlayer = targetTeam.getTeamPlayerList().get(new Random().nextInt(targetTeam.getTeamPlayerList().size()));
				while(targetPlayer.getGameMode()!=GameMode.SURVIVAL)
					targetPlayer = targetTeam.getTeamPlayerList().get(new Random().nextInt(targetTeam.getTeamPlayerList().size()));
				
				p.sendMessage("Player: "+targetPlayer);
				
				Location toBomb = targetPlayer.getLocation();
				
				for(int i = 0; i<20;i++) {
					Location in = toBomb.clone().add(new Random().nextInt(10)-5,Settings.maxBuildHeight+30, new Random().nextInt(10)-5);
					toBomb.getWorld().spawnEntity(in, EntityType.SNOWBALL);
				}
				
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.HONEYCOMB) {
				
				Team targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
				while(targetTeam==TeamManager.getPlayerTeam(p))
					targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
				
				for(Player cp: targetTeam.getTeamPlayerList())
					cp.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30*20, 1));
				
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.FIRE_CHARGE) {
				e.getPlayer().launchProjectile(Fireball.class);
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.BLAZE_ROD) {
				int size = 3;
				for(int x = 0; x<3;x++)
					for(int z = 0; z<3; z++) {
						p.getWorld().getBlockAt(p.getLocation().add(x, 0, z).add(-1, -3, -1)).setType(Material.PINK_WOOL);
					}
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
		}
		
		//Pull items from tnt
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
			if(e.getClickedBlock().getType()==Material.TNT) {
				if(!blockDelays.containsKey(e.getClickedBlock())) {
					
					int choice = new Random().nextInt(5);
					
					ItemStack result = new ItemStack(Material.CAKE, 1);
					
					if(choice==0)
						if(new Random().nextInt(2)!=0)
							result = new ItemStack(Material.TRIDENT, 1);
						else
							result = new ItemStack(Material.SNOWBALL, 1);
					if(choice==1)
						if(new Random().nextInt(5)!=0)
							result = new ItemStack(Material.EGG, 1);
						else
							result = new ItemStack(Material.TNT, 1);
					if(choice==2)
						if(new Random().nextInt(2)!=0)
							result = new ItemStack(Material.FEATHER, 1);
						else
							result = new ItemStack(Material.BOOKSHELF, 1);
					if(choice==3)
						result = new ItemStack(Material.FEATHER, 1);
					if(choice==4)
						if(new Random().nextInt(2)==0)
							result = new ItemStack(Material.HEART_OF_THE_SEA, 1);
						else
							result = new ItemStack(Material.FEATHER, 1);
					
					p.getWorld().dropItem(e.getClickedBlock().getLocation().add(0, 1, 0), result);
					
					blockDelays.put(e.getClickedBlock(), 15);
				}
			
			}
		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		Player p = e.getPlayer();
		
		if(e.getBlock().getLocation().getBlockZ()<10&&e.getBlock().getLocation().getBlockZ()>-10)
			e.setCancelled(true);
		if(e.getBlock().getLocation().getBlockZ()>0) 
			if(e.getBlock().getLocation().getBlockZ()>Settings.maxBuildDepth)
				e.setCancelled(true);
		else
			if(e.getBlock().getLocation().getBlockZ()<-Settings.maxBuildDepth)
				e.setCancelled(true);
		
		
		if(e.getBlock().getLocation().getBlockX()>Settings.maxBuildWidth||e.getBlock().getLocation().getBlockX()<-Settings.maxBuildWidth)
			e.setCancelled(true);
		
		if(e.getBlock().getLocation().getBlockY()>Settings.maxBuildHeight) {
			e.setCancelled(true);
			p.sendMessage("Hör auf Skybases zu Bauen, du Scheißkind!");
		}
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Entity projectile = e.getEntity();
		World world = e.getEntity().getWorld();
		if(!world.getName().startsWith("InGameWorld")) return;
		Location loc = projectile.getLocation();
		
			if(projectile.getType().equals(EntityType.TRIDENT)) {
				e.getEntity().getLocation().getWorld().createExplosion(e.getEntity().getLocation(), 3, true);
				
				for(Player cp : Bukkit.getOnlinePlayers())
					cp.playSound(projectile.getLocation(), Sound.BLOCK_ANVIL_LAND, 15, 1);
					double radius = 0.1;
					  int n = 8;
					  for (int i = 0; i < 6; i++) {
					    double angle = 2 * Math.PI * i / n;
					    Location base =
					        loc.clone().add(new Vector(radius * Math.cos(angle), 0, radius * Math.sin(angle)));
					    for (int j = 0; j <= 8; j++) {
					      world.playEffect(base, Effect.SMOKE, j);
					      world.playEffect(base, Effect.MOBSPAWNER_FLAMES, j);
					    }
					  }
			}
			
			if(projectile.getType().equals(EntityType.SNOWBALL))
				projectile.getWorld().strikeLightning(projectile.getLocation());
			
			if(projectile.getType().equals(EntityType.EGG))
				if(e.getHitBlock()!=null)
					e.getHitBlock().setType(Material.FIRE);
			
			if(projectile.getType().equals(EntityType.FIREBALL))
				if(e.getHitBlock()!=null){
					Blaze blaze = (Blaze)e.getHitBlock().getWorld().spawnEntity(e.getHitBlock().getLocation(), EntityType.BLAZE);
					blaze.setGlowing(true);
					blaze.setHealth(1);
				}
			
			if(projectile.getType().equals(EntityType.ARROW)) {
				if(e.getHitBlock()!=null) {
					if(e.getHitBlock().getType()==Material.CAULDRON) {
						e.getHitBlock().setType(Material.LAVA);
						e.getHitBlock().getWorld().createExplosion(projectile.getLocation(), 10, true);
					}
						
					if(new Random().nextInt(5)==0)
						e.getHitBlock().setType(Material.FIRE);
					else if(new Random().nextInt(2)==0)
						e.getHitBlock().setType(Material.AIR);
				}	
			}
			e.getEntity().remove();
	}
	
}

