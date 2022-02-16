package game;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import main.Main;

public class MainListener implements Listener {
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		//Force AutoRespawn
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				e.getEntity().spigot().respawn();
				
			}
		}, 1*20);
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
}
