// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

import java.util.List;

import org.bukkit.ChatColor;

/**
 * Contains various utilities related to text.
 */
public abstract class TextUtils {
	/**
	 * Constructs a message string from a list of user friendly lines
	 * containing minecraft formatting codes like '&amp;c' for red and '&amp;l'
	 * for bold.
	 *
	 * @param message A list of strings where each item is regarded as a line of the message.
	 * @return The parsed message.
	 * @see <a href="https://minecraft.fandom.com/wiki/Formatting_codes">formatting codes</a>
	 */
	public static String msgFromConfig(List<String> message) {
		if (message == null || message.size() == 0) {
			return null;
		} else {
			String joinedMessage = ChatColor.translateAlternateColorCodes('&', String.join("\n", message));
			return joinedMessage;
		}
	}

	/**
	 * Replaces placeholders in a message.
	 *
	 * @param message The message to be parsed.
	 * @param substitutions The list of substitutions.
	 * @return The parsed message.
	 */
	public static String replace(String message, ParamSubst... substitutions) {
		for (ParamSubst ps : substitutions) {
			message = message.replaceAll("%" + ps.placeholder + "%", ps.value);
		}
		return message;
	}
}
