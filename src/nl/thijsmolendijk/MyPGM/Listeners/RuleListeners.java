package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class RuleListeners implements Listener {
	public Main plugin;

	public RuleListeners(Main instance) {
		this.plugin = instance;
	}

	@EventHandler()
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (this.plugin.currentMap.spawnMonsters)
			return;
		if (event.getSpawnReason() == SpawnReason.SPAWNER
				|| event.getSpawnReason() == SpawnReason.SPAWNER_EGG)
			return;
		event.setCancelled(true);
	}

	@EventHandler
	public void onShoot(ProjectileLaunchEvent event) throws ClassNotFoundException {
		if (!this.plugin.currentMap.changeBowProjectile) return;
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if (arrow.getShooter() instanceof Player) {
				Player shooter = (Player) arrow.getShooter();
				if (shooter.getItemInHand().getType() == Material.BOW) {
					event.setCancelled(true);
					shooter.launchProjectile(this.classForString(this.plugin.currentMap.newBowEntity)).setVelocity(
							arrow.getVelocity().multiply(this.plugin.currentMap.newBowVelocity));
				}
			}
		}
	}
	public Class<? extends Projectile> classForString(String str) {
		if (str.equalsIgnoreCase("Snowball")) return Snowball.class;
		if (str.equalsIgnoreCase("enderpearl")) return EnderPearl.class;
		if (str.equalsIgnoreCase("fireball")) return Fireball.class;
		return Arrow.class;
	}
}
