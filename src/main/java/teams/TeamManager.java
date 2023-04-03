package teams;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
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

import game.GameStateManager;
import main.Main;
import net.md_5.bungee.api.ChatColor;

public class TeamManager implements Listener{

	
	public static final int MAX_BALANCE_OFFSET = 10;
	
	public static String teamSelectionInventoryTitle = "�6�l<--Teamauswahl-->";
	
	public static Team getTeamByButtonMaterial(Material buttonMaterial) {
		for(Team t : Team.values())
			if(t.getButtonMaterial()==buttonMaterial)
				return t;
		return null;
	}
	
	//Broken
//	public static Team getPlayerTeam(Player player) {
//		for(Team ct : Team.values())
//			if(ct.getTeamPlayerList().contains(player))
//				return ct;
//		return null;
//	}
	public static Team getPlayerTeam(String playername) {
		for(Team ct : Team.values())
			if(ct.teamPlayers.contains(playername))
				return ct;
		return null;
	}
	
	public static void setPlayerTeam(String playername, Team team) {
		if(getPlayerTeam(playername)!=null)
			getPlayerTeam(playername).removePlayer(playername);
		
		if(team!=null)
			team.addPlayer(playername);
		
		if(Bukkit.getPlayer(playername)==null) return; //Player cant be found
		Player player = Bukkit.getPlayer(playername);
		if(team!=null) {
			player.setDisplayName(team.getChatColor()+player.getName()+ChatColor.RESET);
			player.setPlayerListName(team.getChatColor()+player.getName()+ChatColor.RESET);
			player.sendMessage("Du bist jetzt in Team "+team.getChatColor()+team.getTeamName());
		}else {
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
				TeamManager.setPlayerTeam(((Player) p).getName(), null);
			else
				TeamManager.setPlayerTeam(((Player) p).getName(), TeamManager.getTeamByButtonMaterial(e.getCurrentItem().getType()));
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onItemInteract(PlayerInteractEvent e) {
		if((e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) && GameStateManager.currentGameState == Main.LOBBY_STATE && e.getItem()!=null) 
			if(e.getItem().getType() == Material.COMPASS) {e.setCancelled(true); e.getPlayer().openInventory(getSelectionInventory());}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction()==Action.RIGHT_CLICK_AIR||e.getAction()==Action.RIGHT_CLICK_BLOCK)
			if(e.getClickedBlock() != null)
				if(e.getClickedBlock().getType() == Material.OAK_WALL_SIGN) {
					Sign sign = (Sign) e.getClickedBlock().getState();
					String[] lines = sign.getLines();
					for(String cl : lines) {
						cl = cl.toLowerCase();
						if(cl.contains("rot"))
							TeamManager.setPlayerTeam(p.getName(), Team.RED);
						if(cl.contains("blau"))
							TeamManager.setPlayerTeam(p.getName(), Team.BLUE);
					}
				}
	}
	
	
}
