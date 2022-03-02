/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataser.lng.importers.merge.support;

public class AddOption extends MergeOption {

	@Override
	public String toString() {
		return "Add";
	}

	@Override
	public int getIndex(final int offset) {
		return 0;
	}
}
