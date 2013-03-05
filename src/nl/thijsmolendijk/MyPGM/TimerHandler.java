package nl.thijsmolendijk.MyPGM;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class TimerHandler {
	public Main plugin;
	public int countdownIDGame;
	public int countdownIDStart;
	public int matchLenght;
	public int timeLeft;
	public int countdownIDMatchLength;
	public int timeForceID;
	public int timeToForce;
	public TimerHandler(Main instance) {
		this.plugin = instance;
		timeLeft = 0;
	}
	//Start the countdown timer needed to TDM
	public void startGameTimer(final int seconds, final Main plugin) {
		this.matchLenght = seconds;
		countdownIDGame = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			int count = matchLenght + 1;
			
			public void run() {
		 
				//note : I changed the timer to 20L so the value of count is in seconds
				
				count--;
				
				plugin.setTimeLeft(count);
				if(count == 60){
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA+"1 "+ChatColor.GOLD+"minute until game ends!");
				}
		 
				if(count == 30 || count == 20 || count < 11){
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA+""+count+ChatColor.GOLD+ " seconds until game ends!");
				}
		 
				if(count == 0){
					System.out.println(ChatColor.GREEN+"Starting game!");
					plugin.endGameTDM();
					Bukkit.getScheduler().cancelTask(countdownIDGame);
				}
		 
			}
		}, 0L, 20L);
	}
	//Start the game start countdown timer
	public void startStartTimer(final Main pluginInstance) {
		
		countdownIDStart = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			int count = 20 + 1;
			
			public void run() {
		 
				//note : I changed the timer to 20L so the value of count is in seconds
				
				count--;
		 
				if(count == 60){
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA+"1 "+ChatColor.GOLD+" minute until game ends!");
				}
		 
				if(count == 30 || count == 20 || count < 11){
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA+""+count+ChatColor.GOLD+ " seconds until game starts!");
				}
		 
				if(count == 0){
					System.out.println(ChatColor.GREEN+"Starting game!");
					pluginInstance.startGame();
					Bukkit.getScheduler().cancelTask(countdownIDStart);
				}
		 
			}

			
		}, 0L, 20L);
	}
	//Start the timer to track the game lenght, gametype == DTC
	public void startMatchLenghtTimer() {
		countdownIDMatchLength = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			int count = 1;
			
			public void run() {
		 
				//note : I changed the timer to 20L so the value of count is in seconds
				
				count++;
				
				plugin.setTimeLeft(count);
		 
			}
		}, 0L, 20L);
	}
	//Force a specific time of the day
	public void forceTime(int time) {
		this.timeToForce = time;
		timeForceID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
			int forcedTime = timeToForce;
			public void run() {
				plugin.currentMap.world.setTime((long)forcedTime);
			}
		}, 0L, 20L);
	}
}
