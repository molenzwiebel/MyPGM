package nl.thijsmolendijk.MyPGM.Commands;

import nl.thijsmolendijk.MyPGM.*;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MatchCommand implements CommandExecutor {
	/**
	 * MatchCommand class, used for processing /match and /map
	 * Args: Main plugin -> The plugin that initiates this class
	 */
	public Main plugin;
	public MatchCommand(Main instance) {
		this.plugin = instance;
	}
	//Command Handler, Setting the team is done by seperate commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		//Match status handler
		if (label.equalsIgnoreCase("match")) return this.handleMatchCommand(p);
		//Map info handler
		if (label.equalsIgnoreCase("map")) return this.handleMapCommand(p);
		return true;
	}
	public boolean handleMapCommand(Player p) {
		//Command for printing map data, first make sure the --- are as long as the map name
		String message = ChatColor.LIGHT_PURPLE+">>> "+ChatColor.GOLD+"name"+ChatColor.LIGHT_PURPLE+" <<< \n" +
						 ChatColor.LIGHT_PURPLE+"----";
		message = message.replace("name", this.plugin.currentMap.name);
		for (int i = 0; i <= this.plugin.currentMap.name.length(); i++) {
			message = message + "-";
		}
		//Send basic map data
		p.sendMessage(message);
		p.sendMessage(ChatColor.AQUA+"Map by: "+ChatColor.GOLD+this.plugin.currentMap.author);
		p.sendMessage(ChatColor.AQUA+"Objective: "+ChatColor.GOLD+this.plugin.currentMap.objective);
		return true;
	}
	public boolean handleMatchCommand(Player p) {
		if (this.plugin.currentMap.gameType.equalsIgnoreCase("tdm")) {
			//GameType == TDM
			p.sendMessage(ChatColor.GOLD+"--------------------"+ChatColor.GREEN+" MATCH "+ChatColor.GOLD+"--------------------");
			p.sendMessage("\n\n");
			p.sendMessage(Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[1]+":"+Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[2]+"\n");
			for (TeamData d : this.plugin.currentMap.teams.teams) {
				p.sendMessage(d.preColor+d.name+": "+ChatColor.WHITE+d.score);
			}
			p.sendMessage("\n ------- \n");
			for (TeamData d : this.plugin.currentMap.teams.teams) {
				p.sendMessage(d.preColor+d.name+": "+d.members.size()+"/"+d.maxSize);
			}
		} else {
			//GameType == DTC
			p.sendMessage(ChatColor.GOLD+"--------------------"+ChatColor.GREEN+" MATCH "+ChatColor.GOLD+"--------------------");
			p.sendMessage("\n ------- \n");
			p.sendMessage(Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[1]+":"+Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[2]+"\n");
			for (TeamData d : this.plugin.currentMap.teams.teams) {
				String status = "OK";
				if (d.score == 1) status = "XX";
				p.sendMessage(d.preColor+d.name+": "+ChatColor.WHITE+status);
			}
			p.sendMessage("\n \n");
			for (TeamData d : this.plugin.currentMap.teams.teams) {
				p.sendMessage(d.preColor+d.name+": "+d.members.size()+"/"+d.maxSize);
			}
		}
		return true;
	}
}