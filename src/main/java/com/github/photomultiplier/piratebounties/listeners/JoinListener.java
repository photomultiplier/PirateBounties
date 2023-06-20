// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.listeners;

import com.github.photomultiplier.piratebounties.managers.EmperorsManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Listens for player joins.
 */
public class JoinListener implements Listener {
	/**
	 * The actual listener.
	 *
	 * @param e The event.
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		EmperorsManager.updateSingle(e.getPlayer());
	}
}
