package game;

import java.sql.Timestamp;
import java.util.ArrayList;
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
	
	
	public static void dropHealItem(Location dropLoc) {
		Random random = new Random();
		ItemStack[] lootTable = new ItemStack[] {
			new ItemStack(Material.GOLDEN_APPLE, random.nextInt(5)+3),
			new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
			new ItemStack(Material.GOLDEN_CHESTPLATE, 1),
			new ItemStack(Material.GOLDEN_BOOTS, 1),
			new ItemStack(Material.TURTLE_HELMET, 1),
			new ItemStack(Material.MUSHROOM_STEW, random.nextInt(3)+2),
			new ItemStack(Material.TOTEM_OF_UNDYING, random.nextInt(2)),
		};
		ItemStack result = lootTable[new Random().nextInt(lootTable.length)];
		dropLoc.getWorld().dropItem(dropLoc.clone().add(0.5, 1, 0.5), result);
	}
	
	
	public static void dropBuildItem(Location dropLoc) {

		Random random = new Random();
		
		ItemStack[] lootTable = new ItemStack[] {
				new ItemStack(Material.PINK_WOOL, random.nextInt(6)+5),new ItemStack(Material.PINK_WOOL, random.nextInt(6)+5),new ItemStack(Material.PINK_WOOL, random.nextInt(6)+5),
				new ItemStack(Material.ENDER_PEARL, 1),new ItemStack(Material.ENDER_PEARL, 1),
				new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
				new ItemStack(Material.BLAZE_ROD, 1),
				new ItemStack(Material.SHIELD, 1),
				new ItemStack(Material.OBSIDIAN, 1),
				new ItemStack(Material.GRAY_WOOL, random.nextInt(20)+10),
				new ItemStack(Material.DRAGON_HEAD, new Random().nextInt(2)),
				new ItemStack(Material.DIAMOND, random.nextInt(3)+1),
				new ItemStack(Material.IRON_INGOT, random.nextInt(5)+2),
				new ItemStack(Material.CAULDRON, 1),
				new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(5)+2),new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(6)+2),new ItemStack(Material.GOLDEN_APPLE, new Random().nextInt(7)+2),
				new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),new ItemStack(Material.COOKED_BEEF, random.nextInt(3)+2),
				new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),new ItemStack(Material.COOKED_CHICKEN, random.nextInt(5)+4),
				new ItemStack(Material.FIRE_CHARGE, 1),
				new ItemStack(Material.FIRE_CHARGE, random.nextInt(2)+1),
				new ItemStack(Material.HONEYCOMB, 1),new ItemStack(Material.HONEYCOMB, 1),
				new ItemStack(Material.BIRCH_LOG, random.nextInt(5)+3),new ItemStack(Material.BIRCH_LOG, random.nextInt(5)+3),
				new ItemStack(Material.NAUTILUS_SHELL, 1),new ItemStack(Material.NAUTILUS_SHELL, 1),new ItemStack(Material.NAUTILUS_SHELL, 1),new ItemStack(Material.NAUTILUS_SHELL, 1),
				new ItemStack(Material.BEETROOT_SOUP, 1),new ItemStack(Material.BEETROOT_SOUP, 1),
				new ItemStack(Material.MILK_BUCKET, 3),
				new ItemStack(Material.MAGENTA_WOOL, new Random().nextInt(1)+1),
				new ItemStack(Material.LIME_WOOL, new Random().nextInt(1)+1),
				};
		
		
		ItemStack result = lootTable[new Random().nextInt(lootTable.length)];
		
		dropLoc.getWorld().dropItem(dropLoc.clone().add(0.5, 1, 0.5), result);
		
	}
	
	World gameworld;
	int FightTickerID,RepairTickerID;
	int playerStartHP;
	public HashMap<Block, Integer> blockDelays;
	public ArrayList<Block> toSelfRepairBlocks;
	public HashMap<String, Integer> playerLives;
	
	@Override
	public void start() {
		blockDelays = new HashMap<>();
		toSelfRepairBlocks = new ArrayList<>();
		playerLives = new HashMap<>();
		
		/*
		 //KrawattenFreak
		 BaseBlueprint.6a4c3e2d-8449-4e04-83ab-02400cc281ed.1.0	
		 
		 //LeeDo
		 BaseBlueprint.43b6b049-77f7-4796-899b-67dc84fdcd18.5.0	
		 BaseBlueprint.43b6b049-77f7-4796-899b-67dc84fdcd18.5.2
		 
		 */
		
		String base1name = Main.base1worldname;
		String base2name = Main.base2worldname;
		
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
					if(cb.getType()==Material.AIR || cb.getType()==Material.FIRE) {
						cb.setType(Settings.selfRepairBlockMaterial);
						for(Player cp : Main.getAllPlayers())
							cp.playSound(cb.getLocation(), Sound.BLOCK_HONEY_BLOCK_STEP, 0.5f, 1);
					}
						
				
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
		if(playerLives.containsKey(joined.getName()))
		if(playerLives.get(joined.getName())<=0) {
			joined.setGameMode(GameMode.SPECTATOR);
			joined.teleport(new Location(gameworld, 0, 100, 0));
		}else
			setupPlayer(joined);
	}
	public void setupPlayer(Player toSetup) {
		if(TeamManager.getPlayerTeam(toSetup.getName())==null) { 			//no team --> Spectator
			toSetup.setGameMode(GameMode.SPECTATOR);
			toSetup.teleport(new Location(gameworld, 0, 100, 0));
		}else { 												//in team team --> Spectator
			Team playerTeam = TeamManager.getPlayerTeam(toSetup.getName());
			toSetup.teleport(calculatePlayerRespawnPoint(toSetup));
			toSetup.setGameMode(GameMode.SURVIVAL);
			toSetup.setHealth(20);
			
			ItemStack bow = new ItemStack(Material.BOW, 1);
			ItemMeta bowMeta = bow.getItemMeta();
			bowMeta.addEnchant(Enchantment.ARROW_INFINITE,1, true);
			bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK,3, true);
			bow.setItemMeta(bowMeta);
			toSetup.getInventory().addItem(bow);
			toSetup.getInventory().addItem(new ItemStack(Material.ARROW, 5));
			toSetup.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 10));
			toSetup.getInventory().addItem(new ItemStack(Material.GRAY_WOOL, 32));
			toSetup.getInventory().addItem(new ItemStack(Material.FEATHER, 2));
			toSetup.getInventory().addItem(new ItemStack(Material.SHEARS, 1));
			
			//toSetup.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 99999, 2));
		}
	}

	@Override
	public void handleLeave(Player left) {
		checkTeamLoss();
		
	}
	
	public void checkTeamLoss() {		
		
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
	
	public Team getRandomEnemyTeam(Team own) {int cntr = 0;
		Team targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
		while(targetTeam==own&&cntr++<500)
			targetTeam = Team.values()[new Random().nextInt(Team.values().length)];
		return targetTeam;
	}
	
	public Player getRandomAlivePlayer(Team targetTeam) {int cntr = 0;
		Player targetPlayer = targetTeam.getTeamPlayerList().get(new Random().nextInt(targetTeam.getTeamPlayerList().size()));
		while(getPlayerLeftHP(targetPlayer.getName())<=0&&cntr++<500)
			targetPlayer = targetTeam.getTeamPlayerList().get(new Random().nextInt(targetTeam.getTeamPlayerList().size()));
		return targetPlayer;
	}
	public ArrayList<Player> getTeamAlivePlayers(Team targetTeam) {
		ArrayList<Player> players = new ArrayList<>();
		for(Player cp : targetTeam.getTeamPlayerList())
			if(getPlayerLeftHP(cp.getName())>0)
				players.add(cp);
		return players;
	}
	public int getPlayerLeftHP(String playername) {
		if(playerLives.containsKey(playername))
			return playerLives.get(playername);
		return Settings.playerHP;
	}
	
	
	@EventHandler 
	public void onPlayerRespawn(PlayerRespawnEvent e) {
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
			Bukkit.broadcastMessage(ChatColor.DARK_RED+p.getName()+" ist ausgeschieden");
			for(Player cp : Main.getAllPlayers())
				cp.playSound(cp.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.5f, 1);
			p.setGameMode(GameMode.SPECTATOR);
			checkTeamLoss();
		}else {
			//p.getWorld().getBlockAt(e.getRespawnLocation().clone().add(0, -5, 0)).setType(Material.PINK_WOOL);
		}
		
		
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		Player p = e.getPlayer();
		
			if(e.getBlock().getType()==Material.TNT)
				e.setDropItems(true);
		
		if(e.getBlock().getType()==Settings.selfRepairBlockMaterial)
			if(toSelfRepairBlocks.contains(e.getBlock()))
				toSelfRepairBlocks.remove(e.getBlock());
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(Main.playerBuilding(e.getPlayer())) return;
		if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
		Player p = e.getPlayer();
		
		
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType()==Material.TARGET) {
				if(!blockDelays.containsKey(e.getClickedBlock())) {
					p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 0.3f, 1);
					dropHealItem(e.getClickedBlock().getLocation().add(0, 1, 0));
					blockDelays.put(e.getClickedBlock(), 30);
				}
			}
			else if(e.getClickedBlock().getType()==Material.BOOKSHELF) {
				if(!blockDelays.containsKey(e.getClickedBlock())) {
					p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 0.3f, 1);
					dropBuildItem(e.getClickedBlock().getLocation().add(0, 1, 0));
					blockDelays.put(e.getClickedBlock(), 10);
				}
			}
			else if(e.getClickedBlock().getType()==Material.TNT) {
				if(!blockDelays.containsKey(e.getClickedBlock())) {
					p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 0.3f, 1);
					int choice = new Random().nextInt(5);
					
					ItemStack result = new ItemStack(Material.CAKE, 1);
					
					if(choice==0)
						if(new Random().nextInt(4)==0)
							result = new ItemStack(Material.TRIDENT, 1);
						else
							result = new ItemStack(Material.SNOWBALL, 1);
					if(choice==1)
						if(new Random().nextInt(5)!=0)
							result = new ItemStack(Material.EGG, new Random().nextInt(2)+1);
						else
							result = new ItemStack(Material.TNT, 1);
					if(choice==2)
						if(new Random().nextInt(2)!=0)
							result = new ItemStack(Material.FEATHER, new Random().nextInt(2)+1);
						else
							result = new ItemStack(Material.BOOKSHELF, 1);
					if(choice==3)
						result = new ItemStack(Material.FEATHER, 1);
					if(choice==4)
						if(new Random().nextInt(2)==0)
							result = new ItemStack(Material.HEART_OF_THE_SEA, 1);
						else
							result = new ItemStack(Material.FEATHER, 1);
					
					p.getWorld().dropItem(e.getClickedBlock().getLocation().add(0.5, 1, 0.5), result);
					
					blockDelays.put(e.getClickedBlock(), 15);
				}
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
				for(Player cp : Main.getAllPlayers())
					cp.playSound(p.getLocation(), Sound.ITEM_BUCKET_EMPTY, 0.7f, 1);
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
			if(e.getPlayer().getItemInHand().getType()==Material.MUSHROOM_STEW) {
				if(p.getHealth()+2<p.getMaxHealth()) {
					p.setHealth(p.getHealth()+2);
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_BURP, 0.3f, 1);
					e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
				}
			}
			if(e.getPlayer().getItemInHand().getType()==Material.HEART_OF_THE_SEA) {
				Location toBomb = getRandomAlivePlayer(getRandomEnemyTeam(TeamManager.getPlayerTeam(p.getName()))).getLocation();
				for(int i = 0; i<20;i++) {
					Location in = toBomb.clone().add(new Random().nextInt(10)-5,Settings.maxBuildHeight+30, new Random().nextInt(10)-5);
					toBomb.getWorld().spawnEntity(in, EntityType.SNOWBALL);
				}
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.HONEYCOMB) {
				for(Player cp: getTeamAlivePlayers(getRandomEnemyTeam(TeamManager.getPlayerTeam(p.getName()))))
					cp.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30*20, 1));
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.BEETROOT_SOUP) {
				getRandomAlivePlayer(getRandomEnemyTeam(TeamManager.getPlayerTeam(p.getName()))).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 15*20, 1));
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
						Block currentBlock = p.getWorld().getBlockAt(p.getLocation().add(x, 0, z).add(-1, -3, -1));
						currentBlock.setType(Material.PINK_WOOL);
						toSelfRepairBlocks.add(currentBlock);
					}
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
			}
			
			if(e.getPlayer().getItemInHand().getType()==Material.NAUTILUS_SHELL) {
				Player targetPlayer = getRandomAlivePlayer(getRandomEnemyTeam(TeamManager.getPlayerTeam(p.getName())));
				for(int i = 0; i<5; i++) 
					targetPlayer.getWorld().spawnEntity(targetPlayer.getLocation().clone().add(0, 10+new Random().nextInt(10), 0), EntityType.ARROW);
				e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount()-1);
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
		
		if(e.getBlock().getLocation().getBlockY()>Settings.maxBuildHeight+3) {
			e.setCancelled(true);
			p.sendMessage("Hör auf Skybases zu Bauen, du Scheißkind!");
		}
		
		if(Main.playerBuilding(e.getPlayer())) return;
		if(e.getBlock().getType()==Settings.selfRepairBlockMaterial && !e.isCancelled()) {
			toSelfRepairBlocks.add(e.getBlock());
		}

		if(e.getBlock().getType()==Material.MAGENTA_WOOL){
			e.getBlock().setType(Material.OBSIDIAN);
			for(int i = 0; i<4; i++){
				e.getBlock().getWorld().spawnFallingBlock(e.getBlock().getLocation().clone().add(0.5, 30+i*2, 0.5), Material.SAND.createBlockData());
			}
		}
		if(e.getBlock().getType()==Material.LIME_WOOL){
			e.getBlock().setType(Material.OBSIDIAN);
			for(int i = 0; i<5; i++){
				e.getBlock().getWorld().spawnFallingBlock(e.getBlock().getLocation().clone().add(0.5, 30+i*2, 0.5), Material.GLASS.createBlockData());
			}
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
				
				for(Player cp : Main.getAllPlayers())
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
				if(e.getHitBlock()!=null) {
					e.getHitBlock().setType(Material.FIRE);
					for(Player cp : Main.getAllPlayers())
						cp.playSound(projectile.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.9f, 1);
				}
					
			
			if(projectile.getType().equals(EntityType.ENDER_PEARL))
				if(e.getHitBlock()!=null){
					Blaze blaze = (Blaze)e.getHitBlock().getWorld().spawnEntity(e.getHitBlock().getLocation(), EntityType.BLAZE);
					blaze.setGlowing(true);
					blaze.setHealth(4);
				}
			
			if(projectile.getType().equals(EntityType.ARROW)) {
				if(e.getHitBlock()!=null) {
					if(e.getHitBlock().getType()==Material.CAULDRON) {
						e.getHitBlock().setType(Material.LAVA);
						e.getHitBlock().getWorld().createExplosion(projectile.getLocation(), 10, true);
					}
						
					if(new Random().nextInt(5)==0) {
						for(Player cp : Main.getAllPlayers())
							cp.playSound(projectile.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.9f, 1);
						e.getHitBlock().setType(Material.FIRE);
					}
					else if(new Random().nextInt(2)==0) {
						e.getHitBlock().setType(Material.AIR);
						for(Player cp : Main.getAllPlayers())
							cp.playSound(projectile.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.8f, 1);
					}
					for(Player cp : Main.getAllPlayers())
						cp.playSound(projectile.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 0.5f, 1);
				}
			}
			if(projectile.getType().equals(EntityType.FIREBALL)) {
				if(e.getHitBlock()!=null) {
					
						int size = 2;
						for(int x = -size; x<size;x++) 
							for(int y = -size; y<size;y++) 
								for(int z = -size; z<size;z++) {
									Block cb = e.getHitBlock().getRelative(x, y, z);
									if(cb.getType()==Settings.selfRepairBlockMaterial)
										if(toSelfRepairBlocks.contains(cb)) {
											toSelfRepairBlocks.remove(cb);
											cb.setType(Material.ACACIA_LEAVES);
										}
								}
						
						for(Player cp : Main.getAllPlayers())
							cp.playSound(projectile.getLocation(), Sound.ENTITY_VILLAGER_HURT, 15, 1);
						
					
				}
			}
			e.getEntity().remove();
	}
	
}

