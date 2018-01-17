package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Set;

import org.eclipse.jface.action.Action;

import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;

public interface ITradesBasedFilterHandler {

	Action activateAction(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer);
	Action deactivateAction(ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer);
	void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters);
	boolean isRowVisible(Row row);
	boolean isDefaultFilter();

}