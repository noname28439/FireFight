package game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LobbyState extends GameState{

	@Override
	public void handleJoin(Player joined) {
		//teleport to spawn
		
		
	}

	@Override
	public void handleLeave(Player leaved) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent e) {
		e.setKeepInventory(true);
	}

}
