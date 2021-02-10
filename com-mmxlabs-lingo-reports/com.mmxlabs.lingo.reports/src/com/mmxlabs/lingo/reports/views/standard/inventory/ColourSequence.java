/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

public class ColourSequence {

	private ColourSequence() {
	}

	private static final int[] sequence = {
		0xe6194b,
		0x3cb44b,
		0xffe119,
		0x4363d8,
		0xf58231,
		0x911eb4,
		0x42d4f4,
		0xf032e6,
		0xbfef45,
		0xfabed4,
		0x469990,
		0xdcbeff,
		0x9A6324,
		0xfffac8,
		0x800000,
		0xaaffc3,
		0x808000,
		0xffd8b1,
		0x000075,
		0xa9a9a9,
		0x000000
	};

	public static int[] getColourSequence() {
		return sequence;
	}
}
