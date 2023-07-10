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
	public long bounty;

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
