package com.mmxlabs.rcp.common.menus;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

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
			}
		}

		return mgr;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
