/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;

public class DefaultExtraFiltersProvider implements IExtraFiltersProvider{
	
	public List<ViewerFilter> getExtraFilters(){
		return Collections.emptyList();
	}
	
	public List<DefaultMenuCreatorAction> getExtraMenuActions(final StructuredViewer viewer){
		return Collections.emptyList();
	}
	
	public void clear() {
		
	}
}
