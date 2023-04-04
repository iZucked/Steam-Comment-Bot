/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class LightPastelColourSequence {

	private LightPastelColourSequence() {
	}

	private static final int[] sequence = { //
		0xaccce8, //
		0xb5e1ee, //
		0xcaeeed, //
		0xa8e0d0, //
		0x58c7b3, //
		0xb0afe6, //
		0xbdccf2, //
		0xf4bebe, //
		0xffdec7, //
		0xfaf0dc, //
		0xebc9f2, //
	};

	@SuppressWarnings("null")
	public static @NonNull Color[] createColourSequence(@NonNull final LocalResourceManager localResourceManager) {
		final Color[] initialisedSequence = new Color[sequence.length];
		for (int i = 0; i < sequence.length; ++i) {
			final int currentCol = sequence[i];
			final int r = (currentCol & 0xFF0000) >> 16;
			final int g = (currentCol & 0x00FF00) >> 8;
			final int b = (currentCol & 0x0000FF);
			initialisedSequence[i] = localResourceManager.createColor(new RGB(r, g, b));
		}
		return initialisedSequence;
	}
}
