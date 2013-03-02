package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Tools;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDamageListener implements Listener {
	public Main plugin;
	public PlayerDamageListener(Main instance) {
		this.plugin = instance;
	}
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
		if (this.plugin.currentMap.teams.teamForPlayer(p).equals(this.plugin.currentMap.teams.teamForPlayer(damager))) {
			event.setCancelled(true);
		} 
	}
	@EventHandler()
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (!(this.plugin.currentMap.gameType.equalsIgnoreCase("tdm"))) return;
		TeamData d = this.plugin.currentMap.teams.teamForPlayer(event.getEntity().getKiller());
		d.score++;
		
		if (this.plugin.currentMap.dropItemsOnDeath == false || this.plugin.spectators.contains(event.getEntity().getName())) {
			event.setDroppedExp(0);
			event.getDrops().clear();
		}
		if (this.plugin.currentMap.teams.teamForPlayer(event.getEntity()) == null && !(this.plugin.spectators.contains(event.getEntity().getName()))) {
			event.setDeathMessage("");
		}
		if (this.plugin.spectators.contains(event.getEntity().getName())) {
			event.setDeathMessage("");
		}
		event.setDeathMessage(this.customDeath(event));
		event.setDeathMessage(event.getDeathMessage().replace(event.getEntity().getName(), event.getEntity().getDisplayName()));
		event.setDeathMessage(event.getDeathMessage().replace(event.getEntity().getKiller().getName(), event.getEntity().getKiller().getDisplayName()));
	}
	@EventHandler()
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (this.plugin.currentMap.name.equals("")) return;
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			event.setRespawnLocation(this.plugin.currentMap.world.getSpawnLocation());
			event.getPlayer().setItemInHand(new ItemStack(Material.COMPASS, 1));
		}
		TeamData d = this.plugin.currentMap.teams.teamForPlayer(event.getPlayer());
		event.getPlayer().teleport(d.spawn);
		Tools.addItemsToPlayerInv(event.getPlayer(), d.spawnInventory);
	}
	@SuppressWarnings("unused")
	public String customDeath(PlayerDeathEvent event) {
		 
        Entity victim = event.getEntity();
        DamageCause damage = null;
        String Victim = "", Killer = "", Cause = "";
        boolean byPlayer = false;
        boolean flipIt = false;
 
        if (victim.getLastDamageCause() != null) {
            damage = victim.getLastDamageCause().getCause();
        } else {
            return "";
        }
 
        // Determine the victim of the death
        // ############################################################################
        if (victim instanceof Player) {
            // Get the player display name
            Victim = ((Player) victim).getDisplayName();
        } else {
            // Format the victim to a readable form
            Victim = victim.getType().getName().toLowerCase().replace("_", " ");
 
            if (victim instanceof Wolf) {
                Wolf wolf = (Wolf) victim;
                if (wolf.getOwner() != null) {
                    Player ply = (Player) wolf.getOwner();
                    Victim = Victim.concat(" owned by ").concat(
                            ply.getDisplayName());
                }
            }
 
            // Concat the appropriate article before the noun.
            if (Victim.matches("[aeiou].*?")) {
                Victim = "An ".concat(Victim);
            } else {
                Victim = "A ".concat(Victim);
            }
        }
 
        // Determine the cause of the death
        // ############################################################################
        if (damage.equals(DamageCause.ENTITY_ATTACK)) {
            // The killer was another entity.
            // Determine the entity
            Entity killer = event.getEntity().getKiller();
 
            if (killer instanceof Player) {
 
                byPlayer = true;
                Killer = " from ".concat(event.getEntity().getKiller()
                        .getDisplayName());
 
                ItemStack item = event.getEntity().getKiller().getItemInHand();
 
                String theCause = item.getType().toString().toLowerCase();
 
                if (theCause.equals("air")) {
                    Cause = " attacking with their awesome fist";
                } else {
 
                    if (theCause.matches("[aeiou].*?")) {
                        Cause = " attacking them with an ".concat(theCause
                                .replace("_", " "));
                    } else {
                        Cause = " attacking them with a ".concat(theCause
                                .replace("_", " "));
                    }
                }
 
            } else {
 
                if (victim.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
 
                    EntityDamageByEntityEvent eEvent = (EntityDamageByEntityEvent) victim
                            .getLastDamageCause();
                    String entityName = eEvent.getDamager().getType().getName()
                            .toLowerCase();
 
                    if (entityName.matches("[aeiou].*?")) {
                        Killer = " by an ".concat(entityName);
                    } else {
                        Killer = " by a ".concat(entityName);
                    }
 
                    if (eEvent.getDamager() instanceof Wolf) {
                        Wolf wolf = (Wolf) eEvent.getDamager();
                        if (wolf.getOwner() != null) {
                            Player ply = (Player) wolf.getOwner();
                            Killer = Killer.concat(" owned by ").concat(
                                    ply.getDisplayName());
                        }
                    }
 
                    Cause = " by being attacked";
                    flipIt = true;
 
                } else {
                    // unknown
                    Cause = " unknown";
                }
            }
        } else if (damage.equals(DamageCause.BLOCK_EXPLOSION)) {
            Cause = " from an explosion";
        } else if (damage.equals(DamageCause.CONTACT)) {
            Cause = " from kissing a cactus";
        } else if (damage.equals(DamageCause.CUSTOM)) {
            Cause = " from something custom";
        } else if (damage.equals(DamageCause.DROWNING)
                || damage.equals(DamageCause.SUFFOCATION)) {
            Cause = " from not taking a breath";
        } else if (damage.equals(DamageCause.ENTITY_EXPLOSION)) {
            Cause = " by playing with a creeper";
        } else if (damage.equals(DamageCause.FALL)) {
            Cause = " by bungee jumping without a cord";
        } else if (damage.equals(DamageCause.FALLING_BLOCK)) {
            Cause = " by being smooshed";
        } else if (damage.equals(DamageCause.FIRE)
                || damage.equals(DamageCause.FIRE_TICK)) {
            Cause = " after jumping into a campfire";
        } else if (damage.equals(DamageCause.LAVA)) {
            Cause = " from swimming in lava";
        } else if (damage.equals(DamageCause.LIGHTNING)) {
            Cause = " by flying a kite in an electrical storm";
        } else if (damage.equals(DamageCause.MAGIC)) {
            Cause = " by playing with magic";
        } else if (damage.equals(DamageCause.MELTING)) {
            Cause = " from thawing.";
        } else if (damage.equals(DamageCause.POISON)) {
            Cause = " by drinking poison";
        } else if (damage.equals(DamageCause.PROJECTILE)) {
            Cause = " from being shot";
        } else if (damage.equals(DamageCause.STARVATION)) {
            Cause = " because they didn't go grocery shopping";
        } else if (damage.equals(DamageCause.SUICIDE)) {
            Cause = " by taking the easy way out";
        } else if (damage.equals(DamageCause.VOID)) {
            Cause = " by falling into the abyss";
        } else if (damage.equals(DamageCause.WITHER)) {
            Cause = " because they danced with the wither";
        }
 
        String toSend = Victim.concat(" died").concat(Killer).concat(Cause)
                .concat(".");
 
        if (flipIt) {
            toSend = Victim.concat(" died").concat(Cause).concat(Killer)
                    .concat(".");
        }
        return toSend;
    }
}
