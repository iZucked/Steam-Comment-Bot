/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

public interface DeltaPair {
	default Object first() {
		return null;
	}

	default Object second() {
		return null;
	}

	default String getName() {
		return null;
	}
}