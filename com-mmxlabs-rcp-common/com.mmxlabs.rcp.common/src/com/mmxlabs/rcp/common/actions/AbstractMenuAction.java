/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

/**
 * An abstract superclass for actions with a menu that provide their own menu.
 * 
 * @author hinton
 * 
 */
public abstract class AbstractMenuAction extends LockableAction implements IMenuCreator {
	private Menu lastMenu;

	protected AbstractMenuAction(final String label) {
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

	protected void addActionToMenu(final Action a, final Menu m) {
		final ActionContributionItem aci = new ActionContributionItem(a);
		aci.fill(m, -1);
	}

	/**
	 * Subclasses should fill their menu with actions here.
	 * 
	 * @param menu
	 *            the menu which is about to be displayed
	 */
	protected abstract void populate(final Menu menu);

	@Override
	public Menu getMenu(final Menu parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}
		lastMenu = new Menu(parent);

		populate(lastMenu);

		return lastMenu;
	}

}
