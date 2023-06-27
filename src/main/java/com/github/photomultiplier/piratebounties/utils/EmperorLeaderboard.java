// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

/**
 * Serializable representation of the leaderboard.
 */
public class EmperorLeaderboard implements Serializable {
	/**
	 * The actual leaderboard, a list of emperors.
	 */
	public Emperor[] l;

	/**
	 * Constructs a new leaderboard.
	 *
	 * @param emperors How many emperors should the leaderboard fit.
	 */
	public EmperorLeaderboard(int emperors) {
		l = new Emperor[emperors];
	}

	/**
	 * Serializes and saves the leaderboard to a compressed file.
	 *
	 * @param filePath The name of the file.
	 * @return true on success.
	 */
	public boolean saveData(String filePath) {
		try {
			BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
			out.writeObject(this);
			out.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Loads and de-serializes the leaderboard from a compressed file.
	 *
	 * @param filePath The name of the file.
	 * @return The de-serialized class or null on failure.
	 */
	public static EmperorLeaderboard loadData(String filePath) {
		try {
			BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
			EmperorLeaderboard data = (EmperorLeaderboard) in.readObject();
			in.close();
			return data;
		} catch (ClassNotFoundException | IOException e) {
			return null;
		}
	}
}
