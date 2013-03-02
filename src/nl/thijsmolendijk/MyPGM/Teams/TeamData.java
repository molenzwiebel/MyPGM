package nl.thijsmolendijk.MyPGM.Teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class TeamData {
	public String name;
	public String id;
	public int maxSize;
	public ChatColor preColor;
	public String joinArg;
	public List<String> members;
	public boolean isSpectating;
	public Location spawn;
	public int score;
	public HashMap<String, ItemStack> spawnInventory;
	public TeamData(String name, int maxSize, ChatColor preColor) {
		this.name = name;
		this.maxSize = maxSize;
		this.preColor = preColor;
		this.isSpectating = false;
		this.members = new ArrayList<String>();
	}
	public boolean isFull() {
		if (this.members.size() >= this.maxSize) return true;
		return false;
	}
}
