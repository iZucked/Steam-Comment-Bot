package com.mmxlabs.models.lng.cargo.ui.filters;

import java.util.List;
import java.util.Objects;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class PairIntersectionFilter extends IntersectionFilter {

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj instanceof PairIntersectionFilter other) {
			return Objects.equals(first, other.first) && Objects.equals(second, other.second);
		}
		return false;
	}

	private final ViewerFilter first;
	private final ViewerFilter second;

	public PairIntersectionFilter(ViewerFilter first, ViewerFilter second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return first.select(viewer, parentElement, element) && second.select(viewer, parentElement, element);
	}

	ViewerFilter getFirst() {
		return first;
	}

	ViewerFilter getSecond() {
		return second;
	}

	@Override
	public List<ViewerFilter> getFilters() {
		return List.of(first, second);
	}

}
