/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class CargoTradesBasedFilterHandler implements ITradesBasedFilterHandler {
	private IPropertyChangeListener propertyChangeListener;
	private final Set<String> filtersOpenContracts;

	public CargoTradesBasedFilterHandler(final Set<String> filtersOpenContracts) {
		this.filtersOpenContracts = filtersOpenContracts;
	}
	
	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		final DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Open/Cargo") {
			@Override
			protected void populate(final Menu menu) {

				final Action cargoesAction = new RunnableAction("Cargoes only", () -> addCargoFilter(activeFilters, viewer, CargoEditorFilterUtils.CargoFilterOption.CARGO));
				final Action openAction = new RunnableAction("Open only", () -> addCargoFilter(activeFilters, viewer, CargoEditorFilterUtils.CargoFilterOption.OPEN));
				final Action longsAction = new RunnableAction("Longs", () -> addCargoFilter(activeFilters, viewer, CargoEditorFilterUtils.CargoFilterOption.LONG));
				final Action shortsAction = new RunnableAction("Shorts", () -> addCargoFilter(activeFilters, viewer, CargoEditorFilterUtils.CargoFilterOption.SHORT));

				// Detect currently selected option (if any)
				for (final ITradesBasedFilterHandler f : activeFilters) {
					if (f instanceof CargoBasedFilterHandler) {
						final CargoBasedFilterHandler filter = (CargoBasedFilterHandler) f;
						final CargoEditorFilterUtils.CargoFilterOption currentOption = filter.option;
						// If any action is selected, then check it.
						if (currentOption != null) {
							switch (currentOption) {
							case CARGO:
								cargoesAction.setChecked(true);
								break;
							case LONG:
								longsAction.setChecked(true);
								break;
							case OPEN:
								openAction.setChecked(true);
								break;
							case SHORT:
								shortsAction.setChecked(true);
								break;
							default:
								break;
							}
						}
					}
				}

				addActionToMenu(cargoesAction, menu);
				addActionToMenu(openAction, menu);
				addActionToMenu(longsAction, menu);
				addActionToMenu(shortsAction, menu);
			}

			@Override
			public void run() {
			}
		};
		return action;
	}

	private void addCargoFilter(final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer, final CargoEditorFilterUtils.CargoFilterOption option) {
		final Iterator<ITradesBasedFilterHandler> itr = activeFilters.iterator();
		while (itr.hasNext()) {
			final ITradesBasedFilterHandler filter = itr.next();
			if (filter instanceof CargoBasedFilterHandler) {
				itr.remove();
			}
		}
		activeFilters.add(new CargoBasedFilterHandler(option));
		viewer.getScenarioViewer().refresh();
	}

	@Override
	public Action deactivateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		// Nothing to do
		return null;
	}

	@Override
	public void activate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
		// Nothing to do
	}

	@Override
	public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
		// Nothing to do
	}

	@Override
	public boolean isRowVisible(final Row row) {
		return true;
	}

	@Override
	public boolean isDefaultFilter() {
		return true;
	}

	private class CargoBasedFilterHandler implements ITradesBasedFilterHandler {

		private final CargoEditorFilterUtils.CargoFilterOption option;

		public CargoBasedFilterHandler(final CargoEditorFilterUtils.CargoFilterOption p) {
			this.option = p;
		}

		@Override
		public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
			return null;
		}

		@Override
		public Action deactivateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
			return null;
		}

		@Override
		public void activate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {

		}

		@Override
		public void deactivate(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters) {
			activeFilters.remove(this);
		}

		@Override
		public boolean isRowVisible(final Row row) {
			if (row != null) {
				return CargoEditorFilterUtils.cargoTradesFilter(option, row.getCargo(), row.getLoadSlot(), row.getDischargeSlot(), filtersOpenContracts);
			} else {
				return false;
			}
		}
		
		@Override
		public boolean isDefaultFilter() {
			return false;
		}
	}
}
