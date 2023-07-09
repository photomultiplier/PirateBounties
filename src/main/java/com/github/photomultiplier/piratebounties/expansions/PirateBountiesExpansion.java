// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.expansions;

import com.github.photomultiplier.piratebounties.PirateBounties;
import com.github.photomultiplier.piratebounties.managers.BountyManager;
import com.github.photomultiplier.piratebounties.managers.EmperorsManager;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

/**
 * The {@link me.clip.placeholderapi.PlaceholderAPI} expansion for
 * PirateBounties.
 *
 * @see <a href="https://www.spigotmc.org/resources/placeholderapi.6245/">Placeholder API</a>
 */
public class PirateBountiesExpansion extends PlaceholderExpansion {
	/**
	 * Returns the identifier of the expansion.
	 *
	 * @return "piratebounties"
	 */
	@Override
	public String getIdentifier() {
		return "piratebounties";
	}

	/**
	 * Returns the author of the expansion.
	 *
	 * @return "photomultiplier" aka me! :)
	 */
	@Override
	public String getAuthor() {
		return "photomultiplier";
	}

	/**
	 * Returns the version of the expansion.
	 *
	 * @return the version, retrieved from plugin.yml.
	 */
	@Override
	public String getVersion() {
		return PirateBounties.getPlugin().getDescription().getVersion();
	}

	/**
	 * Tells PlaceholderAPI to persist the expansion on <code>/reload</code>.
	 *
	 * @return true
	 */
	@Override
	public boolean persist() {
		return true;
	}

	/**
	 * Fetches the value of a placeholder.
	 *
	 * The actual placeholders available are documented in this plugin's
	 * README.
	 *
	 * @param player The player referred to by the placeholders.  May be
	 * null.
	 * @param params The second part of the placeholder, after the
	 * identifier and the underscore (structure of a placeholder:
	 * %piratebounties_&lt;params&gt;%)
	 * @return The value of the placeholder.
	 */
	@Override
	public String onPlaceholderRequest(Player player, String params) {
		if (params.equalsIgnoreCase("emperors_thr")) {
			return Long.toString(EmperorsManager.getEmperorThreshold());
		} else if (params.equalsIgnoreCase("emperors_amount")) {
			return Integer.toString(EmperorsManager.getEmperorAmount());
		}

		if (player == null) {
			return null;
		}

		if (params.equalsIgnoreCase("bounty")) {
			return Long.toString(BountyManager.getBounty(player));
		}

		return null;
	}
}
