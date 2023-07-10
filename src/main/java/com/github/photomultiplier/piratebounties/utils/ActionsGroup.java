// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
	 * Constructs an ActionsGroup from raw parameters.
	 *
	 * @param enabled Is the action group enabled?
	 * @param commands A list of commands to execute.
	 * @param playerMessage A message to send to the player.
	 * @param broadcastMessage A message to broadcast to everyone.
	 */
	public ActionsGroup(boolean enabled, List<String> commands, String playerMessage, String broadcastMessage) {
		this.enabled = enabled;
		this.commands = commands;
		this.playerMessage = playerMessage;
		this.broadcastMessage = broadcastMessage;
	}

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
	 * Note that the substitutions are applied to commands and messages alike.
	 *
	 * @param player The player to send messages to.  Can be null.
	 * @param substitutions The list of substitutions.
	 */
	public void execute(Player player, ParamSubst... substitutions) {
		if (enabled) {
			if (commands.size() != 0) {
				for (String command : commands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), TextUtils.replace(command, substitutions));
				}
			}
			if (broadcastMessage != null) {
				Bukkit.getServer().broadcastMessage(TextUtils.replace(broadcastMessage, substitutions));
			}
			if (playerMessage != null && player != null) {
				player.sendMessage(TextUtils.replace(playerMessage, substitutions));
			}
		}
	}
}
