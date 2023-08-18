/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.filters;

import org.eclipse.jface.viewers.Viewer;


/**
 * @author Euan W
 * Returns true if all of the filters contained as part of this object are true. If this object returns no filters, returns true
 */
public abstract class IntersectionFilter extends MultiFilter {
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return getFilters().stream().allMatch(vf -> vf.select(viewer, parentElement, element));
	}

}
