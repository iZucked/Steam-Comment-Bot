/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;

public interface IExtraFiltersProvider {
	
	List<ViewerFilter> getExtraFilters();
	
	List<DefaultMenuCreatorAction> getExtraMenuActions(final StructuredViewer viewer);
	
	void clear();
}
