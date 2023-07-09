// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

/**
 * Represents a group of actions: commands and messages.
 */
public class ActionsGroup {
	/**
	 * Is the action group enabled?
	 */
	public boolean enabled;
	/**
	 * A list of commands to execute.
	 */
	public List<String> commands;
	/**
	 * A message to send to the player.
	 */
	public String playerMessage;
	/**
	 * A message to broadcast to everyone.
	 */
	public String broadcastMessage;

	/**
	 * Constructs an ActionsGroup from a configuration file and event name.
	 *
	 * @param config The {@link FileConfiguration}.
	 * @param eventName The name of the event.  All parameters will be
	 * searched in "events.&lt;eventName&gt;".
	 */
	public ActionsGroup(FileConfiguration config, String eventName) {
		String path = "events." + eventName;
		this.enabled = config.getBoolean(path + ".enabled");
		this.commands = config.getStringList(path + ".commands");
		this.playerMessage = TextUtils.msgFromConfig(config.getStringList(path + ".playerMessage"));
		this.broadcastMessage = TextUtils.msgFromConfig(config.getStringList(path + ".broadcastMessage"));
	}

	/**
	 * Execute the commands as the console and broadcast messages to whoever necessary.
	 *
	 * @param player The player to send messages to.  Can be null.
	 * @param offlinePlayer OfflinePlayer to be used in command if player is null.
	 */
	public void execute(Player player, OfflinePlayer offlinePlayer) {
		if (enabled) {
			if (commands.size() != 0) {
				for (String command : commands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					                       PlaceholderAPI.setPlaceholders(player == null ? offlinePlayer : player, command));
				}
			}
			if (broadcastMessage != null) {
				Bukkit.getServer().broadcastMessage(PlaceholderAPI.setPlaceholders(player == null ? offlinePlayer : player,
				                                                                   broadcastMessage));
			}
			if (playerMessage != null && player != null) {
				player.sendMessage(PlaceholderAPI.setPlaceholders(player, playerMessage));
			}
		}
	}
}
