/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * A collection of colours that are fairly different from each other and useful
 * for charts.
 * 
 * @author miten
 *
 */
public class ColourSequence {

	private ColourSequence() {
	}

	private static final int[] sequence = { 0xe6194b, 0x3cb44b, 0xffe119, 0x4363d8, 0xf58231, 0x911eb4, 0x42d4f4, 0xf032e6, 0xbfef45, 0xfabed4, 0x469990, 0xdcbeff, 0x9A6324, 0xfffac8, 0x800000,
			0xaaffc3, 0x808000, 0xffd8b1, 0x000075, 0xa9a9a9, 0x000000 };

	/**
	 * @param localResourceManager
	 * @return
	 * 
	 *         Uses the local resource manager to create colours that are returned
	 *         in the array.
	 */
	@SuppressWarnings("null")
	public static @NonNull Color[] createColourSequence(@NonNull final LocalResourceManager localResourceManager) {
		final Color[] initialisedSequence = new Color[sequence.length];
		for (int i = 0; i < sequence.length; ++i) {
			final int currentCol = sequence[i];
			final int r = (currentCol & 0xFF0000) >> 16;
			final int g = (currentCol & 0xFF00) >> 8;
			final int b = (currentCol & 0xFF);
			initialisedSequence[i] = localResourceManager.createColor(new RGB(r, g, b));
		}
		return initialisedSequence;
	}
}
