package nl.thijsmolendijk.MyPGM.Commands;

import nl.thijsmolendijk.MyPGM.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartAndEndCommands implements CommandExecutor {
	/**
	 * StartAndEndCommands class, used for processing /start, /forcestart and /forceend
	 * Args: Main plugin -> The plugin that initiates this class
	 */
	public Main plugin;
	public StartAndEndCommands(Main instance) {
		this.plugin = instance;
	}
	//Command Handler, Setting the team is done by seperate commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		//Match status handler
		if (label.equalsIgnoreCase("forcestart")) return this.forceStartCommand(p);
		//Map info handler
		if (label.equalsIgnoreCase("forceend")) return this.forceEndCommand(p);
		//Normal start
		if (label.equalsIgnoreCase("start")) return this.startCommand(p);
		return true;
	}
	public boolean forceStartCommand(Player p) {
		if (this.plugin.currentMap.inProgress) return false;
		this.plugin.startGame();
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDStart);
		return true;
	}
	public boolean forceEndCommand(Player p) {
		if (!this.plugin.currentMap.inProgress) return false;
		this.plugin.endGameWithWinner(null, false);
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDGame);
		return true;
	}
	public boolean startCommand(Player p) {
		//Start the match countdown, return if the match is already running
		if (this.plugin.currentMap.inProgress) return false;
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDGame);
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDStart);
		this.plugin.timerHandler.startStartTimer(this.plugin);
		return true;
	}
}
