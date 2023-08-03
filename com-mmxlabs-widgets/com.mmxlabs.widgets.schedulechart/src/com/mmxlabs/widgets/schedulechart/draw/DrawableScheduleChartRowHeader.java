package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;

public class DrawableScheduleChartRowHeader extends DrawableElement {
	
	private final List<ScheduleChartRow> rows;
	private final int totalHeaderHeight;

	private final Color bg = new Color(new RGB(230, 239, 249));
	private final Color black = new Color(new RGB(0, 0, 0));
	private final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

	public DrawableScheduleChartRowHeader(List<ScheduleChartRow> rows, int totalHeaderHeight) {
		this.rows = rows;
		this.totalHeaderHeight = totalHeaderHeight;
	}
	
	Optional<Integer> lockedHeaderY = Optional.empty();

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		int y = bounds.y;
		for (int i = 0; i < rows.size(); i++) {
			res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, y, bounds.width, ScheduleCanvas.SCHEDULE_CHART_ROW_HEIGHT).bgColour(bg).strokeColour(gray).create());
			res.add(BasicDrawableElements.Text.from(bounds.x, y, rows.get(i).getName() + " " + (i + 1)).padding(2).textColour(black).create());
			y += ScheduleCanvas.SCHEDULE_CHART_ROW_HEIGHT + 1;
		}
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, y, bounds.width, bounds.height - y).bgColour(bg).strokeColour(gray).create());
		
		// top left rectangle
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, lockedHeaderY.orElse(bounds.y), bounds.width, totalHeaderHeight).bgColour(bg).strokeColour(gray).create());
		return res;
	}
	
	public void setLockedHeaderY(int y) {
		lockedHeaderY = Optional.of(y);
	}
	
	public Optional<Integer> getLockedHeaderY() {
		return lockedHeaderY;
	}

}
