// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.commands;

import com.github.photomultiplier.piratebounties.PirateBounties;
import com.github.photomultiplier.piratebounties.managers.BountyManager;
import com.github.photomultiplier.piratebounties.utils.ParamSubst;
import com.github.photomultiplier.piratebounties.utils.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

/**
 * A command to set a player's bounty.
 */
public class SetBountyCommand implements CommandExecutor {
	String okMessage;
	String insufficientArgumentsMessage;
	String wrongTypeMessage;
	String noPlayerMessage;
	String noPermissionMessage;
	String bountyDisabledMessage;

	final static String permission = "piratebounties.bounties.set";

	/**
	 * Initializes the command, loading responses from the config file.
	 */
	public SetBountyCommand() {
		FileConfiguration config = PirateBounties.getPlugin().getConfig();
		okMessage = TextUtils.msgFromConfig(config.getStringList("setBountyCommand.messages.ok"));
		insufficientArgumentsMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.insufficientArguments"));
		wrongTypeMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.wrongType"));
		noPlayerMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.noPlayer"));
		noPermissionMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.noPermission"));
		bountyDisabledMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.bountyDisabled"));

		noPermissionMessage = TextUtils.replace(noPermissionMessage,
		                                        new ParamSubst("permission", permission));
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
		Player p = null;

		if (sender instanceof Player) {
			p = (Player) sender;
			if (!p.hasPermission("piratebounties.bounties.set")) {
				p.sendMessage(PlaceholderAPI.setPlaceholders(p, noPermissionMessage));
				return true;
			}
		}
		String message;

		if (args.length < 2) {
			message = PlaceholderAPI.setPlaceholders(p,
			                                         TextUtils.replace(insufficientArgumentsMessage,
			                                                           new ParamSubst("given", args.length),
			                                                           new ParamSubst("needed", 2)));
		} else {
			Player t = Bukkit.getPlayer(args[0]);

			if (t == null) {
				message = PlaceholderAPI.setPlaceholders(p,
				                                         TextUtils.replace(noPlayerMessage,
				                                                           new ParamSubst("player", args[0])));
			} else {
				if (t.hasPermission("piratebounties.bounties.enabled")) {
					try {
						long newBounty = Long.parseLong(args[1]);
						BountyManager.setBounty(t, newBounty);
						message = PlaceholderAPI.setPlaceholders(t, okMessage);
					} catch (NumberFormatException e) {
						message = PlaceholderAPI.setPlaceholders(p,
						                                         TextUtils.replace(wrongTypeMessage,
						                                                           new ParamSubst("value", args[1]),
						                                                           new ParamSubst("type", "number")));
					}
				} else {
					message = PlaceholderAPI.setPlaceholders(t, bountyDisabledMessage);
				}
			}
		}

		if (p != null) {
			p.sendMessage(message);
		} else {
			Bukkit.getLogger().info(ChatColor.stripColor(message));
		}
		return true;
	}
}
