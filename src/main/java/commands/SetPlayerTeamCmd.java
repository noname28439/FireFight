package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teams.Team;
import teams.TeamManager;

public class SetPlayerTeamCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) return false;

        TeamManager.setPlayerTeam(target.getName(), Team.valueOf(args[1]));

        return true;
    }
}
