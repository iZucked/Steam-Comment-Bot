/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.ganttviewer.GanttChartViewer;

public abstract class SchedulerViewAction extends Action implements IMenuCreator  {

	protected final SchedulerView schedulerView;
	protected GanttChartViewer viewer;
	protected final EMFScheduleLabelProvider lp;
	private Menu lastMenu = null;

	
	public SchedulerViewAction(String name, int type, SchedulerView schedulerView, GanttChartViewer viewer, EMFScheduleLabelProvider lp) {
		super(name, type);
		this.schedulerView = schedulerView;
		this.viewer = viewer;
		this.lp = lp;
	}

	@Override
	public void run() {
		
		viewer.setInput(viewer.getInput());
		schedulerView.redraw();
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return this;
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

	abstract protected void createMenuItems(final Menu menu);

	@Override
	public void dispose() {
		if (lastMenu != null) {
			lastMenu.dispose();
			lastMenu = null;
		}
	}

}