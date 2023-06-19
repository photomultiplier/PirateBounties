// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.commands;

import com.github.photomultiplier.piratebounties.managers.BountyManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A command to retrieve a player's bounty ingame.
 */
public class BountyCommand implements CommandExecutor {
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
			if (sender instanceof Player p) {
				p.sendMessage("You have a bounty of " + ChatColor.YELLOW + ChatColor.BOLD + "$" + BountyManager.getBounty(p));
			}
		} else {
			Player t = Bukkit.getPlayer(args[0]);

			if (sender instanceof Player p) {
				if (t == null) {
					p.sendMessage(ChatColor.RED + "The player " + ChatColor.BOLD + args[0] + ChatColor.RED + " does not exist!");
				} else {
					p.sendMessage(ChatColor.YELLOW + t.getDisplayName() + ChatColor.RESET + " has a bounty of " + ChatColor.YELLOW
							+ ChatColor.BOLD + "$" + BountyManager.getBounty(t));
				}
			} else {
				if (t == null) {
					System.out.println("The player " + args[0] + " does not exist");
				} else {
					System.out.println(t.getDisplayName() + " has a bounty of $" + BountyManager.getBounty(t));
				}
			}
		}
		return true;
	}
}
