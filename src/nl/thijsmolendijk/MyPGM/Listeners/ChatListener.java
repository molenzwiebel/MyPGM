package nl.thijsmolendijk.MyPGM.Listeners;

import nl.thijsmolendijk.MyPGM.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
			if (this.plugin.teamOne.contains(event.getPlayer().getName())) {
				event.setCancelled(true);
				for (String pName : this.plugin.teamOne) {
					Player p = this.plugin.getServer().getPlayer(pName);
					String message = this.plugin.currentMap.teamOne.preColor+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
					p.sendMessage(message);
				}
				return;
			}
			if (this.plugin.teamTwo.contains(event.getPlayer().getName())) {
				event.setCancelled(true);
				for (String pName : this.plugin.teamTwo) {
					Player p = this.plugin.getServer().getPlayer(pName);
					String message = this.plugin.currentMap.teamTwo.preColor+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
					p.sendMessage(message);
				}
				return;
			}
			if (this.plugin.spectators.contains(event.getPlayer().getName())) {
				event.setCancelled(true);
				for (String pName : this.plugin.spectators) {
					Player p = this.plugin.getServer().getPlayer(pName);
					String message = ChatColor.AQUA+"[Team] "+event.getPlayer().getName()+": "+ChatColor.RESET+event.getMessage();
					p.sendMessage(message);
				}
				return;
			}
		}
	
}
