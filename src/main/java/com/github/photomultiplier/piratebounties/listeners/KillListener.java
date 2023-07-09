// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.listeners;

import com.github.photomultiplier.piratebounties.managers.BountyManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Listens for PVP kills.
 */
public class KillListener implements Listener {
	/**
	 * The actual listener.
	 *
	 * @param e The event. The listener then checks if two players were involved and if one died.
	 */
	@EventHandler
	public void onKill(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player killed = (Player) e.getEntity();
			Player killer = (Player) e.getDamager();
			if (killed.getHealth() - e.getFinalDamage() <= 0.) {
				BountyManager.registerKill(killed, killer);
			}
		}
	}
}
