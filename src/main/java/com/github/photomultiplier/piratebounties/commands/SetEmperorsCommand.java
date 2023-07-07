// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.commands;

import com.github.photomultiplier.piratebounties.PirateBounties;
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
 * A command to modify the list of emperors.
 */
public class SetEmperorsCommand implements CommandExecutor {
	String okMessage;
	String noPermissionMessage;
	String insufficientArgumentsMessage;
	String wrongSyntaxMessage;
	String noPlayerMessage;

	/**
	 * Initializes the command, loading responses from the config file.
	 */
	public SetEmperorsCommand() {
		FileConfiguration config = PirateBounties.getPlugin().getConfig();
		okMessage = TextUtils.msgFromConfig(config.getStringList("setEmperorsCommand.messages.ok"));
		noPermissionMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.noPermission"));
		insufficientArgumentsMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.insufficientArguments"));
		wrongSyntaxMessage = TextUtils.msgFromConfig(config.getStringList("commandsErrorMessages.wrongSyntax"));
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
		Player p = null;

		if (sender instanceof Player) {
			p = (Player) sender;
			if (!p.hasPermission("piratebounties.emperors.set")) {
				p.sendMessage(TextUtils.replace(noPermissionMessage,
				                                new ParamSubst("player", p.getDisplayName()),
				                                new ParamSubst("permission", "piratebounties.emperors.set")));
				return true;
			}
		}

		String message;

		if (args.length < 1) {
			message = TextUtils.replace(insufficientArgumentsMessage,
			                            new ParamSubst("given", args.length),
			                            new ParamSubst("needed", 2));
		} else if (args.length < 2) {
			if (args[0].equals("update")) {
				EmperorsManager.update();
				message = okMessage;
			} else if (args[0].equals("clear")) {
				EmperorsManager.clear();
				message = okMessage;
			} else {
				message = wrongSyntaxMessage;
			}
		} else {
			Player t = Bukkit.getPlayer(args[1]);

			if (t == null) {
				message = TextUtils.replace(noPlayerMessage,
				                            new ParamSubst("player", args[1]));
			} else {
				if (args[0].equals("update")) {
					EmperorsManager.updateSingle(t);
					message = okMessage;
				} else if (args[0].equals("remove")) {
					EmperorsManager.remove(t);
					message = okMessage;
				} else {
					message = wrongSyntaxMessage;
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
