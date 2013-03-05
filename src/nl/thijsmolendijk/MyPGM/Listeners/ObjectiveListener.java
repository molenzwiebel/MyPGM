package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Pair;
import nl.thijsmolendijk.MyPGM.Objectives.Core;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class ObjectiveListener implements Listener {
	public Main plugin;
	public int count;
	public ObjectiveListener(Main instance) {
		this.plugin = instance;
	}
	@EventHandler()
	public void onLiquidFlow(BlockFromToEvent event) {
		if (!this.plugin.currentMap.gameType.equalsIgnoreCase("dtc")) return;
		if (event.getBlock() == null || event.getToBlock() == null) return;
		Object[] data = this.getSourceOfBlock(new Object[] { event.getBlock(), 0 });
		if (data[0] == null || data == null) return;
		int radius = this.plugin.currentMap.coreRadius;
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					Pair p = this.plugin.currentMap.cores.coreBroken((Block) data[0]);
					if ((Boolean) p.bool()) {
						if (((Integer) data[1]) > 5) {
							event.setCancelled(true);
							Core c = this.plugin.currentMap.cores.coreByID((String) p.core());
							this.plugin.endGameWithWinner(c.owner, true);
						}
					}
				}
			}
		}
	}
	public Object[] getSourceOfBlock(Object[] map) {
		Block b = (Block) map[0];
		Integer count = (Integer) map[1];
		count++;
		if (b == null) {
			return null;
		}
		if (b.getData() == 0) {
			return new Object[] { b, count };
		}
		if (b.getData() == 8) {
			return this.getSourceOfBlock(new Object[] { b.getRelative(BlockFace.UP), count });
		}
		if (b.getData() > 0 && b.getData() < 8) {
			for (BlockFace face : BlockFace.values()) {
				if (b.getRelative(face) == null) {
					continue;
				}
				if (b.getRelative(face).getData() == b.getData()-1 && b.getRelative(face).getType() == b.getType()) {
					return this.getSourceOfBlock(new Object[] { b.getRelative(face), count });
				}
			}
		}
		return null;
	}
}
