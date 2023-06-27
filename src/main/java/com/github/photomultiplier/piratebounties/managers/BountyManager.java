// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * Manages player bounties.
 *
 * It creates and maintains the "bounty" NBT tag.
 */
public abstract class BountyManager {
	static NamespacedKey key;
	static int bountyIncreaseAmount = 1;

	/**
	 * Initializes the manager.
	 *
	 * @param pg A reference to the plugin.  Used to create the NamespacedKey.
	 */
	public static void init(Plugin pg) {
		key = new NamespacedKey(pg, "bounty");
		bountyIncreaseAmount = pg.getConfig().getInt("bountyIncreaseAmount");
	}

	/**
	 * Retrieves a player's bounty.
	 *
	 * @param p The player of interest.
	 * @return The player's bounty.
	 */
	public static int getBounty(Player p) {
		if (key == null) {
			return 0;
		} else {
			PersistentDataContainer pdc = p.getPersistentDataContainer();
			if (pdc.has(key, PersistentDataType.INTEGER)) {
				return pdc.get(key, PersistentDataType.INTEGER);
			} else {
				pdc.set(key, PersistentDataType.INTEGER, 0);
				return 0;
			}
		}
	}

	/**
	 * Sets a player's bounty.
	 *
	 * @param p The player of interest.
	 * @param newBounty The new bounty to set.
	 */
	private static void setBounty(Player p, int newBounty) {
		if (key != null) {
			PersistentDataContainer pdc = p.getPersistentDataContainer();
			pdc.set(key, PersistentDataType.INTEGER, newBounty);
		}
	}

	/**
	 * Take note of a PVP kill.
	 *
	 * @param killed The player who died.
	 * @param killer The player who killed.
	 */
	public static void registerKill(Player killed, Player killer) {
		setBounty(killed, getBounty(killed) / 2);
		setBounty(killer, getBounty(killer) + bountyIncreaseAmount);
	}
}
