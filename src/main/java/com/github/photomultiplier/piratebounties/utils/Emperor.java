// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

import com.github.photomultiplier.piratebounties.managers.BountyManager;

import java.io.Serializable;
import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * An utility class to cache player information.
 */
public class Emperor implements Serializable {
	/**
	 * The player's UUID.
	 */
	public UUID uuid;
	/**
	 * The player's display name.
	 */
	public String displayName;
	/**
	 * The player's bounty.
	 */
	public int bounty;

	/**
	 * Constructs an Emperor from its identifier, name and bounty.
	 *
	 * @param newUUID The player's unique identifier.
	 * @param newDisplayName The player's name.
	 * @param newBounty The player's bounty.
	 */
	public Emperor(UUID newUUID, String newDisplayName, int newBounty) {
		uuid = newUUID;
		displayName = newDisplayName;
		bounty = newBounty;
	}

	/**
	 * Constructs an Emperor from a Player.
	 *
	 * @param player The player of interest.
	 */
	public Emperor(Player player) {
		uuid = player.getUniqueId();
		displayName = player.getDisplayName();
		bounty = BountyManager.getBounty(player);
	}
}
