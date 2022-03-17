/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;
@FunctionalInterface
public interface IComparableProvider {
	Comparable getComparable(Object object);
}
