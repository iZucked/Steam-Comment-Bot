/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.inventory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.standard.InventoryReport;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class ChartColourSchemeAction extends Action implements IMenuCreator {

	private InventoryReport report;
	private Menu lastMenu = null;
	private boolean showingCargoes;
	private boolean showingOpenSlots;
	private final Action showCargoesAction;
	private final Action showOpenSlotsAction;

	public ChartColourSchemeAction(final InventoryReport report) {
		super("Colour Scheme", IAction.AS_DROP_DOWN_MENU);
		this.report = report;
		showingCargoes = true;
		showingOpenSlots = true;
		showCargoesAction = new Action("Show cargoes", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				showingCargoes = !showingCargoes;
				setChecked(showingCargoes);
				report.setCargoVisibilityInInventoryChart(showingCargoes);
			}
		};
		showOpenSlotsAction = new Action("Show open", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				showingOpenSlots = !showingOpenSlots;
				setChecked(showingOpenSlots);
				report.setOpenSlotVisibilityInInventoryChart(showingOpenSlots);
			}
		};
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Label, IconMode.Enabled));
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

	}

	public boolean isShowingCargoes() {
		return this.showingCargoes;
	}

	public boolean isShowingOpenSlots() {
		return this.showingOpenSlots;
	}
}
