/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular;

import org.eclipse.jdt.annotation.Nullable;

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
