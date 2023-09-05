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
	private final ScheduleCanvasState canvasState;

	private Menu menu = null;
	private Action hideRow = null;
	private Action showHiddenRows = null;
	private DrawableScheduleChartRow clickedRowHeader = null;

	public RowHeaderMenuHandler(final ScheduleCanvas canvas, final ScheduleCanvasState canvasState) {
		this.canvas = canvas;
		this.canvasState = canvasState;
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
				canvasState.getHiddenRowKeys().add(clickedRowHeader.getScheduleChartRow().getKey());
				canvas.redraw();
			}
		};
		ActionContributionItem hideRowACI = new ActionContributionItem(hideRow);
		hideRowACI.fill(m, -1);

		showHiddenRows = new Action("Show Hidden Rows", IAction.AS_PUSH_BUTTON) {
			@Override
			public void run() {
				canvasState.getHiddenRowKeys().clear();
				canvas.redraw();
			}
		};
		ActionContributionItem showHiddenRowsACI = new ActionContributionItem(showHiddenRows);
		showHiddenRowsACI.fill(m, -1);
	}
	
	private boolean clickedHeaderRegion(int x, int y) {
		return canvasState.getOriginalBounds().x <= x && x <= canvasState.getMainBounds().x;
	}

	@Override
	public void menuDetected(MenuDetectEvent e) {
		final Point p = canvas.toControl(e.x, e.y);
		if (!clickedHeaderRegion(p.x, p.y)) {
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
		
		showHiddenRows.setEnabled(!canvasState.getHiddenRowKeys().isEmpty());
	}

}
