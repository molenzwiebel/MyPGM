package nl.thijsmolendijk.MyPGM.Cores;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Core {
	public List<String> team;
	public Location center;
	public int radius;
	public boolean forTeamOne;
	public String id;
	public Core(String id, List<String> t, Location c, int r, boolean f) {
		this.id = id;
		this.team = t;
		this.center = c;
		this.radius = r;
		this.forTeamOne = f;
	}
	public boolean blockIsWithinCoreArea(Block b) {
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {

					if (this.center.getBlock()
							.getRelative(x, y, z).equals(b)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
