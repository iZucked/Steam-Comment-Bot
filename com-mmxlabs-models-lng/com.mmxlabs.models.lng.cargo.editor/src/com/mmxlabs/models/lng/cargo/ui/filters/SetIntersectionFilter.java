/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jface.viewers.ViewerFilter;

public class SetIntersectionFilter extends IntersectionFilter {
	private final Set<ViewerFilter> filters;

	@Override
	public Set<ViewerFilter> getFilters() {
		return new HashSet<>(filters);
	}

	public SetIntersectionFilter(Collection<ViewerFilter> filters) {
		super();
		this.filters = new HashSet<>(filters);
	}
	
	public SetIntersectionFilter(ViewerFilter... filters) {
		this.filters = new HashSet<>(Arrays.asList(filters));
	}

	
	private SetIntersectionFilter() {
		this.filters = new HashSet<>();
	}

	
	public SetIntersectionFilter with(Collection<ViewerFilter> filters) {
		SetIntersectionFilter newFilter = new SetIntersectionFilter(this.filters);
		newFilter.filters.addAll(filters);
		return newFilter;
	}
	
	public SetIntersectionFilter with(ViewerFilter... filters) {
		SetIntersectionFilter newFilter = new SetIntersectionFilter(this.filters);
		return newFilter;
	}
	
	public SetIntersectionFilter without(Collection<ViewerFilter> filters) {
		SetIntersectionFilter newFilter = new SetIntersectionFilter(this.filters.stream().filter(vf -> !filters.contains(vf)).toList());
		return newFilter;
	}
	
	public SetIntersectionFilter without(ViewerFilter... filters) {
		Set<ViewerFilter> filtersSet = new HashSet<>(Arrays.asList(filters));
		SetIntersectionFilter newFilter = new SetIntersectionFilter(this.filters.stream().filter(vf -> !filtersSet.contains(vf)).toList());
		newFilter.filters.addAll(Arrays.asList(filters));
		return newFilter;
	}

	@Override
	public int hashCode() {
		return Objects.hash(filters);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SetIntersectionFilter other = (SetIntersectionFilter) obj;
		return Objects.equals(filters, other.filters);
	}
	
	
	
}
