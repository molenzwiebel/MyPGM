package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Tools;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakAndPlaceListener implements Listener {
	public Main plugin;
	public BlockBreakAndPlaceListener(Main instance) {
		this.plugin = instance;
	}
	@EventHandler()
	public void onBlockBreak(BlockBreakEvent event) {
		if (this.plugin.currentMap.blockBreaking == false) {
			//BlockBreaking is off, cancelling event
			event.setCancelled(true);
			return;
		}
		//Spawn protection check
		int radius = this.plugin.currentMap.spawnProtectionRadius;
		for (int x = -(radius); x <= radius; x ++)
		{
		  for (int y = -(radius); y <= radius; y ++)
		  {
		    for (int z = -(radius); z <= radius; z ++)
		    {
		      if (this.plugin.currentMap.redSpawn.getBlock().getRelative(x, y, z).equals(event.getBlock()) ||
		    	  this.plugin.currentMap.blueSpawn.getBlock().getRelative(x, y, z).equals(event.getBlock()))
		      {
		          event.setCancelled(true);
		          event.getPlayer().sendMessage(ChatColor.RED + "No breaking or placing blocks in spawn!");
		       }
		     }
		   }
		}
		//Core breaking!!!
		if (this.plugin.currentMap.gameType.equalsIgnoreCase("dtc")) {
			this.checkCoreBreak(event);
		}	
	}
	public void checkCoreBreak(BlockBreakEvent event) {
	//Check if a core is broken
	int radius = this.plugin.currentMap.coreRadius;
		for (int x = -(radius); x <= radius; x ++)
		{
		  for (int y = -(radius); y <= radius; y ++)
		  {
		    for (int z = -(radius); z <= radius; z ++)
		    {
		      if (this.plugin.currentMap.redCoreLocation.getBlock().getRelative(x, y, z).equals(event.getBlock()) &&
		    		  event.getBlock().getType() == Material.OBSIDIAN && Tools.hasLava(event.getBlock()))
		      {
		          if (!(this.plugin.teamOne.contains(event.getPlayer().getName()))) {
		        	  event.setCancelled(false);
		        	  this.plugin.scoreOne = this.plugin.scoreTwo+1;
		        	  this.plugin.endGame();
		        	  
		          } else {
		        	  event.getPlayer().sendMessage(ChatColor.RED + "Don't break your own core!");
		        	  event.setCancelled(true);
		          }
		      }
		      if (this.plugin.currentMap.blueCoreLocation.getBlock().getRelative(x, y, z).equals(event.getBlock()) &&
		    		  event.getBlock().getType() == Material.OBSIDIAN && Tools.hasLava(event.getBlock()))
		      {
		    	  if (!(this.plugin.teamTwo.contains(event.getPlayer().getName()))) {
		        	  event.setCancelled(false);
		        	  
		        	  this.plugin.scoreTwo = this.plugin.scoreOne+1;
		        	  this.plugin.endGame();
		          } else {
		        	  event.getPlayer().sendMessage(ChatColor.RED + "Don't break your own core!");
		        	  event.setCancelled(true);
		          }
		      }
		     }
		   }
		}
	}
	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		//Handle spawn protection
		int radius = this.plugin.currentMap.spawnProtectionRadius;
		for (int x = -(radius); x <= radius; x ++)
		{
		  for (int y = -(radius); y <= radius; y ++)
		  {
		    for (int z = -(radius); z <= radius; z ++)
		    {
		      if (this.plugin.currentMap.redSpawn.getBlock().getRelative(x, y, z).equals(event.getBlock()) ||
		    	  this.plugin.currentMap.blueSpawn.getBlock().getRelative(x, y, z).equals(event.getBlock()))
		      {
		          event.setCancelled(true);
		          event.getPlayer().sendMessage(ChatColor.RED + "No breaking or placing blocks in spawn!");
		       }
		     }
		   }
		}
	}
}
