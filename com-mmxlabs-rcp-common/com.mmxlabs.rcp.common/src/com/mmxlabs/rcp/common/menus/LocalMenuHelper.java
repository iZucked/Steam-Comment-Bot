/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.menus;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuListener2;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public class LocalMenuHelper {

	private final MenuManager mgr;

	private final List<IMenuType> menuActions = new LinkedList<>();

	private final Composite control;
	private Menu menu;

	private final IMenuListener listener;

	private String title;

	public LocalMenuHelper(final Composite control) {
		this.control = control;
		this.mgr = new MenuManager();

		this.listener = new IMenuListener2() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				if (menu == null) {
					menu = mgr.createContextMenu(control);
				}
				final IContributionItem[] l = mgr.getItems();
				mgr.removeAll();
				for (final IContributionItem itm : l) {
					itm.dispose();
				}

				if (title != null) {
					mgr.add(new GroupMarker(title));
				}

				for (final IMenuType menu : menuActions) {
					if (menu instanceof ActionMenuType) {
						final ActionMenuType actionMenuType = (ActionMenuType) menu;
						mgr.add(actionMenuType.getAction());
					} else if (menu instanceof SubMenuType) {
						final SubMenuType subMenu = (SubMenuType) menu;
						mgr.add(subMenu.getSubMenu().createSubMenu());
					} else if (menu instanceof SeparatorMenuType) {
						mgr.add(new Separator());
					}
				}

				menu.setVisible(true);
			}

			@Override
			public void menuAboutToHide(final IMenuManager manager) {
				if (menu != null) {
					menu.setVisible(false);
				}
			}

		};
		mgr.addMenuListener(listener);
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

	public void addSeparator() {
		menuActions.add(new SeparatorMenuType());
	}

	public void open() {
		mgr.fill(control);
		mgr.setVisible(true);
		listener.menuAboutToShow(mgr);
	}

	public void dispose() {
		menuActions.clear();
		mgr.dispose();
	}

	public void setTitle(final String title) {
		this.title = title;
	}
}
