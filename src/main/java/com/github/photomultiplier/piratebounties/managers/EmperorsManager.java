// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.managers;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

import com.github.photomultiplier.piratebounties.PirateBounties;
import com.github.photomultiplier.piratebounties.utils.ActionsGroup;
import com.github.photomultiplier.piratebounties.utils.Emperor;
import com.github.photomultiplier.piratebounties.utils.EmperorLeaderboard;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Manages the emperors leaderboard.
 *
 * The leaderboard is cached and saved between plugin reloads.
 */
public abstract class EmperorsManager {
	static BukkitScheduler scheduler = Bukkit.getScheduler();
	static long emperorThreshold = 1l;
	static int emperorAmount = 1;
	static File dataFile;
	static EmperorLeaderboard leaderBoard = new EmperorLeaderboard(4);
	static ActionsGroup leaderboardEnter;
	static ActionsGroup leaderboardLeave;

	/**
	 * Returns the minimum bounty needed to become an emperor.
	 *
	 * @return The minimum bounty.
	 */
	public static long getEmperorThreshold() {
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
		return leaderBoard.l;
	}

	/**
	 * Initializes the manager.
	 */
	public static void init() {
		Plugin pg = PirateBounties.getPlugin();
		FileConfiguration config = pg.getConfig();
		emperorThreshold = config.getLong("general.emperorThreshold");
		emperorAmount = config.getInt("general.emperorAmount");
		leaderboardEnter = new ActionsGroup(config, "leaderboardEnter");
		leaderboardLeave = new ActionsGroup(config, "leaderboardLeave");

		File dataFolder = pg.getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		dataFile = new File(dataFolder, config.getString("files.emperorsFile"));

		leaderBoard = EmperorLeaderboard.loadData(dataFile.getAbsolutePath());

		if (leaderBoard == null) {
			leaderBoard = new EmperorLeaderboard(emperorAmount);
		}
	}

	/**
	 * De-initializes the manager.
	 */
	public static void deInit() {
		if (!leaderBoard.saveData(dataFile.getAbsolutePath())) {
			Bukkit.getLogger().severe("Couldn't save the emperors leaderboard!");
		}
	}

	/**
	 * Updates the whole leaderboard.
	 */
	public static void update() {
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
		Emperor og = newEmperor;
		boolean seen = false;

		// Four things may happen:
		// 1. newEmperor gets added and the last emperor is removed as
		// there wasn't space on the leaderboard
		// 2. newEmperor gets added but no one is removed as there was
		// space on the leaderboard
		// 3. newEmperor gets removed
		// 4. No one gets added or removed

		for (int i = 0; i < leaderBoard.l.length; i++) {
			if (seen || (leaderBoard.l[i] != null && og.uuid.equals(leaderBoard.l[i].uuid))) {
				if (i + 1 > emperorAmount) {
					leaderBoard.l[i] = null;
				} else {
					leaderBoard.l[i] = leaderBoard.l[i + 1];
				}
				seen = true;
			}

			if (leaderBoard.l[i] == null) {
				if (newEmperor.bounty >= emperorThreshold) {
					leaderBoard.l[i] = newEmperor;
					newEmperor = null;
				}
				break;
			} else {
				if (newEmperor.bounty >= leaderBoard.l[i].bounty) {
					if (seen) {
						leaderBoard.l[i] = newEmperor;
						newEmperor = null;
						break;
					} else {
						Emperor buffer = leaderBoard.l[i];
						leaderBoard.l[i] = newEmperor;
						newEmperor = buffer;
						if (newEmperor == null) {
							break;
						}
					}
				}
			}
		}

		if (seen) {
			// Surely it hasn't been added
			// Cases 3 and 4
			if (newEmperor != null) {
				// Case 3
				leaderboardLeave.execute(og.getUpdatePlayer());
			} // else case 4, nothing to do
		} else {
			// Surely it hasn't been removed
			// Cases 1, 2 and 4
			if (newEmperor == null) {
				// Surely it was added and surely everyone fit
				// Case 2
				leaderboardEnter.execute(og.getUpdatePlayer());
			} else {
				// Cases 1 and 4
				if (!newEmperor.uuid.equals(og.uuid)) {
					// Case 1
					leaderboardEnter.execute(og.getUpdatePlayer());
					leaderboardLeave.execute(newEmperor.getUpdatePlayer());
				} // else case 4, nothing to do
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

	/**
	 * Removes an emperor from the leaderboard.
	 *
	 * @param uuid The emperor's unique identifier.
	 */
	public static void remove(UUID uuid) {
		boolean seen = false;

		for (int i = 0; i < leaderBoard.l.length; i++) {
			if (seen || (leaderBoard.l[i] != null && uuid.equals(leaderBoard.l[i].uuid))) {
				if (i + 1 > emperorAmount) {
					leaderBoard.l[i] = null;
				} else {
					leaderBoard.l[i] = leaderBoard.l[i + 1];
				}
				seen = true;
			}

			if (leaderBoard.l[i] == null) {
				break;
			}
		}
	}

	/**
	 * Removes an emperor from the leaderboard.
	 *
	 * @param player The player to be removed.
	 */
	public static void remove(Player player) {
		remove(player.getUniqueId());
	}

	/**
	 * Empties the leaderboard.
	 */
	public static void clear() {
		leaderBoard = new EmperorLeaderboard(emperorAmount);
	}
}
