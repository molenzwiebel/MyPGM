package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onShoot(ProjectileLaunchEvent event) throws ClassNotFoundException {
		if (!this.plugin.currentMap.changeBowProjectile) return;
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			if (arrow.getShooter() instanceof Player) {
				Player shooter = (Player) arrow.getShooter();
				if (shooter.getItemInHand().getType() == Material.BOW) {
					event.setCancelled(true);
					Class<?> cls = Class.forName("org.bukkit.entity."+this.plugin.currentMap.newBowEntity);
					Class<? extends Projectile> cls2 = (Class<? extends Projectile>) cls;
					shooter.launchProjectile(cls2).setVelocity(
							arrow.getVelocity().multiply(this.plugin.currentMap.newBowVelocity));
				}
			}
		}
	}
}
