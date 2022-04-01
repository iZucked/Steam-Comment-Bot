/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

import org.eclipse.swt.graphics.Color;

public class LegendItemImpl implements ILegendItem {
	private final String description;
	private final Color[] colours;

	public LegendItemImpl(final String description, final Color... colours) {
		this.description = description;
		this.colours = colours;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Color[] getColours() {
		return colours;
	}
}