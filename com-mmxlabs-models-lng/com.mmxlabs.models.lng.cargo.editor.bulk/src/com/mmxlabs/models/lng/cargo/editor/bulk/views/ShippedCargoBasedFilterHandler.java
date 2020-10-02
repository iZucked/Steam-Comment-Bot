/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.views;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.BulkTradesTablePane;
import com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart.ColumnFilters;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.rcp.common.actions.RunnableAction;

public class ShippedCargoBasedFilterHandler implements ITradesBasedFilterHandler {

	@Override
	public Action activateAction(final ColumnFilters columnFilters, final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer) {
		final DefaultMenuCreatorAction action = new DefaultMenuCreatorAction("Shipped") {
			@Override
			protected void populate(final Menu menu) {

				final Action shippedAction = new RunnableAction("Shipped", () -> addCargoFilter(activeFilters, viewer, ShippedCargoFilterOption.SHIPPED));
				final Action nonShippedAction = new RunnableAction("Non Shipped", () -> addCargoFilter(activeFilters, viewer, ShippedCargoFilterOption.NON_SHIPPED));
				final Action desAction = new RunnableAction("DES", () -> addCargoFilter(activeFilters, viewer, ShippedCargoFilterOption.DES));
				final Action fobAction = new RunnableAction("FOB", () -> addCargoFilter(activeFilters, viewer, ShippedCargoFilterOption.FOB));
				final Action nominalAction = new RunnableAction("Nominal", () -> addCargoFilter(activeFilters, viewer, ShippedCargoFilterOption.NOMINAL));

				// Detect currently selected option (if any)
				for (final ITradesBasedFilterHandler f : activeFilters) {
					if (f instanceof CargoBasedFilterHandler) {
						final CargoBasedFilterHandler filter = (CargoBasedFilterHandler) f;
						final ShippedCargoFilterOption currentOption = filter.option;
						// If any action is selected, then check it.
						if (currentOption != null) {
							switch (currentOption) {
							case SHIPPED:
								shippedAction.setChecked(true);
								break;
							case NON_SHIPPED:
								nonShippedAction.setChecked(true);
								break;
							case DES:
								desAction.setChecked(true);
								break;
							case FOB:
								fobAction.setChecked(true);
								break;
							case NOMINAL:
								nominalAction.setChecked(true);
								break;
							default:
								break;
							}
						}
					}
				}

				addActionToMenu(shippedAction, menu);
				addActionToMenu(nonShippedAction, menu);
				addActionToMenu(desAction, menu);
				addActionToMenu(fobAction, menu);
				addActionToMenu(nominalAction, menu);

			}

			@Override
			public void run() {
			}
		};
		return action;
	}

	private void addCargoFilter(final Set<ITradesBasedFilterHandler> activeFilters, final BulkTradesTablePane viewer, final ShippedCargoFilterOption option) {
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

	private enum ShippedCargoFilterOption {
		NONE, SHIPPED, NON_SHIPPED, DES, FOB, NOMINAL
	}

	private class CargoBasedFilterHandler implements ITradesBasedFilterHandler {

		private final ShippedCargoFilterOption option;

		public CargoBasedFilterHandler(final ShippedCargoFilterOption p) {
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
				final CargoType cargoType = (cargo != null ? cargo.getCargoType() : null);
				switch (option) {
				case NONE:
					return true;
				case SHIPPED:
					return cargo != null && cargoType == CargoType.FLEET;
				case NON_SHIPPED:
					return cargo != null && cargoType != CargoType.FLEET;
				case DES:
					return cargo != null && cargoType == CargoType.DES;
				case FOB:
					return cargo != null && cargoType == CargoType.FOB;
				case NOMINAL:
					if (cargo != null) {
						final VesselAssignmentType cargoVesselAT = cargo.getVesselAssignmentType();
						final CharterInMarket cargoCharterInMarket;
						if (cargoVesselAT instanceof CharterInMarket) {
							cargoCharterInMarket = (CharterInMarket) cargoVesselAT;
						} else if (cargoVesselAT instanceof CharterInMarketOverride) {
							cargoCharterInMarket = ((CharterInMarketOverride) cargoVesselAT).getCharterInMarket();
						} else {
							return false;
						}
						return cargoCharterInMarket.isNominal();
					} else {
						return false;
					}
				}
			}
			return false;
		}
		
		@Override
		public boolean isDefaultFilter() {
			return false;
		}
	}
}
