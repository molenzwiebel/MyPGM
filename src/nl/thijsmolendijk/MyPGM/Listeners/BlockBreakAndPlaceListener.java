package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;

import org.bukkit.ChatColor;
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
		      for (TeamData d : this.plugin.currentMap.teams.teams) {
		    	  if (d.spawn.getBlock().getRelative(x, y, z).equals(event.getBlock())) {
		    		  event.setCancelled(true);
			          event.getPlayer().sendMessage(ChatColor.RED + "No breaking or placing blocks in spawn!");
		    	  }
		      }
		     }
		   }
		}
		//Core breaking!!!
	}
	@EventHandler()
	public void onBlockPlace(BlockPlaceEvent event) {
		int radius = this.plugin.currentMap.spawnProtectionRadius;
		for (int x = -(radius); x <= radius; x ++)
		{
		  for (int y = -(radius); y <= radius; y ++)
		  {
		    for (int z = -(radius); z <= radius; z ++)
		    {
		      for (TeamData d : this.plugin.currentMap.teams.teams) {
		    	  if (d.spawn.getBlock().getRelative(x, y, z).equals(event.getBlock())) {
		    		  event.setCancelled(true);
			          event.getPlayer().sendMessage(ChatColor.RED + "No breaking or placing blocks in spawn!");
		    	  }
		      }
		     }
		   }
		}
	}
}
