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

/**
 * A command to retrieve a player's bounty ingame.
 */
public class BountyCommand implements CommandExecutor {
	String selfMessage;
	String otherMessage;
	String noPlayerMessage;

	/**
	 * Initializes the command, loading responses from the config file.
	 */
	public BountyCommand() {
		FileConfiguration config = PirateBounties.getPlugin().getConfig();
		selfMessage = TextUtils.msgFromConfig(config.getStringList("bountyCommand.messages.self"));
		otherMessage = TextUtils.msgFromConfig(config.getStringList("bountyCommand.messages.other"));
		noPlayerMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.noPlayer"));
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
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.sendMessage(TextUtils.replace(selfMessage,
				                                new ParamSubst("player", p.getDisplayName()),
				                                new ParamSubst("bounty", BountyManager.getBounty(p))));
			}
		} else {
			Player t = Bukkit.getPlayer(args[0]);

			String message;

			if (t == null) {
				message = TextUtils.replace(noPlayerMessage,
				                            new ParamSubst("player", args[0]));
			} else {
				message = TextUtils.replace(otherMessage,
				                            new ParamSubst("player", t.getDisplayName()),
				                            new ParamSubst("bounty", BountyManager.getBounty(t)));
			}

			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.sendMessage(message);
			} else {
				System.out.println(ChatColor.stripColor(message));
			}
		}
		return true;
	}
}
