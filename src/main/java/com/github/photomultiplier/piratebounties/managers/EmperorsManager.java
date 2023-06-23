// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.managers;

import java.util.Collection;

import com.github.photomultiplier.piratebounties.PirateBounties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Manages the emperors leaderboard.
 *
 * The leaderboard is cached and recomputed every two hours.
 */
public abstract class EmperorsManager {
	static BukkitScheduler scheduler = Bukkit.getScheduler();
	static int emperorThreshold = 1;
	static int emperorAmount = 1;
	static Emperor[] leaderBoard = new Emperor[4];

	/**
	 * Returns the minimum bounty needed to become an emperor.
	 *
	 * @return The minimum bounty.
	 */
	public static int getEmperorThreshold() {
		return emperorThreshold;
	}

	/**
	 * Returns the total amount of emperors.
	 *
	 * @return The amount of emperors.
	 */
	public static int getEmperorAmount() {
		return emperorAmount;
	}

	/**
	 * Returns the leaderboard.
	 *
	 * @return The leaderboard.
	 */
	public static Emperor[] getLeaderBoard() {
		return leaderBoard;
	}

	/**
	 * Initializes the manager.
	 */
	public static void init() {
		Plugin pg = PirateBounties.getPlugin();
		FileConfiguration config = pg.getConfig();
		emperorThreshold = config.getInt("emperorThreshold");
		emperorAmount = config.getInt("emperorAmount");

		scheduler.runTaskTimer(pg, () -> update(), 20L, 20L * 60L * 60L * 2L);
	}

	/**
	 * Updates the whole leaderboard.
	 */
	public static void update() {
		leaderBoard = new Emperor[emperorAmount];
		@SuppressWarnings("unchecked")
		Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();

		Emperor newEmperor;

		for (Player player : players) {
			newEmperor = new Emperor(player);
			if (newEmperor.bounty >= emperorThreshold) {
				updateSingle(newEmperor);
			}
		}
	}

	/**
	 * Updates a single emperor.
	 *
	 * @param newEmperor The emperor to be updated.
	 */
	public static void updateSingle(Emperor newEmperor) {
		String originalName = newEmperor.displayName;
		boolean seen = false;

		for (int i = 0; i < leaderBoard.length; i++) {
			if (seen || (leaderBoard[i] != null && originalName.equals(leaderBoard[i].displayName))) {
				if (i + 1 > emperorAmount) {
					leaderBoard[i] = null;
				} else {
					leaderBoard[i] = leaderBoard[i + 1];
				}
				seen = true;
			}

			if (leaderBoard[i] == null) {
				if (newEmperor.bounty >= emperorThreshold) {
					leaderBoard[i] = newEmperor;
				}
				break;
			} else {
				if (newEmperor.bounty >= leaderBoard[i].bounty) {
					Emperor buffer = leaderBoard[i];
					leaderBoard[i] = newEmperor;
					newEmperor = buffer;
				}
			}
		}
	}

	/**
	 * Updates a single emperor.
	 *
	 * @param player The emperor to be updated, as a player.
	 */
	public static void updateSingle(Player player) {
		updateSingle(new Emperor(player));
	}
}
