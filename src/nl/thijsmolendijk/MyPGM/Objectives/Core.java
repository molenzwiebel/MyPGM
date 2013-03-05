package nl.thijsmolendijk.MyPGM.Objectives;

import java.util.List;

import nl.thijsmolendijk.MyPGM.Teams.TeamData;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Core implements Objective {
	public List<String> team;
	public Location center;
	public int radius;
	public TeamData owner;
	public String id;
	public Core(String id, List<String> t, Location c, int r, TeamData f) {
		this.id = id;
		this.team = t;
		this.center = c;
		this.radius = r;
		this.owner = f;
	}
	@Override
	public boolean blockLiesInRegion(Block b) {
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
