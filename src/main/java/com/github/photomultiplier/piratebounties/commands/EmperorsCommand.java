// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.commands;

import com.github.photomultiplier.piratebounties.PirateBounties;
import com.github.photomultiplier.piratebounties.managers.Emperor;
import com.github.photomultiplier.piratebounties.managers.EmperorsManager;
import com.github.photomultiplier.piratebounties.utils.ParamSubst;
import com.github.photomultiplier.piratebounties.utils.TextUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * A command to retrieve the list of emperors.
 */
public class EmperorsCommand implements CommandExecutor {
	String listHeaderMessage;
	String listLineMessage;
	String noEmperorsMessage;

	/**
	 * Initializes the command, loading responses from the config file.
	 */
	public EmperorsCommand() {
		FileConfiguration config = PirateBounties.getPlugin().getConfig();
		listHeaderMessage = TextUtils.parseMessageFromConfig(config.getStringList("emperorsCommand.messages.listHeader"));
		listLineMessage = TextUtils.parseMessageFromConfig(config.getStringList("emperorsCommand.messages.listLine"));
		noEmperorsMessage = TextUtils.parseMessageFromConfig(config.getStringList("emperorsCommand.messages.noEmperors"));
	}

	/**
	 * The actual command.
	 *
	 * @param sender Who sent the command.
	 * @param command The command which was executed.
	 * @param label The alias used.
	 * @param args Arguments passed to the command.
	 * @return true if the command is valid.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Emperor[] leaderBoard = EmperorsManager.getLeaderBoard();
		String message;

		if (leaderBoard[0] == null) {
			message = TextUtils.replace(noEmperorsMessage,
			                            new ParamSubst("threshold", EmperorsManager.getEmperorThreshold()));

			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.sendMessage(message);
			} else {
				System.out.println(ChatColor.stripColor(message));
			}
		} else {
			message = listHeaderMessage;

			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.sendMessage(listHeaderMessage);
			} else {
				System.out.println(ChatColor.stripColor(message));
			}

			for (int i = 0; i < leaderBoard.length; i++) {
				Emperor emperor = leaderBoard[i];

				if (emperor == null) {
					break;
				} else {
					message = TextUtils.replace(listLineMessage,
					                            new ParamSubst("index", (i + 1)),
					                            new ParamSubst("player", emperor.displayName),
					                            new ParamSubst("bounty", emperor.bounty));

					if (sender instanceof Player) {
						Player p = (Player) sender;
						p.sendMessage(message);
					} else {
						System.out.println(ChatColor.stripColor(message));
					}
				}
			}
		}
		return true;
	}
}
