package nl.thijsmolendijk.MyPGM.Commands;

import java.util.HashMap;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class XMLCommands implements CommandExecutor {
	/**
	 * XMLCommands class, used for processing /xml <action>
	 * Args: Main plugin -> The plugin that initiates this class
	 */
	public Main plugin;
	public XMLCommands(Main instance) {
		this.plugin = instance;
	}
	//TODO: Currently one function, add a reload xml function?
	//Command Handler, Setting the team is done by seperate commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if (!label.equalsIgnoreCase("xml")) return false;
		if (args.length < 1 || args.length > 1) return false;
		Player p = (Player) sender;
		if (args[0].equals("createInventory")) return this.createInvXML(p);
		return true;
	}
	public boolean createInvXML(Player p) {
		//Make a hashmap with the data of the inventory
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		//Method for receiving the slot of a object
		for (int slot = 0; slot < 40; slot++) {
			for (ItemStack i : p.getInventory().getContents()) {
				if (p.getInventory().getItem(slot) == null || i == null) continue;
				if (p.getInventory().getItem(slot).getType() == Material.AIR) continue;
				if (i.getType() == Material.AIR) continue;
				if (i.equals(p.getInventory().getItem(slot))) {
					if (!(map.containsKey(String.valueOf(slot)))) {
						map.put(String.valueOf(slot), i);
						System.out.println("Item: "+i.getType().toString()+", Slot: "+slot);
					} else {
						continue;
					}
				}
			}
		}
		//Add the armor, is not standard included
		map.put("36", p.getInventory().getBoots());
		map.put("37", p.getInventory().getLeggings());
		map.put("38", p.getInventory().getChestplate());
		map.put("39", p.getInventory().getHelmet());
		try {
			p.sendMessage(Tools.uploadToPasteBin(map));
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED+"A error occured, please try again.");
			System.out.println(e.getMessage());
		}
		return true;
	}
}
