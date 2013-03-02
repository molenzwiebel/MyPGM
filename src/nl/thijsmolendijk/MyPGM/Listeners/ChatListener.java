package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {
	public Main plugin;

	public ChatListener(Main instance) {
		this.plugin = instance;
	}

	@EventHandler()
	// Handle global chat
	public void onPublicChat(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().split(" ")[0].equals("/g")) {
			event.setCancelled(true);
			String chatMessage = event.getMessage().substring(3);
			this.plugin.getServer().broadcastMessage(
					"<" + event.getPlayer().getDisplayName() + "> "
							+ chatMessage);
		}
	}
	//Handle team chat
		@EventHandler()
		public void onChatEvent(AsyncPlayerChatEvent event) {
			TeamData team = this.plugin.currentMap.teams.teamForPlayer(event.getPlayer());
			if (team == null) {
				event.setCancelled(true);
				for (String pName : this.plugin.spectators) {
					Player p = this.plugin.getServer().getPlayer(pName);
					String message = ChatColor.AQUA+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
					p.sendMessage(message);
				}
				return;
			}
			for (String str : team.members) {
				Player p = this.plugin.getServer().getPlayer(str);
				String message = team.preColor+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
				p.sendMessage(message);
			}
		}
	
}
