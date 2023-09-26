/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.ui.filters.SetUnionFilter;
import com.mmxlabs.models.lng.cargo.ui.filters.UnionFilter;

public class StructuredViewerFilterManager {
	private final StructuredViewer viewer;
	private final Map<Object, ViewerFilter> filterMap;
	private final Set<ViewerFilter> permanentFilters;

	public StructuredViewerFilterManager(StructuredViewer viewer) {
		this.viewer = viewer;
		this.filterMap = new HashMap<>();
		permanentFilters = new HashSet<>();
	}

	public StructuredViewerFilterManager(AbstractTreeViewer viewer, ViewerFilter... filters) {
		this(viewer);
		for (ViewerFilter filter : Arrays.asList(filters)) {
			addPermanentFilter(filter);
		}
	}

	public StructuredViewerFilterManager(AbstractTreeViewer viewer, Pair<Object, ViewerFilter>... filters) {
		this(viewer);
		for (Pair<Object, ViewerFilter> filter : Arrays.asList(filters)) {
			addFilter(filter.getFirst(), filter.getSecond());
		}
	}

	public void addPermanentFilter(ViewerFilter filter) {
		getViewer().addFilter(filter);
		permanentFilters.add(filter);
		getViewer().refresh(false);
	}

	public boolean addFilter(Object id, ViewerFilter filter) {
		boolean oldFilterPresent = removeFilter(id);
		filterMap.put(id, filter);
		getViewer().addFilter(filter);
		getViewer().refresh(false);
		return oldFilterPresent;
	}

	public boolean removeFilter(Object id) {
		if (!filterMap.containsKey(id)) {
			return false;
		}
		ViewerFilter oldFilter = filterMap.remove(id);
		getViewer().removeFilter(oldFilter);
		getViewer().refresh(false);
		return true;
	}

	public boolean removeFilters(boolean includePermanent) {
		if (filterMap.isEmpty() && (!includePermanent || permanentFilters.isEmpty())) {
			return false;
		}
		for (ViewerFilter filter : filterMap.values()) {
			getViewer().removeFilter(filter);
		}
		filterMap.clear();
		if (includePermanent) {
			for (ViewerFilter filter : permanentFilters) {
				getViewer().removeFilter(filter);
			}
			permanentFilters.clear();
		}
		getViewer().refresh(false);
		return true;
	}

	public boolean removeFilters() {
		return removeFilters(false);
	}

	public boolean filterExists(Object key) {
		return filterMap.containsKey(key);
	}

	public ViewerFilter getFilter(Object key) {
		return filterMap.get(key);
	}

	public StructuredViewer getViewer() {
		return viewer;
	}


	public boolean test(Object o) {
		return filterMap.values().stream().allMatch(vf -> vf.select(viewer, viewer, o))
				&& permanentFilters.stream().allMatch(vf -> vf.select(viewer, viewer, o));
	}

	public boolean addFilterAsUnion(Object key, ViewerFilter filter) {
		if (filterExists(key)) {
			ViewerFilter oldFilter = getFilter(key);
			if (!filter.equals(oldFilter)) {
				if (oldFilter instanceof SetUnionFilter unionFilter) {
					addFilter(key, unionFilter.with(filter));
				} else {
					addFilter(key, new SetUnionFilter(filter, oldFilter));
				}
			}
			return true;
		} else {
			addFilter(key, filter);
			return false;
		}
	}

	public boolean removeFilter(Object key, ViewerFilter filter, boolean checkUnions) {
		if (filterExists(key)) {
			if (getFilter(key).equals(filter)) {
				removeFilter(key);
				return true;
			} else if (getFilter(key) instanceof SetUnionFilter unionFilter && checkUnions) {
				SetUnionFilter newUnionFilter = unionFilter.without(filter);
				int newFilterSize = newUnionFilter.getFilters().size();
				if (newFilterSize == 0) {
					removeFilter(key);
				} else if(newFilterSize == 1) {
					addFilter(key, newUnionFilter.getFilters().iterator().next());
				} else {
					addFilter(key, newUnionFilter);
				}				
			}
		}
		return false;
	}


	public boolean removeFilter(Object key, ViewerFilter filter) {
		return removeFilter(key, filter, false);
	}

	public boolean containsFilter(Object key, ViewerFilter filter) {
		if (!filterExists(key)) {
			return false;
		} else if (getFilter(key).equals(filter)) {
			return true;
		} else if (getFilter(key) instanceof UnionFilter unionFilter) {
			return unionFilter.getFilters().contains(filter);
		}
		return false;
	}
}
