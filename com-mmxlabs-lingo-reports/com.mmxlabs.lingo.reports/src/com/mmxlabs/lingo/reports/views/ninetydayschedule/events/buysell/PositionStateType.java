/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import com.mmxlabs.lingo.reports.ColourPalette;

/**
 * Four main types of positions
 * In fact there are two differences - red and green,
 * but duplicate value enumerations were added for readability
 * @author AP
 *
 */
public enum PositionStateType {
	PAIRED_TO_SPOT(ColourPalette.getInstance().getColour(new RGB(128, 0, 0))),
	OPEN(ColourPalette.getInstance().getColour(new RGB(128, 0, 0))),
	PAIRED(ColourPalette.getInstance().getColour(new RGB(0, 128, 0))),
	OPTIONAL(ColourPalette.getInstance().getColour(new RGB(0, 128, 0)));

	private final Color colour;

	PositionStateType(final Color colour) {
		this.colour = colour;
	}

	public Color getColour() {
		return colour;
	}
}
