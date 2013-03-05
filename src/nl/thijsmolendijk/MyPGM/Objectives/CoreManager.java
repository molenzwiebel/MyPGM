package nl.thijsmolendijk.MyPGM.Objectives;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import nl.thijsmolendijk.MyPGM.*;
public class CoreManager {
	public List<Core> cores;
	public CoreManager() {
		this.cores = new ArrayList<Core>();
	}
	public void addCore(Core core) {
		cores.remove(core);
		cores.add(core);
	}
	public Pair coreBroken(Block data) {
		boolean found = false;
		String id = "";
		for (Core c : cores) {
			found = c.blockLiesInRegion(data);
			if (found) id = c.id;
		}
		return new Pair(found, id);
	}
	public Core coreByID(String id) {
		Core c = null;
		for (Core core : cores) {
			if (core.id.equals(id)) c = core;
		}
		return c;
	}
}