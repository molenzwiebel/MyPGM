package nl.thijsmolendijk.MyPGM;

import org.bukkit.ChatColor;

public class TeamData {
	public String name;
	public int maxSize;
	public ChatColor preColor;
	public String joinArg;
	public TeamData(String name, int maxSize, ChatColor preColor) {
		this.name = name;
		this.maxSize = maxSize;
		this.preColor = preColor;
	}
}
