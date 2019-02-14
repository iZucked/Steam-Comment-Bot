/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class CargoTradesBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		final DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Open/Cargo") {
			@Override
			protected void populate(final Menu menu) {

				final Action cargoesAction = new RunnableAction("Cargoes only", () -> addCargoFilter(activeFilters, viewer, CargoFilterOption.CARGO));
				final Action openAction = new RunnableAction("Open only", () -> addCargoFilter(activeFilters, viewer, CargoFilterOption.OPEN));
				final Action longsAction = new RunnableAction("Longs", () -> addCargoFilter(activeFilters, viewer, CargoFilterOption.LONG));
				final Action shortsAction = new RunnableAction("Shorts", () -> addCargoFilter(activeFilters, viewer, CargoFilterOption.SHORT));

				// Detect currently selected option (if any)
				for (final ITradesBasedFilterHandler f : activeFilters) {
					if (f instanceof CargoBasedFilterHandler) {
						final CargoBasedFilterHandler filter = (CargoBasedFilterHandler) f;
						final CargoFilterOption currentOption = filter.option;
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

	private void addCargoFilter(final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer, final CargoFilterOption option) {
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

	private enum CargoFilterOption {
		NONE, CARGO, LONG, SHORT, OPEN
	}

	private class CargoBasedFilterHandler implements ITradesBasedFilterHandler {

		private final CargoFilterOption option;

		public CargoBasedFilterHandler(final CargoFilterOption p) {
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
				final Cargo cargo = row.getCargo();
				switch (option) {
				case NONE:
					return true;
				case CARGO:
					if (cargo != null) {
						return true;
					} else {
						return false;
					}
				case LONG:
					return isLong(row, cargo);
				case SHORT:
					return isShort(row, cargo);
				case OPEN:
					return isShort(row, cargo) || isLong(row, cargo);
				}
			}
			return false;
		}

		private boolean isLong(final Row row, final Cargo cargo) {
			if (cargo == null && row.getLoadSlot() != null) {
				return true;
			} else if (cargo != null && row.getLoadSlot() != null && row.getDischargeSlot() instanceof SpotSlot) {
				return true;
			}
			return false;
		}

		private boolean isShort(final Row row, final Cargo cargo) {
			if (cargo == null && row.getDischargeSlot() != null) {
				return true;
			} else if (cargo != null && row.getDischargeSlot() != null && row.getLoadSlot() instanceof SpotSlot) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isDefaultFilter() {
			return false;
		}
	}
}
