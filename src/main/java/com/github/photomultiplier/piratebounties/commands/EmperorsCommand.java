// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.commands;

import com.github.photomultiplier.piratebounties.managers.Emperor;
import com.github.photomultiplier.piratebounties.managers.EmperorsManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A command to retrieve the list of emperors.
 */
public class EmperorsCommand implements CommandExecutor {
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

		if (leaderBoard[0] == null) {
			if (sender instanceof Player p) {
				p.sendMessage(ChatColor.YELLOW + "There are no emperors!");
				p.sendMessage("Earn at least " + ChatColor.YELLOW + ChatColor.BOLD + "$" + EmperorsManager.getEmperorThreshold()
						+ ChatColor.RESET
						+ " to become one!");
			} else {
				System.out.println("There are no emperors!");
			}
		} else {
			if (sender instanceof Player p) {
				p.sendMessage("The emperors are:");
			}
			for (int i = 0; i < leaderBoard.length; i++) {
				Emperor emperor = leaderBoard[i];

				if (emperor == null) {
					break;
				} else {
					if (sender instanceof Player p) {
						p.sendMessage((i + 1) + ". " + ChatColor.YELLOW + emperor.displayName + ChatColor.RESET + ", "
								+ ChatColor.YELLOW + ChatColor.BOLD + "$" + emperor.bounty);
					} else {
						System.out.println((i + 1) + ". " + emperor.displayName + ", $" + emperor.bounty);
					}
				}
			}
		}
		return true;
	}
}
