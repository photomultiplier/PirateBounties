// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.commands;

import com.github.photomultiplier.piratebounties.PirateBounties;
import com.github.photomultiplier.piratebounties.managers.BountyManager;
import com.github.photomultiplier.piratebounties.managers.EmperorsManager;
import com.github.photomultiplier.piratebounties.utils.ParamSubst;
import com.github.photomultiplier.piratebounties.utils.TextUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * A command to set a player's bounty.
 */
public class SetBountyCommand implements CommandExecutor {
	String okMessage;
	String insufficientArgumentsMessage;
	String wrongTypeMessage;
	String noPlayerMessage;

	/**
	 * Initializes the command, loading responses from the config file.
	 */
	public SetBountyCommand() {
		FileConfiguration config = PirateBounties.getPlugin().getConfig();
		okMessage = TextUtils.parseMessageFromConfig(config.getStringList("setBountyCommand.messages.ok"));
		insufficientArgumentsMessage = TextUtils.parseMessageFromConfig(config.getStringList("commandsErrorMessages.insufficientArguments"));
		wrongTypeMessage = TextUtils.parseMessageFromConfig(config.getStringList("commandsErrorMessages.wrongType"));
		noPlayerMessage = TextUtils.parseMessageFromConfig(config.getStringList("commandsErrorMessages.noPlayer"));
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
		String message;

		if (args.length < 2) {
			message = TextUtils.replace(insufficientArgumentsMessage,
			                            new ParamSubst("given", args.length),
			                            new ParamSubst("needed", 2));
		} else {
			Player t = Bukkit.getPlayer(args[0]);

			if (t == null) {
				message = TextUtils.replace(noPlayerMessage,
				                            new ParamSubst("player", args[0]));
			} else {
				try {
					int newBounty = Integer.parseInt(args[1]);
					BountyManager.setBounty(t, newBounty);
					EmperorsManager.updateSingle(t);
					message = TextUtils.replace(okMessage,
					                            new ParamSubst("player", t.getDisplayName()),
					                            new ParamSubst("bounty", newBounty));
				} catch (NumberFormatException e) {
					message = TextUtils.replace(wrongTypeMessage,
					                            new ParamSubst("value", args[1]),
					                            new ParamSubst("type", "number"));
				}
			}
		}

		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(message);
		} else {
			System.out.println(ChatColor.stripColor(message));
		}
		return true;
	}
}
