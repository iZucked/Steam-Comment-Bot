/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.models.lng.cargo.ui.editorpart.StructuredViewerFilterManager;
import com.mmxlabs.models.lng.cargo.ui.filters.EMFPathFilter;
import com.mmxlabs.models.lng.cargo.ui.filters.ShippedCargoFilter;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class ShippedCargoFilterAction extends DefaultMenuCreatorAction {
	private static final String SHIPPED_FILTER_ID = "Shipped";

	private final StructuredViewerFilterManager filterManager;

	public ShippedCargoFilterAction(StructuredViewerFilterManager filterManager) {
		super("Shipped");
		this.filterManager = filterManager;
	}

	@Override
	protected void populate(final Menu menu) {
		final ShippedCargoFilter shippedFilter = new ShippedCargoFilter(CargoEditorFilterUtils.ShippedCargoFilterOption.SHIPPED);
		final ShippedCargoFilter nonShippedFilter = new ShippedCargoFilter(CargoEditorFilterUtils.ShippedCargoFilterOption.NON_SHIPPED);
		final ShippedCargoFilter fobFilter = new ShippedCargoFilter(CargoEditorFilterUtils.ShippedCargoFilterOption.FOB);
		final ShippedCargoFilter desFilter = new ShippedCargoFilter(CargoEditorFilterUtils.ShippedCargoFilterOption.DES);
		final ShippedCargoFilter nominalFilter = new ShippedCargoFilter(CargoEditorFilterUtils.ShippedCargoFilterOption.NOMINAL);

		final Action clearShippedCargoAction = new Action("Clear Open/Cargo Filters") {
			@Override
			public void run() {
				filterManager.removeFilter(SHIPPED_FILTER_ID);
			}
		};

		Action shippedAction;
		if (filterManager.containsFilter(SHIPPED_FILTER_ID, shippedFilter)) {
			shippedAction = new Action("Shipped") {
				@Override
				public void run() {
					filterManager.removeFilter(SHIPPED_FILTER_ID, shippedFilter, true);
				}
			};

			shippedAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			shippedAction = new Action("Shipped") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(SHIPPED_FILTER_ID, shippedFilter);
					} else {
						filterManager.addFilter(SHIPPED_FILTER_ID, shippedFilter);
					}
				}
			};
		}

		Action nonShippedAction;
		if (filterManager.containsFilter(SHIPPED_FILTER_ID, nonShippedFilter)) {
			nonShippedAction = new Action("Non Shipped") {
				@Override
				public void run() {
					filterManager.removeFilter(SHIPPED_FILTER_ID, nonShippedFilter, true);
				}
			};
			nonShippedAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			nonShippedAction = new Action("Non Shipped") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(SHIPPED_FILTER_ID, nonShippedFilter);
					} else {
						filterManager.addFilter(SHIPPED_FILTER_ID, nonShippedFilter);
					}
				}
			};
		}
		Action fobAction;
		if (filterManager.containsFilter(SHIPPED_FILTER_ID, fobFilter)) {
			fobAction = new Action("FOB") {
				@Override
				public void run() {
					filterManager.removeFilter(SHIPPED_FILTER_ID, fobFilter, true);
				}
			};
			fobAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			fobAction = new Action("FOB") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(SHIPPED_FILTER_ID, fobFilter);
					} else {
						filterManager.addFilter(SHIPPED_FILTER_ID, fobFilter);
					}
				}
			};
		}
		Action desAction;
		if (filterManager.containsFilter(SHIPPED_FILTER_ID, desFilter)) {
			desAction = new Action("DES") {
				@Override
				public void run() {
					filterManager.removeFilter(SHIPPED_FILTER_ID, desFilter, true);
				}
			};
			desAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			desAction = new Action("DES") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(SHIPPED_FILTER_ID, desFilter);
					} else {
						filterManager.addFilter(SHIPPED_FILTER_ID, desFilter);
					}
				}
			};
		}
		Action nominalsAction;
		if (filterManager.containsFilter(SHIPPED_FILTER_ID, nominalFilter)) {
			nominalsAction = new Action("Nominal") {
				@Override
				public void run() {
					filterManager.removeFilter(SHIPPED_FILTER_ID, nominalFilter, true);
				}
			};
			nominalsAction.setImageDescriptor(IconPaths.BlackDot.getImageDescriptor(IconMode.Enabled));
		} else {
			nominalsAction = new Action("Nominal") {

				@Override
				public void runWithEvent(Event e) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						filterManager.addFilterAsUnion(SHIPPED_FILTER_ID, nominalFilter);
					} else {
						filterManager.addFilter(SHIPPED_FILTER_ID, nominalFilter);
					}
				}
			};
		}
		
		if (filterManager.filterExists(SHIPPED_FILTER_ID)) {
			addActionToMenu(clearShippedCargoAction, menu);
		}
		addActionToMenu(shippedAction, menu);
		addActionToMenu(nonShippedAction, menu);
		addActionToMenu(fobAction, menu);
		addActionToMenu(desAction, menu);
		addActionToMenu(nominalsAction, menu);
	}

}
