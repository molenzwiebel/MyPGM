package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
public class ObserverListener implements Listener {
	/**
	 * Class ObserverListener, used to block all actions that
	 * can be abused by observers.
	 * @param instance - The instance of the plugin initiating the listener
	 */
	public Main plugin;
	public ObserverListener(Main instance) {
		this.plugin = instance;
	}
	@EventHandler()
	public void onInteract(PlayerInteractEvent event) {
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			//Player clicked no block, return
			if (event.getClickedBlock() == null) {
				event.setCancelled(true);
				return;
			}
			//Player is a observer and clicked with a compass
			//OLD: Used before adding WorldEdit as the compass handler!!!
//			if(event.getPlayer().getItemInHand().getType() == Material.COMPASS){
//                		event.setCancelled(true);
//                		Block target = event.getPlayer().getTargetBlock(null, 200);
//                		if (target == null) {
//                			event.setCancelled(true);
//                			return;
//                		}
//                		float Yaw = event.getPlayer().getLocation().getYaw();
//                		float Pitch = event.getPlayer().getLocation().getPitch();
//                		Location loc = target.getLocation();
//               			loc.setY(loc.getY()+1);
//                		loc.setPitch(Pitch);
//                		loc.setYaw(Yaw);
//                		event.getPlayer().teleport(loc);
//           		}
			//Make observers able to open chests
			if (event.getClickedBlock().getType() == Material.CHEST) return;
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			//Player is observer, cancelling event
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		//Handle spawn protection
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler()
	public void onItemDrop(PlayerDropItemEvent event) {
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler() 
	public void onInventoryClick(InventoryClickEvent event) {
		if (this.plugin.spectators.contains(event.getWhoClicked().getName()) && event.getWhoClicked().getOpenInventory().getType() == InventoryType.CHEST) {
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler()
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (this.plugin.spectators.contains(event.getPlayer().getName())) {
			event.setCancelled(true);
			return;
		}
	}
}
