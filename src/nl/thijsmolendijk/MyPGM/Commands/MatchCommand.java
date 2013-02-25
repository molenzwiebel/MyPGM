package nl.thijsmolendijk.MyPGM.Commands;

import nl.thijsmolendijk.MyPGM.*;

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
			System.out.println(Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft));
			String lineOne = "##################";
			String lineTwo = "#       !-@   $$:%%       #";
			String line333 = "#   Red players: ^|*     #";
			String line444 = "#   Blue players: &|)    #";
			String line555 = "##################";
			lineOne = ChatColor.LIGHT_PURPLE+lineOne;
			lineTwo = ChatColor.LIGHT_PURPLE+lineTwo;
			line333 = ChatColor.LIGHT_PURPLE+line333;
			line444 = ChatColor.LIGHT_PURPLE+line444;
			line555 = ChatColor.LIGHT_PURPLE+line555;
			lineTwo = lineTwo.replace("!", ChatColor.RED+""+this.plugin.scoreTwo+""+ChatColor.RESET);
			lineTwo = lineTwo.replace("-", ChatColor.DARK_GRAY+"-"+ChatColor.RESET);
			lineTwo = lineTwo.replace("@", ChatColor.BLUE+""+this.plugin.scoreOne+""+ChatColor.LIGHT_PURPLE);
			lineTwo = lineTwo.replace("$$",ChatColor.AQUA+Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[1]+ChatColor.DARK_GRAY);
			lineTwo = lineTwo.replace("%%",ChatColor.AQUA+Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[2]+ChatColor.LIGHT_PURPLE);
			lineTwo = lineTwo.replace("*", String.valueOf(this.plugin.currentMap.teamOne.maxSize));
			line333 = line333.replace("Red players", this.plugin.currentMap.teamOne.preColor+this.plugin.currentMap.teamOne.name);
			line333 = line333.replace("^", this.plugin.teamOne.size()+""+ChatColor.LIGHT_PURPLE);
			line333 = line333.replace(")", String.valueOf(this.plugin.currentMap.teamTwo.maxSize));
			line444 = line444.replace("Blue players", this.plugin.currentMap.teamTwo.preColor+this.plugin.currentMap.teamTwo.name);
			line444 = line444.replace("&", this.plugin.teamTwo.size()+""+ChatColor.LIGHT_PURPLE);
			p.sendMessage(lineOne);
			p.sendMessage(lineTwo);
			p.sendMessage(line333);
			p.sendMessage(line444);
			p.sendMessage(line555);
		} else {
			//GameType == DTC
			String rs = ChatColor.AQUA+"OK";
			String bs = ChatColor.AQUA+"OK";
			if (this.plugin.scoreOne == 1) bs = ChatColor.YELLOW +"XX";
			if (this.plugin.scoreTwo == 1) rs = ChatColor.YELLOW +"XX";
			p.sendMessage(ChatColor.LIGHT_PURPLE+"####################");
			p.sendMessage(ChatColor.LIGHT_PURPLE+"#    "+ChatColor.AQUA+Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[1]+":"+Tools.formatIntoHHMMSS(this.plugin.timerHandler.timeLeft).split(":")[2]+ChatColor.LIGHT_PURPLE+"                        #");
			p.sendMessage(ChatColor.LIGHT_PURPLE+"# "+ChatColor.RED+"Red core: "+bs+ChatColor.LIGHT_PURPLE+"               #");
			p.sendMessage(ChatColor.LIGHT_PURPLE+"# "+ChatColor.BLUE+"Blue core: "+rs+ChatColor.LIGHT_PURPLE+"              #");
			p.sendMessage(ChatColor.LIGHT_PURPLE+"# "+ this.plugin.currentMap.teamOne.preColor+this.plugin.currentMap.teamOne.name+": "+ChatColor.AQUA+this.plugin.teamOne.size()+"/"+this.plugin.currentMap.teamOne.maxSize+ChatColor.LIGHT_PURPLE+"            #");
			p.sendMessage(ChatColor.LIGHT_PURPLE+"# "+ this.plugin.currentMap.teamTwo.preColor+this.plugin.currentMap.teamTwo.name+": "+ChatColor.AQUA+this.plugin.teamTwo.size()+"/"+this.plugin.currentMap.teamTwo.maxSize+ChatColor.LIGHT_PURPLE+"            #");
			p.sendMessage(ChatColor.LIGHT_PURPLE+"####################");
		}
		return true;
	}
}