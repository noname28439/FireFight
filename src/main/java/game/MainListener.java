package game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import main.Main;

public class MainListener implements Listener {
	
	public static HashMap<Player, Location> playerBuildStartPoints = new HashMap<>();
	public static HashMap<Player, Location> playerBuildEndPoints = new HashMap<>();
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		//Force AutoRespawn
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() { @Override public void run() {
			e.getEntity().spigot().respawn();
		}}, 1*20);
	}
	@EventHandler
	public void OnPlayerDamageHanging(HangingBreakByEntityEvent  e) {
			if(e.getEntity() instanceof Painting)
				e.setCancelled(true);
			
			if(e.getEntity() instanceof ItemFrame)
				e.setCancelled(true);
		
			if (e.getRemover() instanceof Player) {
				Player remover = (Player) e.getRemover();
	            if(remover.getGameMode()==GameMode.CREATIVE)
	            	e.setCancelled(false);
	        }
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		
//		if(p.getInventory().getItemInMainHand()!=null) {
//			if(!playerBuildStartPoints.containsKey(p))
//				playerBuildStartPoints.put(p, null);
//			if(!playerBuildEndPoints.containsKey(p))
//				playerBuildEndPoints.put(p, null);
//			
//			ItemStack inHand = p.getInventory().getItemInMainHand();
//			
//			if(inHand.getType()==Material.NETHER_STAR) {
//				e.setCancelled(true);
//				if(e.getAction()==Action.RIGHT_CLICK_AIR)
//					playerBuildStartPoints.put(p, p.getLocation());
//				else if(e.getAction()==Action.RIGHT_CLICK_BLOCK)
//					playerBuildStartPoints.put(p, e.getClickedBlock().getLocation());
//				
//				if(e.getAction()==Action.LEFT_CLICK_AIR)
//					playerBuildEndPoints.put(p, p.getLocation());
//				else if(e.getAction()==Action.LEFT_CLICK_BLOCK)
//					playerBuildEndPoints.put(p, e.getClickedBlock().getLocation());
//			
//				if(playerBuildEndPoints.get(p)!=null)
//					p.getWorld().spawnParticle(Particle.HEART, playerBuildEndPoints.get(p), 0);
//				if(playerBuildStartPoints.get(p)!=null)
//					p.getWorld().spawnParticle(Particle.HEART, playerBuildStartPoints.get(p), 0);
//				
//				
//					if(playerBuildStartPoints.get(p)!=null)
//						if(playerBuildStartPoints.get(p).getY()>Settings.maxBuildHeight) {
//							Location newPos = playerBuildStartPoints.get(p);
//							newPos.setY(Settings.maxBuildHeight);
//							playerBuildStartPoints.put(p, newPos);
//						}
//					if(playerBuildEndPoints.get(p)!=null)
//						if(playerBuildEndPoints.get(p).getY()>Settings.maxBuildHeight) {
//							Location newPos = playerBuildEndPoints.get(p);
//							newPos.setY(Settings.maxBuildHeight);
//							playerBuildEndPoints.put(p, newPos);
//						}
//					InGameBuildTools.markArea(p, MainListener.playerBuildStartPoints.get(p), MainListener.playerBuildEndPoints.get(p), 3);
//			}
//			
//		}
	}
	
}
