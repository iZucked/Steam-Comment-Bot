/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.lingo.reports.views.standard.InventoryReport;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class ChartColourSchemeAction extends Action implements IMenuCreator {

	private InventoryReport report;
	private Menu lastMenu = null;
	private boolean showingCargoes;
	private boolean showingOpenSlots;
	private boolean showingCV;
	private final Action showCargoesAction;
	private final Action showOpenSlotsAction;
	private final Action showCVAction;

	public ChartColourSchemeAction(final InventoryReport report) {
		super("Filter", IAction.AS_DROP_DOWN_MENU);
		this.report = report;
		showingCargoes = true;
		showingOpenSlots = true;
		showCargoesAction = new Action("Cargoes", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				showingCargoes = !showingCargoes;
				setChecked(showingCargoes);
				report.setCargoVisibilityInInventoryChart(showingCargoes);
			}
		};
		showOpenSlotsAction = new Action("Open", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				showingOpenSlots = !showingOpenSlots;
				setChecked(showingOpenSlots);
				report.setOpenSlotVisibilityInInventoryChart(showingOpenSlots);
			}
		};
		final Inventory inv = report.getSelectedInventory();
		boolean initialCVState = report.getMemento().getBoolean(InventoryReportConstants.Show_CV);
		showingCV = initialCVState && inv != null && inv.getFacilityType() != InventoryFacilityType.UPSTREAM && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL);
		showCVAction = new Action("CV", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				showingCV = !showingCV;
				report.getMemento().putBoolean(InventoryReportConstants.Show_CV, showingCV);
				setChecked(showingCV);
				report.setCVVisibilityInInventoryChart(showingCV);
			}
		};
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return this;
	}

	@Override
	public void dispose() {
		if (lastMenu != null) {
			lastMenu.dispose();
			lastMenu = null;
		}
	}

	@Override
	public Menu getMenu(Control parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}

	private void createMenuItems(final Menu menu) {
		showCargoesAction.setChecked(showingCargoes);
		final ActionContributionItem showCargoesAci = new ActionContributionItem(showCargoesAction);
		showCargoesAci.fill(menu, 0);
		showOpenSlotsAction.setChecked(showingOpenSlots);
		final ActionContributionItem showOpenSlotsAci = new ActionContributionItem(showOpenSlotsAction);
		showOpenSlotsAci.fill(menu, 0);
		showCVAction.setChecked(showingCV);
		final Inventory inv = report.getSelectedInventory();
		if (inv != null && inv.getFacilityType() != InventoryFacilityType.UPSTREAM && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL)) {
			final ActionContributionItem showCVAci = new ActionContributionItem(showCVAction);
			showCVAci.fill(menu, 0);
		}
	}
	
	public void update() {
		boolean initialCVState = report.getMemento().getBoolean(InventoryReportConstants.Show_CV);
		
		final Inventory inv = report.getSelectedInventory();
		showingCV = initialCVState && inv != null && inv.getFacilityType() != InventoryFacilityType.UPSTREAM && LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL);
	}

	public boolean isShowingCargoes() {
		return this.showingCargoes;
	}

	public boolean isShowingOpenSlots() {
		return this.showingOpenSlots;
	}

	public boolean isShowingCV() {
		return this.showingCV;
	}
}
