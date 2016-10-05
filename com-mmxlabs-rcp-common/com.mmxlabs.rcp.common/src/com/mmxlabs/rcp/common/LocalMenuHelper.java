package com.mmxlabs.rcp.common;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuListener2;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

public class LocalMenuHelper {

	private final MenuManager mgr;

	private final List<IAction> menuActions = new LinkedList<>();

	private final Composite control;
	private Menu menu;

	private IMenuListener listener;

	public LocalMenuHelper(final Composite control) {
		this.control = control;
		this.mgr = new MenuManager();

		this.listener = new IMenuListener2() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				if (menu == null) {
					menu = mgr.createContextMenu(control);
				}
				mgr.removeAll();

				for (final IAction action : menuActions) {
					mgr.add(action);
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

	public void addAction(final IAction action) {
		this.menuActions.add(action);
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

}
