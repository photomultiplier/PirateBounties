// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.managers;

import java.util.ArrayList;
import java.util.List;

import com.github.photomultiplier.piratebounties.PirateBounties;
import com.github.photomultiplier.piratebounties.utils.ActionsGroup;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.milkbowl.vault.economy.EconomyResponse;

/**
 * Manages player bounties.
 *
 * It creates and maintains the "bounty" NBT tag.
 */
public abstract class BountyManager {
	static NamespacedKey key;
	static long bountyIncreaseAmount = 1l;
	static List<ActionsGroup> rewards;
	static List<Long> rewardLevels;

	/**
	 * Initializes the manager.
	 */
	public static void init() {
		Plugin pg = PirateBounties.getPlugin();
		FileConfiguration conf = pg.getConfig();

		key = new NamespacedKey(pg, "bounty");
		bountyIncreaseAmount = conf.getLong("general.bountyIncrease");
		rewardLevels = conf.getLongList("general.rewards");

		rewards = new ArrayList<ActionsGroup>();

		for (long i : rewardLevels) {
			rewards.add(new ActionsGroup(conf, String.format("%dReward", i)));
		}
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
			long oldBounty = pdc.get(key, PersistentDataType.LONG);
			pdc.set(key, PersistentDataType.LONG, newBounty);

			for (int i = 0; i < rewardLevels.size(); i++) {
				if (oldBounty < rewardLevels.get(i) && newBounty > rewardLevels.get(i)) {
					rewards.get(i).execute(p, (OfflinePlayer) p);
				}
			}

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
		long killedBounty = getBounty(killed);
		setBounty(killed, killedBounty / 2);
		setBounty(killer, getBounty(killer) + bountyIncreaseAmount);
		EconomyResponse r = PirateBounties.getEconomy().depositPlayer(killer, killedBounty - (killedBounty / 2));
		if(!r.transactionSuccess()) {
			Bukkit.getLogger().warning("A transaction has gone wrong!");
		}
	}
}
