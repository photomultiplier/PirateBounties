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
	static int emperorTotal = 1;
	static boolean recalculateIfVacant = false;
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
		emperorTotal = config.getInt("general.emperorTotal");

		if (emperorTotal < emperorAmount) {
			Bukkit.getLogger().warning("The total number of emperors must be greater than the amount of emperors!");
			emperorTotal = emperorAmount;
		}

		recalculateIfVacant = config.getBoolean("general.recalculateLeaderboardIfVacant");
		leaderboardEnter = new ActionsGroup(config, "leaderboardEnter");
		leaderboardLeave = new ActionsGroup(config, "leaderboardLeave");

		File dataFolder = pg.getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		dataFile = new File(dataFolder, config.getString("files.emperorsFile"));

		leaderBoard = EmperorLeaderboard.loadData(dataFile.getAbsolutePath());

		if (leaderBoard == null) {
			leaderBoard = new EmperorLeaderboard(emperorTotal);
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

		for (int i=0; i < leaderBoard.l.length; i++) {
			Emperor nextEmperor;

			if (i+1 >= leaderBoard.l.length) {
				nextEmperor = null;
			} else {
				nextEmperor = leaderBoard.l[i+1];
			}

			if (seen || (leaderBoard.l[i] != null && leaderBoard.l[i]
			             .offlinePlayer.getUniqueId().equals(og.offlinePlayer.getUniqueId()))) {
				leaderBoard.l[i] = nextEmperor;
				nextEmperor = null;
				seen = true;
			}

			if (leaderBoard.l[i] != null) {
				if (newEmperor != null && newEmperor.bounty >= leaderBoard.l[i].bounty) {
					Emperor buffer = newEmperor;
					newEmperor = leaderBoard.l[i];
					leaderBoard.l[i] = buffer;
				}
			} else {
				if (newEmperor != null && newEmperor.bounty > emperorThreshold) {
					leaderBoard.l[i] = newEmperor;
					newEmperor = null;
				}
			}

			// This is what could have happened:
			// newEmperor not added (0), list unchanged
			// newEmperor added (+)
			//   someone at the end may have been dropped (+-)
			// newEmperor dropped (-)
			//   someone at the end may have been added (-+)
			// newEmperor moved inside the list (~)

			if (i == emperorAmount-1 || (i < emperorAmount && leaderBoard.l[i] == null)) {
				if (newEmperor != null) {
					// Someone dropped
					// 0, +-, -, -+
					if (newEmperor.offlinePlayer.getUniqueId().equals(og.offlinePlayer.getUniqueId())) {
						// newEmperor dropped
						// 0, -, -+
						if (seen) {
							// newEmperor was on leaderboard
							// -, -+

							if (leaderBoard.l[emperorAmount-1] != null) {
								// Someone at the end added
								// -+
								leaderboardLeave.execute(og.getUpdatePlayer(), og.offlinePlayer);
								leaderboardEnter.execute(leaderBoard.l[emperorAmount-1].getUpdatePlayer(),
								                         leaderBoard.l[emperorAmount-1].offlinePlayer);
							} else {
								// No one was added
								// -
								leaderboardLeave.execute(og.getUpdatePlayer(), og.offlinePlayer);
								// This means that a place on the leaderboard is left vacant!
								// This could be correct (e.g. if there aren't enough player to fill the
								// leaderboard), but it could also mean that an emperor was kicked off the list
								// and forgotten.
								// The quick solution for said emperor is just to reconnect to the server, but
								// the real fix would be recalculating the whole leaderboard when this happens.
								// That said, we'll let the server owner decide what to do.
								if (recalculateIfVacant) {
									update();
								}
							}
						} else {
							// newEmperor wasn't on leaderboard
							// 0
						}
					} else {
						// newEmperor else dropped
						// +-
								leaderboardEnter.execute(og.getUpdatePlayer(), og.offlinePlayer);
								leaderboardLeave.execute(newEmperor.getUpdatePlayer(), newEmperor.offlinePlayer);
					}
				} else {
					// No drops
					// +, ~
					if (seen) {
						// newEmperor was on leaderboard
						// ~
					} else {
						// newEmperor wasn't on leaderboard
						// +
						leaderboardEnter.execute(og.getUpdatePlayer(), og.offlinePlayer);
					}
				}
			}

			if (leaderBoard.l[i] == null) {
				// As the list can't have holes in this point of the loop,
				// and as we've already warned everyone, we can safely
				// break out of the loop.
				break;
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
			if (leaderBoard.l[i] != null && uuid.equals(leaderBoard.l[i].offlinePlayer.getUniqueId())) {
				leaderboardLeave.execute(leaderBoard.l[i].getUpdatePlayer(),
				                         leaderBoard.l[i].offlinePlayer);
				seen = true;
			}

			if (seen) {
				if (i + 1 > leaderBoard.l.length) {
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
		for (int i = 0; i < leaderBoard.l.length; i++) {
			if (leaderBoard.l[i] == null) {
				break;
			}

			leaderboardLeave.execute(leaderBoard.l[i].getUpdatePlayer(),
			                         leaderBoard.l[i].offlinePlayer);
			leaderBoard.l[i] = null;
		}
	}
}
