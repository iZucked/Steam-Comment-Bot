/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.rcp.common.actions.LockableAction;

public abstract class DefaultMenuCreatorAction extends LockableAction implements IMenuCreator {
	private Menu lastMenu;

	public DefaultMenuCreatorAction(final String label) {
		super(label, IAction.AS_DROP_DOWN_MENU);
	}

	@Override
	public void dispose() {
		if ((lastMenu != null) && (lastMenu.isDisposed() == false)) {
			lastMenu.dispose();
		}
		lastMenu = null;
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return this;
	}

	@Override
	public Menu getMenu(final Control parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);

		populate(lastMenu);

		return lastMenu;
	}

	protected abstract void populate(Menu menu);

	@Override
	public Menu getMenu(final Menu parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);

		populate(lastMenu);

		return lastMenu;
	}

	protected void addActionToMenu(final Action a, final Menu m) {
		final ActionContributionItem aci = new ActionContributionItem(a);
		aci.fill(m, -1);
	}

}