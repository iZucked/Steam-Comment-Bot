package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.eclipse.jface.viewers.ViewerFilter;

public class SetUnionFilter extends UnionFilter {
	private final Set<ViewerFilter> filters;

	@Override
	public Set<ViewerFilter> getFilters() {
		return new HashSet<>(filters);
	}

	public SetUnionFilter(Collection<ViewerFilter> filters) {
		super();
		this.filters = new HashSet<>(filters);
	}
	
	public SetUnionFilter(ViewerFilter... filters) {
		this.filters = new HashSet<>(Arrays.asList(filters));
	}

	
	private SetUnionFilter() {
		this.filters = new HashSet<>();
	}

	
	public SetUnionFilter with(Collection<ViewerFilter> filters) {
		SetUnionFilter newFilter = new SetUnionFilter(this.filters);
		newFilter.filters.addAll(filters);
		return newFilter;
	}
	
	public SetUnionFilter with(ViewerFilter... filters) {
		SetUnionFilter newFilter = new SetUnionFilter(this.filters);
		newFilter.filters.addAll(Arrays.asList(filters));
		return newFilter;
	}
	
	public SetUnionFilter without(Collection<ViewerFilter> filters) {
		SetUnionFilter newFilter = new SetUnionFilter(this.filters.stream().filter(vf -> !filters.contains(vf)).toList());
		return newFilter;
	}
	
	public SetUnionFilter without(ViewerFilter... filters) {
		Set<ViewerFilter> filtersSet = new HashSet<>(Arrays.asList(filters));
		SetUnionFilter newFilter = new SetUnionFilter(this.filters.stream().filter(vf -> !filtersSet.contains(vf)).toList());
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
		SetUnionFilter other = (SetUnionFilter) obj;
		return Objects.equals(filters, other.filters);
	}
	
	
	
}
