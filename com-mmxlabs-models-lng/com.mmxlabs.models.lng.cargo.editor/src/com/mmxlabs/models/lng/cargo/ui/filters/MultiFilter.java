/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.Collection;

import org.eclipse.jface.viewers.ViewerFilter;

/**
 * A multi filter is a filter that contains other filters that can be obtained through the getFilters().
 *  The Collection returned by this method should not be altered by the class;
 *  This class makes no assumptions about how these filters are used in the select method.
 * 
 * @author Euan Worth
 *
 */
public abstract class MultiFilter extends ViewerFilter {
	public abstract Collection<ViewerFilter> getFilters();
}
