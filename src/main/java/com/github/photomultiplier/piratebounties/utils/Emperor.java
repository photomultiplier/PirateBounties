// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

import java.io.Serializable;

import com.github.photomultiplier.piratebounties.managers.BountyManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * An utility class to cache player information.
 */
public class Emperor implements Serializable {
	/**
	 * The player.
	 */
	transient private Player player;
	/**
	 * The offline player.
	 */
	public OfflinePlayer offlinePlayer;
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
	 * @param player The player.
	 */
	public Emperor(Player player) {
		this.player = player;
		this.offlinePlayer = (OfflinePlayer) player;
		this.displayName = player.getDisplayName();
		this.bounty = BountyManager.getBounty(player);
	}

	/**
	 * Getter for player, trying to update it if it is null.
	 *
	 * @return The player on success, null otherwise.
	 */
	public Player getUpdatePlayer() {
		if (player == null && offlinePlayer.isOnline()) {
			player = offlinePlayer.getPlayer();
			displayName = player.getDisplayName();
			bounty = BountyManager.getBounty(player);
		}
		return player;
	}
}
