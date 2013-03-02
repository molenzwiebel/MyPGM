package nl.thijsmolendijk.MyPGM.Teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class TeamManager {
	public List<TeamData> teams;
	public TeamManager() {
		this.teams = new ArrayList<TeamData>();
	}
	public void addTeam(TeamData t) {
		this.teams.remove(t);
		this.teams.add(t);
	}
	public TeamData teamForPlayer(Player p) {
		TeamData result = null;
		for (TeamData t : this.teams) {
			if (t.members.contains(p.getName())) {
				result = t;
			}
		}
		return result;
	}
	public TeamData teamForID(String id) {
		TeamData result = null;
		for (TeamData t : this.teams) {
			if (t.id.equals(id)) {
				result = t;
			}
		}
		return result;
	}
	public void logTeams() {
		for (TeamData d : this.teams) {
			System.out.println(d.id+", "+d.name+", "+d.preColor+", "+d.spawnInventory);
		}
	}
	public void removePlayerFromAllTeams(Player p) {
		for (TeamData s : this.teams) {
			s.members.remove(p.getName());
		}
	}
	public TeamData teamForJoinArg(String arg) throws Exception {
		int count = 0;
		TeamData found = null;
		for (TeamData s : teams) {
			if (s.joinArg.equals(arg)) {
				count++;
				found = s;
			}
		}
		if (count > 1) throw new Exception("Found more than one team for the argument specified");
		return found;
	}
}
