package nl.thijsmolendijk.MyPGM;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
public class PGMListener implements Listener {
	Main plugin;
	//Init
	public PGMListener(Main mainInstance) {
		this.plugin = mainInstance;
	}
	
	//Events
	
	//Observer cancelling end
	@EventHandler()
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		//Handle team killing and observer interference
		if (!(event.getDamager() instanceof Player)) return;
		Player p = (Player) event.getDamager();
		if (this.plugin.spectators.contains(p.getName())) {
			event.setCancelled(true);
			return;
		}
		if (!(event.getEntity() instanceof Player)) return;
		if (!(event.getDamager() instanceof Player)) return;
		Player damager = (Player) event.getEntity();
		if (this.plugin.teamOne.contains(p.getName()) && this.plugin.teamOne.contains(damager.getName())) {
			event.setCancelled(true);
		} 
		if (this.plugin.teamTwo.contains(p.getName()) && this.plugin.teamTwo.contains(damager.getName())) {
			event.setCancelled(true);
		} 
		
	}
	//Death and score handling
	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent event) {
		//TODO: CHECK FOR GAME TYPE!
		if (!(event.getDeathMessage().contains("died"))) {
			
			if (this.plugin.teamOne.contains(event.getEntity().getName())) {
				this.plugin.scoreOne++;
			}
			if (this.plugin.teamTwo.contains(event.getEntity().getName())) {
				this.plugin.scoreTwo++;
			}
		} else {
			event.setDeathMessage("");
		}
		EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
		if (damageEvent instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) damageEvent).getDamager() instanceof Player) {
			Player killer = (Player) ((EntityDamageByEntityEvent) damageEvent).getDamager();
			event.setDeathMessage(event.getEntity().getDisplayName()+ChatColor.LIGHT_PURPLE+" was killed by "+killer.getDisplayName());
		}
		if (this.plugin.currentMap.dropItemsOnDeath == false || this.plugin.spectators.contains(event.getEntity().getName())) {
			event.setDroppedExp(0);
			event.getDrops().clear();
		}
		if (!(this.plugin.teamOne.contains(event.getEntity().getName())) && !(this.plugin.teamTwo.contains(event.getEntity().getName())) && !(this.plugin.spectators.contains(event.getEntity().getName()))) {
			event.setDeathMessage("");
		}
		if (this.plugin.spectators.contains(event.getEntity().getName())) {
			event.setDeathMessage("");
		}
		event.setDeathMessage(event.getDeathMessage().replace(event.getEntity().getName(), event.getEntity().getDisplayName()));
	}
	//Make the player respawn at his respawn point and add the needed inventory
	@EventHandler()
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (this.plugin.currentMap.name.equals("")) return;
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			event.setRespawnLocation(this.plugin.currentMap.world.getSpawnLocation());
			event.getPlayer().setItemInHand(new ItemStack(Material.COMPASS, 1));
		}
		if (this.plugin.teamOne.contains(event.getPlayer().getName())) {
			event.setRespawnLocation(this.plugin.currentMap.redSpawn);
			Tools.addItemsToPlayerInv(event.getPlayer(), this.plugin.currentMap.redInv);
		}
		if (this.plugin.teamTwo.contains(event.getPlayer().getName())) {
			event.setRespawnLocation(this.plugin.currentMap.blueSpawn);
			Tools.addItemsToPlayerInv(event.getPlayer(), this.plugin.currentMap.blueInv);
		}
	}
	//Handle team chat
	@EventHandler()
	public void onChatEvent(AsyncPlayerChatEvent event) {
		if (this.plugin.teamOne.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			for (String pName : this.plugin.teamOne) {
				Player p = this.plugin.getServer().getPlayer(pName);
				String message = this.plugin.currentMap.teamOne.preColor+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
				p.sendMessage(message);
			}
			return;
		}
		if (this.plugin.teamTwo.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			for (String pName : this.plugin.teamTwo) {
				Player p = this.plugin.getServer().getPlayer(pName);
				String message = this.plugin.currentMap.teamTwo.preColor+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
				p.sendMessage(message);
			}
			return;
		}
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			for (String pName : this.plugin.spectators) {
				Player p = this.plugin.getServer().getPlayer(pName);
				String message = ChatColor.AQUA+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
				p.sendMessage(message);
			}
			return;
		}
	}
	//Handle global chat
	@EventHandler()
	public void onPublicChat(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().split(" ")[0].equals("/g")) {
			event.setCancelled(true);
			String chatMessage = event.getMessage().substring(3);
			this.plugin.getServer().broadcastMessage("<"+event.getPlayer().getDisplayName()+"> "+chatMessage);
		}
	}
	//Cancel mob spawning if needed
	@EventHandler()
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (this.plugin.currentMap.spawnMonsters) return;
		if (event.getSpawnReason() == SpawnReason.SPAWNER || event.getSpawnReason() == SpawnReason.SPAWNER_EGG) return;
		event.setCancelled(true);
	}
	//Let player p join team one
	//TODO: Change name to joinOne
	public boolean joinRed(Player p) {
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
	public boolean joinBlue(Player p) {
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
