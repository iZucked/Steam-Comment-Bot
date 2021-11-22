/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataser.lng.importers.merge.support;

public class OverwriteExistingOption extends MergeOption {

	@Override
	public String toString() {
		return "Overwrite existing";
	}

	@Override
	public int getIndex(final int offset) {
		return 0;
	}
}
