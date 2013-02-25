package nl.thijsmolendijk.MyPGM.Commands;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Tools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand implements CommandExecutor {
	/**
	 * SetCommand class, used for processing /set <variable> <value>
	 * Args: Main plugin -> The plugin that initiates this class
	 */
	public Main plugin;
	public SetCommand(Main instance) {
		this.plugin = instance;
	}
	//TODO: Currently one function, add a reload xml function?
	//Command Handler, Setting the team is done by seperate commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		//Experimental function to set a variable in this.plugin.mapData
		if (args.length < 2) return false;
		String value = "";
		//Add all arguments exept the first together
		for (int i = 1; i < args.length; i++) {
			value = value + args[i] + " ";
		}
		if (value.substring(value.length()-1).equals(" ")) {
			value = value.substring(0, value.length()-1);
		}
		try {
			Tools.setValueOf(this.plugin.currentMap, args[0], value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			p.sendMessage(ChatColor.RED+"Whoops! Error while executing command: "+e.getMessage());
		}
		return true;
	}
}
