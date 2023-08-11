/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Set;

import org.eclipse.jface.action.Action;

import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;

public interface ITradesBasedFilterHandler {

	Action activateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer);
	Action deactivateAction(ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer);
	void activate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters);
	void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters);
	boolean isRowVisible(TradesRow tradesRow);
	boolean isDefaultFilter();

}