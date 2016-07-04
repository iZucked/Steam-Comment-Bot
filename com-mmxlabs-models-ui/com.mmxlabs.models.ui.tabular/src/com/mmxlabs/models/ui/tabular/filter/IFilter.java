/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.filter;

import java.util.Map;

import com.mmxlabs.common.Pair;

/**
 * generic interface for a filter on a bunch of key-value pairs
 * 
 * @author hinton
 * 
 */
public interface IFilter {
	public boolean matches(final Map<String, Pair<?, ?>> properties);

	/**
	 * 
	 * @return something identical to this filter, but possible more efficient
	 */
	public IFilter collapse();
}
