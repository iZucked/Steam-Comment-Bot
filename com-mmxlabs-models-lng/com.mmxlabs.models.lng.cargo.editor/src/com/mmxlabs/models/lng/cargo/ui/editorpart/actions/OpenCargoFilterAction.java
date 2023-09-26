/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.filters.TradesCargoFilter;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class OpenCargoFilterAction extends DefaultMenuCreatorAction {
	private final StructuredViewerFilterManager filterManager;
	private final Set<String> filtersOpenContracts;
	
	private static final String CARGOES_FILTER_ID = "Cargoes";

	public OpenCargoFilterAction(StructuredViewerFilterManager filterManager, Set<String> filtersOpenContracts) {
		super("Open/Cargo");
		this.filterManager = filterManager;
		this.filtersOpenContracts = filtersOpenContracts;
	}

	@Override
	protected void populate(final Menu menu) {
		final TradesCargoFilter cargoesFilter = new TradesCargoFilter(filtersOpenContracts, CargoEditorFilterUtils.CargoFilterOption.CARGO);
		final TradesCargoFilter openFilter = new TradesCargoFilter(filtersOpenContracts, CargoEditorFilterUtils.CargoFilterOption.OPEN);
		final TradesCargoFilter longsFilter = new TradesCargoFilter(filtersOpenContracts, CargoEditorFilterUtils.CargoFilterOption.LONG);
		final TradesCargoFilter shortsFilter = new TradesCargoFilter(filtersOpenContracts, CargoEditorFilterUtils.CargoFilterOption.SHORT);
		
		
		final Action clearTradesCargoAction = new Action("Clear Open/Cargo Filters") {
			@Override
			public void run() {
				filterManager.removeFilter(CARGOES_FILTER_ID);
			}
		};

		Action cargoesAction;
		if (filterManager.containsFilter(CARGOES_FILTER_ID, cargoesFilter)) {
			cargoesAction = new Action("Cargoes") {
				@Override
				public void run() {
					filterManager.removeFilter(CARGOES_FILTER_ID, cargoesFilter, true);
				}
			};

			cargoesAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			cargoesAction = new Action("Cargoes") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(CARGOES_FILTER_ID, cargoesFilter);
					} else {
						filterManager.addFilter(CARGOES_FILTER_ID, cargoesFilter);
					}
				}
			};
		}

		Action openAction;
		if (filterManager.containsFilter(CARGOES_FILTER_ID, openFilter)) {
			openAction = new Action("Open") {
				@Override
				public void run() {
					filterManager.removeFilter(CARGOES_FILTER_ID, openFilter, true);
				}
			};
			openAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			openAction = new Action("Open") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(CARGOES_FILTER_ID, openFilter);
					} else {
						filterManager.addFilter(CARGOES_FILTER_ID, openFilter);
					}
				}
			};
		}
		Action longsAction;
		if (filterManager.containsFilter(CARGOES_FILTER_ID, longsFilter)) {
			longsAction = new Action("Longs") {
				@Override
				public void run() {
					filterManager.removeFilter(CARGOES_FILTER_ID, longsFilter, true);
				}
			};
			longsAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			longsAction = new Action("Longs") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(CARGOES_FILTER_ID, longsFilter);
					} else {
						filterManager.addFilter(CARGOES_FILTER_ID, longsFilter);
					}
				}
			};
		}
		Action shortsAction;
		if (filterManager.containsFilter(CARGOES_FILTER_ID, shortsFilter)) {
			shortsAction = new Action("Shorts") {
				@Override
				public void run() {
					filterManager.removeFilter(CARGOES_FILTER_ID, shortsFilter, true);
				}
			};
			shortsAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			shortsAction = new Action("Shorts") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(CARGOES_FILTER_ID, shortsFilter);
					} else {
						filterManager.addFilter(CARGOES_FILTER_ID, shortsFilter);
					}
				}
			};
		}
		if (filterManager.filterExists(CARGOES_FILTER_ID)) {
			addActionToMenu(clearTradesCargoAction, menu);
		}
		addActionToMenu(cargoesAction, menu);
		addActionToMenu(openAction, menu);
		addActionToMenu(longsAction, menu);
		addActionToMenu(shortsAction, menu);
	}
}
