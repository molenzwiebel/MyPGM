package nl.thijsmolendijk.MyPGM.Commands;

import java.io.File;
import java.io.IOException;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.MapData;
import nl.thijsmolendijk.MyPGM.Tools;
import nl.thijsmolendijk.MyPGM.XMLHandler;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LoadMapCommand implements CommandExecutor {
	/**
	 * LoadMapCommand class, used for processing /lm <map>
	 * Args: Main plugin -> The plugin that initiates this class
	 */
	public Main plugin;
	public LoadMapCommand(Main instance) {
		this.plugin = instance;
	}
	//Command Handler, Setting the team is done by seperate commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!label.equalsIgnoreCase("lm")) return true;
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		//Load a map, reset the scores
		this.plugin.scoreOne = 0;
		this.plugin.scoreTwo = 0;
		this.plugin.timerHandler.timeLeft = 0;
		System.out.println("LM Command triggered");
		//Return if missing arguments
		if (args.length > 1 || args.length == 0) return false;
		
		if (p.getWorld().getName().contains("_COPY")) {
			//Currently in a loaded world, send them back and delete this world
			String oldWorldName = p.getWorld().getName();
			World mainWorld = this.plugin.getServer().getWorld("world");
			for (Player pToTP : this.plugin.getServer().getOnlinePlayers()) {
				
				pToTP.teleport(mainWorld.getSpawnLocation());
			}
			p.sendMessage(ChatColor.GREEN+"Unloading world");
			this.plugin.getServer().unloadWorld(oldWorldName, false);
			p.sendMessage(ChatColor.GREEN+"Deleting _COPY world files");
			try {
				Tools.deleteFolder(new File(oldWorldName));
			} catch (IOException e) {
				p.sendMessage(ChatColor.RED+"Something went wrong during deleting: "+e.getMessage());
			}
		}
		//Check if the map exists and is valid
		File path = new File("Maps/",args[0]);
		p.sendMessage(ChatColor.GOLD+"Trying to find map at: "+path.getAbsolutePath());
		if (!(path.exists())) {
			p.sendMessage(ChatColor.RED+"The specified file does not exist");
			return true;
		}
		p.sendMessage(ChatColor.GREEN+"Found file at: "+path.getAbsolutePath());
		if (!(path.isDirectory())) {
			p.sendMessage(ChatColor.RED+"The specified world is not a directory");
			return true;
		}
		if (!(new File(path,"level.dat").exists())) {
			p.sendMessage(ChatColor.RED+"No level.dat file found inside world folder");
			return true;
		}
		//Correct world, try to copy the world to the main folder
		p.sendMessage(ChatColor.GREEN+"Found correct world, copying it to: "+new File(args[0]+"_COPY").getAbsolutePath());
		try {
			Tools.copyFolder(path, new File(args[0]+"_COPY"));
		} catch (IOException e) {
			p.sendMessage(ChatColor.RED+"Something went wrong during copying: "+e.getMessage());
		}
		//Copying succeeded, load the world and send all players to the new world
		p.sendMessage(ChatColor.GREEN+"Copying succeeded, loading world");
		World newWorld = this.plugin.getServer().createWorld(new WorldCreator(args[0]+"_COPY"));
		MapData data = new MapData("", "", "", 0);
		try {
		data = XMLHandler.getMapData(args[0], newWorld);
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED+"Your map XML is incorrect! Error: "+ e.getMessage());
			return true;
		}
		System.out.println("Map name: "+data.name+", author: "+data.author);
		for (Player pToTP : this.plugin.getServer().getOnlinePlayers()) {
			pToTP.sendMessage(ChatColor.GOLD+"Cycling to: "+ChatColor.LIGHT_PURPLE+data.name);
			pToTP.sendMessage(ChatColor.GOLD+"Map by: "+ChatColor.LIGHT_PURPLE+data.author);
			this.plugin.currentMap = data;
			pToTP.teleport(newWorld.getSpawnLocation());
		}
		for (Player p2 : this.plugin.getServer().getOnlinePlayers()) {
			p2.setGameMode(GameMode.CREATIVE);
			this.plugin.teamOne.remove(p2.getName());
			this.plugin.teamTwo.remove(p2.getName());
			this.plugin.spectators.remove(p2.getName());
			this.plugin.spectators.add(p2.getName());
			for (ItemStack i : p.getInventory().getArmorContents()) {
				i.setType(Material.AIR);
			}
			p.setFoodLevel(20);
			p.getInventory().clear();
			p.setItemInHand(new ItemStack(Material.COMPASS, 1));
			p2.setDisplayName(ChatColor.AQUA+p2.getName()+ChatColor.RESET);
		}
		//Command succeeded, reset all the timers
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDGame);
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDStart);
		this.plugin.getServer().getScheduler().cancelTask(this.plugin.timerHandler.countdownIDMatchLength);
		if (this.plugin.currentMap.gameType.equalsIgnoreCase("tdm")) {
			this.plugin.timerHandler.timeLeft = this.plugin.currentMap.matchLenght;
		}
		this.plugin.timerHandler.timeLeft = 0;
		//TODO: Eventually auto-start the game
		return true;
	}
}