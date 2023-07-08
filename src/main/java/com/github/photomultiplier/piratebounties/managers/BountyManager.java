// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.managers;

import com.github.photomultiplier.piratebounties.PirateBounties;

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
	static long bountyIncreaseAmount = 1l;

	/**
	 * Initializes the manager.
	 */
	public static void init() {
		Plugin pg = PirateBounties.getPlugin();
		key = new NamespacedKey(pg, "bounty");
		bountyIncreaseAmount = pg.getConfig().getLong("general.bountyIncrease");
	}

	/**
	 * Retrieves a player's bounty.
	 *
	 * @param p The player of interest.
	 * @return The player's bounty.
	 */
	public static long getBounty(Player p) {
		if (key == null) {
			return 0l;
		} else {
			if (!p.hasPermission("piratebounties.bounties.enabled")) {
				return 0l;
			}

			PersistentDataContainer pdc = p.getPersistentDataContainer();
			if (pdc.has(key, PersistentDataType.LONG)) {
				return pdc.get(key, PersistentDataType.LONG);
			} else {
				pdc.set(key, PersistentDataType.LONG, 0l);
				return 0l;
			}
		}
	}

	/**
	 * Sets a player's bounty.
	 *
	 * @param p The player of interest.
	 * @param newBounty The new bounty to set.
	 */
	public static void setBounty(Player p, long newBounty) {
		if (key != null) {
			if (!p.hasPermission("piratebounties.bounties.enabled")) {
				return;
			}

			PersistentDataContainer pdc = p.getPersistentDataContainer();
			pdc.set(key, PersistentDataType.LONG, newBounty);
			EmperorsManager.updateSingle(p);
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
