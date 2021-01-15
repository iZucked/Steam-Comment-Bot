/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

public class EconsOptions {
	enum MarginBy {
		PURCHASE_VOLUME, SALE_VOLUME
	}

	boolean alwaysShowRawValue = false;
	MarginBy marginBy = MarginBy.SALE_VOLUME;
	boolean showPnLCalcs = true;
}