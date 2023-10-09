/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.Optional;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;

public class RowHeaderMenuHandler implements MenuDetectListener {
	
	private final ScheduleCanvas canvas;
	private final ScheduleFilterSupport filterSupport;

	private Menu menu = null;
	private Action hideRow = null;
	private Action showHiddenRows = null;
	private DrawableScheduleChartRow clickedRowHeader = null;

	public RowHeaderMenuHandler(final ScheduleCanvas canvas, final ScheduleFilterSupport filterSupport) {
		this.canvas = canvas;
		this.filterSupport = filterSupport;
		this.menu = new Menu(canvas);
		createMenuItems(menu);
		canvas.setMenu(menu);
		
		canvas.addMenuDetectListener(this);
	}

	public void dispose() {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
	}

	private void createMenuItems(Menu m) {
		hideRow = new Action("Hide Row", IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				filterSupport.hideRow(clickedRowHeader.getScheduleChartRow().getKey());
				canvas.redraw();
			}
		};
		ActionContributionItem hideRowACI = new ActionContributionItem(hideRow);
		hideRowACI.fill(m, -1);

		showHiddenRows = new Action("Show Hidden Rows", IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				filterSupport.showHiddenRows();
				canvas.redraw();
			}
		};
		ActionContributionItem showHiddenRowsACI = new ActionContributionItem(showHiddenRows);
		showHiddenRowsACI.fill(m, -1);
	}
	
	@Override
	public void menuDetected(MenuDetectEvent e) {
		final Point p = canvas.toControl(e.x, e.y);
		if (!canvas.clickedRowHeaderRegion(p.x, p.y)) {
			e.doit = false;
			return;
		}
		
		Optional<DrawableScheduleChartRow> optRow = canvas.findRowHeader(p.x, p.y);
		if (optRow.isPresent()) {
			hideRow.setEnabled(true);
			clickedRowHeader = optRow.get();
		} else {
			hideRow.setEnabled(false);
			clickedRowHeader = null;
		}
		
		showHiddenRows.setEnabled(filterSupport.isFiltered());
	}

}
