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

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class of the plugin.
 *
 * For now it just prints "Hello, world!".
 */
public class PirateBounties extends JavaPlugin {
	/**
	 * Method called on plugin initialization.
	 */
	@Override
	public void onEnable() {
		System.out.println("PirateBounties loading...");
		System.out.println("Hello, world!");
		System.out.println("PirateBounties loaded.");
	}
}
