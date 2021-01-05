/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Set;

import org.eclipse.jface.action.Action;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;

public class DefaultTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		Action action = new Action("Show all cargoes") {
			@Override
			public void run() {
				columnFilters.resetDefaultTypeFilters();
				for (ITradesBasedFilterHandler filter : activeFilters.toArray(new ITradesBasedFilterHandler[activeFilters.size()])) {
					if (!filter.isDefaultFilter()) {
						filter.deactivate(columnFilters, activeFilters);
					}
				}
				viewer.setColumnsVisibility(); // TODO (move this method out of NewStyleCargoEditorPane)
				viewer.getScenarioViewer().refresh();
			}
		};
		return action;
	}

	@Override
	public Action deactivateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		return null;
	}

	@Override
	public boolean isRowVisible(Row row) {
		return true;
	}

	@Override
	public boolean isDefaultFilter() {
		return true;
	}

	@Override
	public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {

	}

	@Override
	public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
	}

}
