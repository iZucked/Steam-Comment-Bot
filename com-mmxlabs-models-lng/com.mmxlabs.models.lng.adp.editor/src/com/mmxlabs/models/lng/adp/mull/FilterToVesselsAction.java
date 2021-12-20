/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.mull;

import java.util.function.Consumer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;

public class FilterToVesselsAction extends Action implements IMenuCreator {

	private boolean showingVessels = false;
	private Menu lastMenu = null;
	private final Action showVesselsAction;

	public FilterToVesselsAction(final Consumer<Boolean> filterToVesselsAction) {
		super("Filter", IAction.AS_DROP_DOWN_MENU);
		showVesselsAction = new Action("Only vessels", IAction.AS_CHECK_BOX) {
			@Override
			public void run() {
				showingVessels = !showingVessels;
				setChecked(showingVessels);
				filterToVesselsAction.accept(showingVessels);
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
		showVesselsAction.setChecked(showingVessels);
		final ActionContributionItem actionContributionItem = new ActionContributionItem(showVesselsAction);
		actionContributionItem.fill(menu, 0);
	}
	
	public boolean isShowingVessels() {
		return this.showingVessels;
	}

}
