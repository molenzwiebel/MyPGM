package nl.thijsmolendijk.MyPGM.MapData;

import java.util.HashMap;

import nl.thijsmolendijk.MyPGM.Cores.CoreManager;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;
import nl.thijsmolendijk.MyPGM.Teams.TeamManager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.*;
public class MapData {
	public int matchLenght;
	public String name;
	public String author;
	public String realName;
	public boolean inProgress;
	public Location redSpawn;
	public Location blueSpawn;
	public HashMap<String, ItemStack> redInv;
	public HashMap<String, ItemStack> blueInv;
	public boolean dropItemsOnDeath;
	public World world;
	public boolean spawnMonsters;
	public boolean blockBreaking;
	public int spawnProtectionRadius;
	public String objective;
	public String gameType;
	public Location redCoreLocation;
	public Location blueCoreLocation;
	public int coreRadius;
	public boolean forceTime;
	public int timeToForce;
	public TeamData teamOne;
	public TeamData teamTwo;
	public boolean changeBowProjectile;
	public String newBowEntity;
	public float newBowVelocity;
	public CoreManager cores;
	public TeamManager teams;
	public MapData(String name, String author, String realName, int matchLenght) {
		this.name = name;
		this.author = author;
		this.realName = realName;
		this.matchLenght = matchLenght;
		this.inProgress = false;
		this.redSpawn = new Location(null, 0,0,0);
		this.blueSpawn = new Location(null, 0,0,0);
		this.redCoreLocation = new Location(null, 0,0,0);
		this.blueCoreLocation = new Location(null, 0,0,0);
		this.coreRadius = 0;
		this.redInv = new HashMap<String, ItemStack>();
		this.blueInv = new HashMap<String, ItemStack>();
		this.dropItemsOnDeath = true;
		this.world = null;
		this.spawnMonsters = false;
		this.blockBreaking = true;
		this.spawnProtectionRadius = 5;
		this.objective = "";
		this.gameType = "TDM";
		this.forceTime = true;
		this.timeToForce = 0;
		this.teamOne = new TeamData("Red team", 50, ChatColor.RED);
		this.teamTwo = new TeamData("Blue team", 50, ChatColor.BLUE);
		this.changeBowProjectile = false;
	}
}
