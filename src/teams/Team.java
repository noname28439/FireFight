package teams;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum Team {

	
	RED("Rot", Material.RED_WOOL, ChatColor.RED),
	BLUE("Blau", Material.BLUE_WOOL, ChatColor.BLUE);
	
	private String teamName;
	private ChatColor chatColor;
	private Material button;
	private Location respawnPoint;
	
	private ArrayList<Player> teamPlayers;
	
	
	private Team(String teamName, Material button, ChatColor chatColor) {
		this.teamName = teamName;
		this.button = button;
		this.chatColor = chatColor;
		this.respawnPoint = new Location(Bukkit.getWorld("world"), 0, 100, 0);
		
		teamPlayers=new ArrayList<>();
	}
	
	public ArrayList<Player> getTeamPlayers() {
		return teamPlayers;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public ChatColor getChatColor() {
		return chatColor;
	}
	public Material getButtonMaterial() {
		return button;
	}

	public Location getRespawnPoint() {
		return respawnPoint;
	}

	public void setRespawnPoint(Location respawnPoint) {
		this.respawnPoint = respawnPoint;
	}
	
	
	
}
