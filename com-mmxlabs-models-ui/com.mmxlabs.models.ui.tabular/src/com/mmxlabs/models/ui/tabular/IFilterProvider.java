/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import java.util.List;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;

/**
 * Implementors are used by {@link EObjectEditorViewerPane} to render (as text) and sort columns. Rendering and sorting are together because they are typically operations decided by the type of the
 * thing being rendered.
 * 
 * @author hinton
 * 
 */
public interface IFilterProvider  {
	/**
	 * Render the given object for viewing in a table cell.
	 * 
	 * @param object
	 * @return string rep. of object
	 */
	@Nullable
	String render(Object object);

	/**
	 * Get a representation useful for filtering.
	 * 
	 * @param object
	 * @return
	 */
	@Nullable
	Object getFilterValue(Object object);
}
