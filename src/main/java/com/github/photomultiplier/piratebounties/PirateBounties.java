/* PirateBounties, a Spigot plugin adding bounties to minecraft.
 * Copyright (C) 2023 photomultiplier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.photomultiplier.piratebounties;

import com.github.photomultiplier.piratebounties.commands.BountyCommand;
import com.github.photomultiplier.piratebounties.commands.EmperorsCommand;
import com.github.photomultiplier.piratebounties.commands.SetBountyCommand;
import com.github.photomultiplier.piratebounties.commands.SetEmperorsCommand;
import com.github.photomultiplier.piratebounties.expansions.PirateBountiesExpansion;
import com.github.photomultiplier.piratebounties.listeners.JoinListener;
import com.github.photomultiplier.piratebounties.listeners.KillListener;
import com.github.photomultiplier.piratebounties.managers.BountyManager;
import com.github.photomultiplier.piratebounties.managers.EmperorsManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the plugin.
 *
 * Initializes managers and registers commands and listeners.
 */
public class PirateBounties extends JavaPlugin {
	private static JavaPlugin plugin;

	/**
	 * Method called on plugin initialization.
	 */
	@Override
	public void onEnable() {
		plugin = this;

		Bukkit.getLogger().info("PirateBounties loading...");
		saveDefaultConfig();

		// Managers
		BountyManager.init();
		EmperorsManager.init();

		// Commands
		getCommand("bounty").setExecutor(new BountyCommand());
		getCommand("setbounty").setExecutor(new SetBountyCommand());
		getCommand("emperors").setExecutor(new EmperorsCommand());
		getCommand("setemperors").setExecutor(new SetEmperorsCommand());

		// Listeners
		getServer().getPluginManager().registerEvents(new KillListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);

		// PlaceholderAPI extension
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PirateBountiesExpansion().register();
		}

		Bukkit.getLogger().info("PirateBounties loaded.");
	}

	/**
	 * Method called on plugin de-initialization.
	 */
	@Override
	public void onDisable() {
		// Managers
		EmperorsManager.deInit();
	}

	/**
	 * Return a pointer to the plugin instance.
	 *
	 * @return The instance.
	 */
	public static JavaPlugin getPlugin() {
		return plugin;
	}
}
