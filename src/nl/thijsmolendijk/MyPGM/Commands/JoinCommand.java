package nl.thijsmolendijk.MyPGM.Commands;

import java.util.Random;

import nl.thijsmolendijk.MyPGM.*;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;

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
					for (TeamData s : this.plugin.currentMap.teams.teams) {
						s.members.remove(p.getName());
					}
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
					try {
						return this.joinTeam(this.plugin.currentMap.teams.teamForJoinArg(args[0]), p);
					} catch (Exception e) {
						p.sendMessage(e.getMessage());
					}
				//Join blue
				
			} else {
				//Player is no op! Chosing a random team
				//Return if the arguments are more than 0
				if (args.length > 0) return false;
				for (TeamData s : this.plugin.currentMap.teams.teams) {
					if (s.members.contains(p.getName())) return false;;
				}
				boolean notFull = false;
				for (TeamData d : this.plugin.currentMap.teams.teams) {
					if (!d.isFull()) notFull = true;
				}
				int random = Tools.showRandomInteger(0, this.plugin.currentMap.teams.teams.size(), new Random());
				TeamData team = this.randomTeam(random);
				//random is false, no place in one and two
				if (!notFull) {
					p.sendMessage(ChatColor.RED+"The teams are full!");
					return true;
				}
				return this.joinTeam(team, p);
			}
		}
		return true;
	}
	public boolean joinTeam(TeamData data, Player p) {
		if (data.members.contains(p.getName())) return false;
		p.sendMessage(ChatColor.GOLD+"You joined "+data.preColor+data.name);
		if (this.plugin.currentMap.inProgress) {
			p.setDisplayName(data.preColor+p.getName()+ChatColor.RESET);
			this.plugin.currentMap.teams.removePlayerFromAllTeams(p);
			if (this.plugin.spectators.contains(p.getName())) {
				p.teleport(data.spawn);
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
				Tools.addItemsToPlayerInv(p, data.spawnInventory);
			} else {
				this.plugin.spectators.remove(p.getName());
				p.setHealth(0);
			}
			Tools.showPlayer(p);
		} else {
			p.setDisplayName(data.preColor+p.getName()+ChatColor.RESET);
			this.plugin.currentMap.teams.removePlayerFromAllTeams(p);
			data.members.add(p.getName());
			this.plugin.spectators.remove(p.getName());
			this.plugin.spectators.add(p.getName());
		}
		return true;
	}
	public TeamData randomTeam(int team) {
		int Low = 0;
		int High = this.plugin.currentMap.teams.teams.size();
		int R = new Random().nextInt(High-Low) + Low;
		if (this.plugin.currentMap.teams.teams.get(R).isFull()) return this.randomTeam(R);
		return this.plugin.currentMap.teams.teams.get(R);
	}
}
