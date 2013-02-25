package nl.thijsmolendijk.MyPGM.Commands;

import nl.thijsmolendijk.MyPGM.*;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class JoinCommand implements CommandExecutor {
	/**
	 * JoinCommand class, used for processing /join
	 * Args: Main plugin -> The plugin that initiates this class
	 */
	public Main plugin;
	public JoinCommand(Main instance) {
		this.plugin = instance;
	}
	//Command Handler, Setting the team is done by seperate commands
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		if (label.equalsIgnoreCase("join")) {
			System.out.println("Successfull joining command");
			if (this.plugin.currentMap.name == "") return false;
			//Join spectators
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("o") || args[0].equalsIgnoreCase("obs")) {
					p.sendMessage(ChatColor.GOLD+"You joined the"+ChatColor.AQUA+" observers");
					p.setDisplayName(ChatColor.AQUA+p.getName()+ChatColor.RESET);
					this.plugin.spectators.remove(p.getName());
					this.plugin.spectators.add(p.getName());
					this.plugin.teamOne.remove(p.getName());
					this.plugin.teamTwo.remove(p.getName());
					p.teleport(this.plugin.currentMap.world.getSpawnLocation());
					p.getInventory().clear();
					p.setItemInHand(new ItemStack(Material.COMPASS, 1));
				
					Tools.hidePlayer(p);
					return true;
				}
			}
			//Check if player is op
			if (p.isOp()) {
				//Player is op, give him the ability to choose a team
				//Join red
				if (args.length < 1) return false;
				if (args[0].equalsIgnoreCase(this.plugin.currentMap.teamOne.joinArg)) {
					return this.joinOne(p);
				}
				//Join blue
				if (args[0].equalsIgnoreCase(this.plugin.currentMap.teamTwo.joinArg)) {
					return this.joinTwo(p);
				}
			} else {
				//Player is no op! Chosing a random team
				//Return if the arguments are more than 0
				if (args.length > 0) return false;
				if (this.plugin.teamOne.contains(p.getName()) || this.plugin.teamTwo.contains(p.getName())) return false;
				boolean random = Tools.getRandomBoolean();
				//Random is true, try to join tean one
				if (random) {
					if (this.plugin.teamOne.size() < this.plugin.currentMap.teamOne.maxSize) {
						//random = true, place in one
						return this.joinOne(p);
					} else {
						//random = true, no place in one
						random = false;
					}
				}
				if (!random) {
					//Random is false, try to join team two
					if (this.plugin.teamTwo.size() < this.plugin.currentMap.teamTwo.maxSize) {
						//random = false, place in two
						return this.joinTwo(p);
					} else if (this.plugin.teamOne.size() < this.plugin.currentMap.teamOne.maxSize) {
						//random = false, no place in two, place in one
						return this.joinOne(p);
					}
				}
				//random is false, no place in one and two
				p.sendMessage(ChatColor.RED+"The teams are full!");
				return true;
			}
		}
		return true;
	}
	public boolean joinOne(Player p) {
		if (this.plugin.teamOne.contains(p.getName())) return false;
		p.sendMessage(ChatColor.GOLD+"You joined "+this.plugin.currentMap.teamOne.preColor+this.plugin.currentMap.teamOne.name);
		if (this.plugin.currentMap.inProgress) {
			p.setDisplayName(this.plugin.currentMap.teamOne.preColor+p.getName()+ChatColor.RESET);
			this.plugin.teamOne.remove(p.getName());
			this.plugin.teamOne.add(p.getName());
			this.plugin.teamTwo.remove(p.getName());
			if (this.plugin.spectators.contains(p.getName())) {
				p.teleport(this.plugin.currentMap.redSpawn);
				p.getInventory().clear();
				for (ItemStack i : p.getInventory().getArmorContents()) {
					i.setType(Material.AIR);
				}
				p.setFoodLevel(20);
				p.setHealth(20);
				for (PotionEffect effect : p.getActivePotionEffects()) {
					p.removePotionEffect(effect.getType());
				}
				this.plugin.spectators.remove(p.getName());
				Tools.addItemsToPlayerInv(p, this.plugin.currentMap.redInv);
			} else {
				this.plugin.spectators.remove(p.getName());
				p.setHealth(0);
			}
			Tools.showPlayer(p);
		} else {
			p.setDisplayName(this.plugin.currentMap.teamOne.preColor+p.getName()+ChatColor.RESET);
			this.plugin.teamOne.remove(p.getName());
			this.plugin.teamOne.add(p.getName());
			this.plugin.teamTwo.remove(p.getName());
			this.plugin.spectators.remove(p.getName());
			this.plugin.spectators.add(p.getName());
		}
		return true;
	}
	//Let player p join team two
	//TODO: Change name to joinTwo
	public boolean joinTwo(Player p) {
		if (this.plugin.teamTwo.contains(p.getName())) return false;
		p.sendMessage(ChatColor.GOLD+"You joined "+this.plugin.currentMap.teamTwo.preColor+this.plugin.currentMap.teamTwo.name);
		if (this.plugin.currentMap.inProgress) {
			p.setDisplayName(this.plugin.currentMap.teamTwo.preColor+p.getName()+ChatColor.RESET);
			this.plugin.teamOne.remove(p.getName());
			this.plugin.teamTwo.remove(p.getName());
			this.plugin.teamTwo.add(p.getName());
			if (this.plugin.spectators.contains(p.getName())) {
				p.sendMessage("You were a spectator");
				p.teleport(this.plugin.currentMap.redSpawn);
				p.getInventory().clear();
				for (ItemStack i : p.getInventory().getArmorContents()) {
					i.setType(Material.AIR);
				}
				p.setFoodLevel(20);
				p.setHealth(20);
				for (PotionEffect effect : p.getActivePotionEffects()) {
					p.removePotionEffect(effect.getType());
				}
				this.plugin.spectators.remove(p.getName());
				Tools.addItemsToPlayerInv(p, this.plugin.currentMap.blueInv);
			} else {
				this.plugin.spectators.remove(p.getName());
				p.setHealth(0);
			}
			Tools.showPlayer(p);
		
		} else {
			p.setDisplayName(this.plugin.currentMap.teamTwo.preColor+p.getName()+ChatColor.RESET);
			this.plugin.spectators.remove(p.getName());
			this.plugin.spectators.add(p.getName());
			this.plugin.teamOne.remove(p.getName());
			this.plugin.teamTwo.remove(p.getName());
			this.plugin.teamTwo.add(p.getName());
		}
		return true;
	}
}
