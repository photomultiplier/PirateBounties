// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.managers;

import org.bukkit.entity.Player;

/**
 * An utility class to cache player information.
 */
public class Emperor {
	/**
	 * The player's display name.
	 */
	public String displayName;
	/**
	 * The player's bounty.
	 */
	public int bounty;

	/**
	 * Constructs an Emperor from its name and bounty.
	 *
	 * @param newDisplayName The player's name.
	 * @param newBounty The player's bounty.
	 */
	public Emperor(String newDisplayName, int newBounty) {
		displayName = newDisplayName;
		bounty = newBounty;
	}

	/**
	 * Constructs an Emperor from a Player.
	 *
	 * @param player The player of interest.
	 */
	public Emperor(Player player) {
		displayName = player.getDisplayName();
		bounty = BountyManager.getBounty(player);
	}
}
