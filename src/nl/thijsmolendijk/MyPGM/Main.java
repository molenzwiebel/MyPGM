package nl.thijsmolendijk.MyPGM;

import java.util.ArrayList;
import java.util.List;


import nl.thijsmolendijk.MyPGM.Commands.JoinCommand;
import nl.thijsmolendijk.MyPGM.Commands.LoadMapCommand;
import nl.thijsmolendijk.MyPGM.Commands.MatchCommand;
import nl.thijsmolendijk.MyPGM.Commands.SetCommand;
import nl.thijsmolendijk.MyPGM.Commands.StartAndEndCommands;
import nl.thijsmolendijk.MyPGM.Commands.XMLCommands;
import nl.thijsmolendijk.MyPGM.Listeners.BlockBreakAndPlaceListener;
import nl.thijsmolendijk.MyPGM.Listeners.ChatListener;
import nl.thijsmolendijk.MyPGM.Listeners.ObjectiveListener;
import nl.thijsmolendijk.MyPGM.Listeners.ObserverListener;
import nl.thijsmolendijk.MyPGM.Listeners.PlayerDamageListener;
import nl.thijsmolendijk.MyPGM.Listeners.RuleListeners;
import nl.thijsmolendijk.MyPGM.MapData.MapData;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

public class Main extends JavaPlugin {
	//Variables (teams, etc...)
	public List<String> spectators;
	public List<String> teamOne;
	public List<String> teamTwo;
	public int scoreOne;
	public int scoreTwo;
	public MapData currentMap;
	public TimerHandler timerHandler;
	//onEnable and onDisable
	public void onEnable() {
		//Enabling plugin
		timerHandler = new TimerHandler(this);
		this.getServer().getPluginManager().registerEvents(new ObserverListener(this), this);
		this.getServer().getPluginManager().registerEvents(new BlockBreakAndPlaceListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamageListener(this), this);
		this.getServer().getPluginManager().registerEvents(new RuleListeners(this), this);
		this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
		this.getServer().getPluginManager().registerEvents(new ObjectiveListener(this), this);
		this.getCommand("join").setExecutor(new JoinCommand(this));
		this.getCommand("match").setExecutor(new MatchCommand(this));
		this.getCommand("lm").setExecutor(new LoadMapCommand(this));
		this.getCommand("map").setExecutor(new MatchCommand(this));
		this.getCommand("start").setExecutor(new StartAndEndCommands(this));
		this.getCommand("xml").setExecutor(new XMLCommands(this));
		this.getCommand("forcestart").setExecutor(new StartAndEndCommands(this));
		this.getCommand("forceend").setExecutor(new StartAndEndCommands(this));
		this.getCommand("set").setExecutor(new SetCommand(this));
		//Initting variables
		spectators = new ArrayList<String>();
		teamOne = new ArrayList<String>();
		teamTwo = new ArrayList<String>();
		currentMap = new MapData("","","",0);
		scoreOne = 1;
	}
	
	public void onDisable() {
		//Disabeling plugin
	}
	//Start the game, handle teleporting to spawn and adding inventory items
	public void startGame() {
		this.currentMap.inProgress = true;
		for (String pName : this.teamOne) {
			this.spectators.remove(pName);
			Player p = this.getServer().getPlayer(pName);
			p.teleport(this.currentMap.redSpawn);
			p.getInventory().clear();
			for (ItemStack i : p.getInventory().getArmorContents()) {
				i.setType(Material.AIR);
			}
			p.setFoodLevel(20);
			p.setHealth(20);
			for (PotionEffect effect : p.getActivePotionEffects()) {
				p.removePotionEffect(effect.getType());
			}
			Tools.showPlayer(p);
			Tools.addItemsToPlayerInv(p, this.currentMap.redInv);
			
		}
		for (String pName : this.teamTwo) {
			this.spectators.remove(pName);
			Player p = this.getServer().getPlayer(pName);
			p.teleport(this.currentMap.blueSpawn);
			p.getInventory().clear();
			for (ItemStack i : p.getInventory().getArmorContents()) {
				i.setType(Material.AIR);
			}
			p.setFoodLevel(20);
			p.setHealth(20);
			for (PotionEffect effect : p.getActivePotionEffects()) {
				p.removePotionEffect(effect.getType());
			}
			Tools.showPlayer(p);
			Tools.addItemsToPlayerInv(p, this.currentMap.blueInv);
			
		}
		this.getServer().broadcastMessage(Tools.startMessage());
		if (this.currentMap.gameType.equalsIgnoreCase("tdm")) {
			this.timerHandler.startGameTimer(this.currentMap.matchLenght, this);
		} else {
			this.timerHandler.startMatchLenghtTimer();
		}
		if (this.currentMap.forceTime) {
			this.timerHandler.forceTime(this.currentMap.timeToForce);
		}
	}
	//End the game, show a message based on the score
	public void endGame() {
		if (!this.currentMap.inProgress) return;
		this.getServer().getScheduler().cancelTask(this.timerHandler.countdownIDMatchLength);
		this.getServer().getScheduler().cancelTask(this.timerHandler.countdownIDGame);
		this.getServer().getScheduler().cancelTask(this.timerHandler.timeForceID);
		for (Player p : this.getServer().getOnlinePlayers()) {
			p.setGameMode(GameMode.CREATIVE);
			this.spectators.remove(p.getName());
			this.spectators.add(p.getName());
			Tools.showPlayerNC(p);
		}
		this.currentMap.inProgress = false;
		if (scoreOne < scoreTwo) {
			this.getServer().broadcastMessage(Tools.redWonMessage(this));
			return;
		}
		if (scoreTwo < scoreOne) {
			this.getServer().broadcastMessage(Tools.blueWonMessage(this));
			return;
		}
		if (scoreOne == scoreTwo) {
			this.getServer().broadcastMessage(Tools.tieMessage(this));
			return;
		}
	}
	//Dummy method to set non-final variables from a timer
	public void setTimeLeft(int time) {
		this.timerHandler.timeLeft = time;
	}
}
