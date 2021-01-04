/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.inventory;

import org.eclipse.jdt.annotation.NonNull;

public class InventoryFileName {
	public static @NonNull String getName(@NonNull String prefix, @NonNull String postfix) {
		String result = prefix;
		result += postfix;
		return result;
	}
}
