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
public class BountyManager {
	static NamespacedKey key;

	/**
	 * Initializes the manager.
	 *
	 * @param pg A reference to the plugin.  Used to create the NamespacedKey.
	 */
	public static void init(Plugin pg) {
		key = new NamespacedKey(pg, "bounty");
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
}
