// Copyright (C) 2023 photomultiplier
// This program is licensed under the GNU General Public License.
// Detailed licensing information is available in the "LICENSE" file.

package com.github.photomultiplier.piratebounties.utils;

/**
 * Represents a parameter placeholder to be substituted.
 *
 * For use in formatting strings: <code>new ParamSubst("player",
 * "Jeb")</code> may represent replacing "%player%" in a string with
 * "Jeb".
 */
public class ParamSubst {
	/**
	 * The placeholder to be substituted, without any markers
	 * (so "player" and not "%player%").
	 */
	public String placeholder;
	/**
	 * The new value to be inserted.
	 */
	public String value;

	/**
	 * Creates a new substitution from a placeholder and a value.
	 *
	 * @param placeholder The placeholder to be substituted, without any
	 * markers (so "player" and not "%player%").
	 * @param value The new value to be inserted.
	 */
	public ParamSubst(String placeholder, Object value) {
		placeholder = placeholder;
		value = value.toString();
	}
}
