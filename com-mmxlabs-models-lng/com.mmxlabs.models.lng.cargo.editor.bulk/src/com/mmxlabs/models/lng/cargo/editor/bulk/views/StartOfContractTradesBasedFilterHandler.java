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
import com.mmxlabs.models.lng.port.Port;

public class StartOfContractTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		Action action = new Action("Show start Columns") {
			@Override
			public void run() {
				columnFilters.addGroupFilter(TradesBasedColumnFactory.LOAD_START_GROUP, TradesBasedColumnFactory.LOAD_START_GROUP);
				columnFilters.addGroupFilter(TradesBasedColumnFactory.DISCHARGE_START_GROUP, TradesBasedColumnFactory.DISCHARGE_START_GROUP);
				activeFilters.add(StartOfContractTradesBasedFilterHandler.this);
				viewer.setColumnsVisibility();
				viewer.getScenarioViewer().refresh();
			}
		};
		return action;
	}

	@Override
	public Action deactivateAction(ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, BulkTradesTablePane viewer) {
		return null;
	}

	@Override
	public void activate(ColumnFilters columnFilters, Set<ITradesBasedFilterHandler> activeFilters) {

	}

	@Override
	public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
		activeFilters.remove(this); // TODO - how about isActive?
		columnFilters.removeGroupFilter(TradesBasedColumnFactory.LOAD_START_GROUP, TradesBasedColumnFactory.LOAD_START_GROUP);
		columnFilters.removeGroupFilter(TradesBasedColumnFactory.DISCHARGE_START_GROUP, TradesBasedColumnFactory.DISCHARGE_START_GROUP);
	}

	@Override
	public boolean isRowVisible(TradesRow tradesRow) {
		if (tradesRow.getLoadSlot() != null) {
			Port port = tradesRow.getLoadSlot().getPort();
			if (port != null && port.getName().equals("Bonny")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDefaultFilter() {
		return false;
	}

}
