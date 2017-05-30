/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.menus;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

public class SubLocalMenuHelper {

	private final List<IMenuType> menuActions = new LinkedList<>();

	private String title;

	public SubLocalMenuHelper(final String title) {
		this.title = title;
	}

	public void clearActions() {
		this.menuActions.clear();
	}

	public void addAction(final @NonNull IAction action) {
		this.menuActions.add(new ActionMenuType(action));
	}

	public void addSubMenu(final @NonNull SubLocalMenuHelper subMenuType) {
		this.menuActions.add(new SubMenuType(subMenuType));
	}

	public IMenuManager createSubMenu() {
		MenuManager mgr = new MenuManager(title);

		for (final IMenuType menu : menuActions) {
			if (menu instanceof ActionMenuType) {
				final ActionMenuType actionMenuType = (ActionMenuType) menu;
				mgr.add(actionMenuType.getAction());
			} else if (menu instanceof SubMenuType) {
				SubMenuType subMenu = (SubMenuType) menu;
				mgr.add(subMenu.getSubMenu().createSubMenu());
			} else if (menu instanceof SeparatorMenuType) {
				mgr.add(new Separator());
			}
		}

		return mgr;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean hasActions() {
		return !menuActions.isEmpty();
	}

	public void addSeparator() {
		menuActions.add(new SeparatorMenuType());
	}
}
