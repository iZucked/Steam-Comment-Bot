/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataser.lng.importers.merge.support;

public class IgnoreOption extends MergeOption {

	@Override
	public String toString() {
		return "Ignore";
	}

	@Override
	public int getIndex(final int offset) {
		return 1;
	}
}
