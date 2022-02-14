package teams;

import java.util.HashMap;

import org.bukkit.Bukkit;
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
import org.bukkit.inventory.meta.ItemMeta;

import game.GameState;
import game.GameStateManager;
import main.Main;
import net.md_5.bungee.api.ChatColor;

public class TeamManager implements Listener{

	public static HashMap<Player, Team> teamConnector = new HashMap<>();
	
	public static final int MAX_BALANCE_OFFSET = 10;
	
	public static String teamSelectionInventoryTitle = "§6§l<--Teamauswahl-->";
	
	public static Team getTeamByButtonMaterial(Material buttonMaterial) {
		for(Team t : Team.values())
			if(t.getButtonMaterial()==buttonMaterial)
				return t;
		return null;
	}
	
	public static Team getPlayerTeam(Player player) {
		return teamConnector.containsKey(player) ? teamConnector.get(player) : null;
	}
	
	public static void setPlayerTeam(Player player, Team team) {
		if(getPlayerTeam(player)!=null)
			getPlayerTeam(player).getTeamPlayers().remove(player);
		
		if(team!=null) {
			team.getTeamPlayers().add(player);
			teamConnector.put(player, team);
			player.setDisplayName(team.getChatColor()+player.getName()+ChatColor.RESET);
			player.setPlayerListName(team.getChatColor()+player.getName()+ChatColor.RESET);
			player.sendMessage("Du bist jetzt in Team "+team.getChatColor()+team.getTeamName());
		}else {
			teamConnector.remove(player);
			player.setDisplayName(ChatColor.RESET+player.getName()+ChatColor.RESET);
			player.setPlayerListName(ChatColor.RESET+player.getName()+ChatColor.RESET);
			player.sendMessage("Du bist jetzt in keinem Team mehr");
		}
		
	}
	
	public static Inventory getSelectionInventory() {
		Inventory inv = Bukkit.createInventory(null, 9*1,teamSelectionInventoryTitle);
		for(Team currentTeam : Team.values()) {
			ItemStack TeamButton = new ItemStack(currentTeam.getButtonMaterial(), 1);
			ItemMeta meta = TeamButton.getItemMeta();
			meta.setDisplayName(currentTeam.getChatColor()+"Team "+currentTeam.getTeamName());
			TeamButton.setItemMeta(meta);
			inv.addItem(TeamButton);
			
		}
		inv.setItem(8, new ItemStack(Material.BARRIER));
		return inv;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		HumanEntity p = e.getWhoClicked();
		String title = e.getView().getTitle();
		ItemStack clicked = e.getCurrentItem();
		if(clicked==null)
			return;
		if(title.equalsIgnoreCase(TeamManager.teamSelectionInventoryTitle)) {
			if(e.getCurrentItem().getType() == Material.BARRIER)
				TeamManager.setPlayerTeam((Player) p, null);
			else
				TeamManager.setPlayerTeam((Player)p, TeamManager.getTeamByButtonMaterial(e.getCurrentItem().getType()));
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onItemInteract(PlayerInteractEvent e) {
		if((e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) && GameStateManager.currentGameState == Main.LOBBY_STATE && e.getItem()!=null) 
			if(e.getItem().getType() == Material.COMPASS) e.getPlayer().openInventory(getSelectionInventory());
	}
	
	
}
