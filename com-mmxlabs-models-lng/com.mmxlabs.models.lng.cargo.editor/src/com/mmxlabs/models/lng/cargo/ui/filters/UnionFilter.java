package com.mmxlabs.models.lng.cargo.ui.filters;

import org.eclipse.jface.viewers.Viewer;

/**
 * @author Euan W
 * Returns true if any of the filters contained as part of this object are true. If this object returns no filters, returns true
 */
public abstract class UnionFilter extends MultiFilter {
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return getFilters().stream().anyMatch(vf -> vf.select(viewer, parentElement, element));
	}
}
