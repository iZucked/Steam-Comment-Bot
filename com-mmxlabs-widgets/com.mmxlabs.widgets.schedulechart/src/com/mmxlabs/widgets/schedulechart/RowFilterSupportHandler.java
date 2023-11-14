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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.widgets.schedulechart.draw.DrawableCheckboxButton;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRowHeader;

public class RowFilterSupportHandler implements MenuDetectListener, MouseListener {
	
	private final ScheduleCanvas canvas;
	private final ScheduleFilterSupport filterSupport;

	private Menu menu = null;
	private Action showHideRow = null;
	private Action showHiddenRows = null;
	private DrawableScheduleChartRowHeader clickedRowHeader = null;

	public RowFilterSupportHandler(final ScheduleCanvas canvas, final ScheduleFilterSupport filterSupport) {
		this.canvas = canvas;
		this.filterSupport = filterSupport;
		this.menu = new Menu(canvas);
		createMenuItems(menu);
		canvas.setMenu(menu);
		
		canvas.addMenuDetectListener(this);
		canvas.addMouseListener(this);
	}

	public void dispose() {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
	}

	private void createMenuItems(Menu m) {
		showHideRow = new Action("", IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				filterSupport.toggleShowHide(clickedRowHeader.getScheduleChartRow().getKey());
				canvas.redraw();
			}
		};
		ActionContributionItem hideRowACI = new ActionContributionItem(showHideRow);
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
		
		Optional<DrawableScheduleChartRowHeader> optRowHeader = canvas.findRowHeader(p.x, p.y);
		if (optRowHeader.isPresent()) {
			showHideRow.setEnabled(true);
			showHideRow.setText(filterSupport.isRowHidden(optRowHeader.get().getScheduleChartRow().getKey()) ? "Show Row" : "Hide Row");
			clickedRowHeader = optRowHeader.get();
		} else {
			showHideRow.setEnabled(false);
			clickedRowHeader = null;
		}
		
		showHiddenRows.setEnabled(filterSupport.isFiltered());
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Optional<DrawableScheduleChartRowHeader> optRowHeader = canvas.findRowHeader(e.x, e.y);
		if (optRowHeader.isPresent()) {
			DrawableCheckboxButton btn =  optRowHeader.get().getCheckbox();
			if (btn != null && btn.getBounds().contains(e.x, e.y)) {
				btn.setChecked(!btn.getChecked());
			}
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// Do nothing
	}
	
}
