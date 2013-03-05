package nl.thijsmolendijk.MyPGM.Objectives;


import org.bukkit.block.Block;

public interface Objective {
	public boolean blockLiesInRegion(Block b);
}
