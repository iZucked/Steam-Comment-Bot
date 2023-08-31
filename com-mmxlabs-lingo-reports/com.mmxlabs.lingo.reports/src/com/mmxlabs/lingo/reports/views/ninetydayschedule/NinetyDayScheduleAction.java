package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public abstract class NinetyDayScheduleAction extends Action implements IMenuCreator {

	protected final NinetyDayScheduleReport parent;
	private Menu lastMenu = null;

	protected NinetyDayScheduleAction(String name, int type, NinetyDayScheduleReport parent) {
		super(name, type);
		this.parent = parent;
	}

	@Override
	public void run() {
		parent.redraw();
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
	public Menu getMenu(final Menu parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}

		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}

	@Override
	public Menu getMenu(final Control parent) {
		if (lastMenu != null) {
			lastMenu.dispose();
		}

		lastMenu = new Menu(parent);
		createMenuItems(lastMenu);
		return lastMenu;
	}
	
	protected abstract void createMenuItems(final Menu menu);
}
