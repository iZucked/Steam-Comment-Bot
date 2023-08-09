package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;

public class DrawableScheduleChartRowHeaders extends DrawableElement {

	private final List<ScheduleChartRow> rows;
	private final int totalHeaderHeight;

	private final IScheduleChartColourScheme colourScheme;
	private final IScheduleChartSettings settings;

	public DrawableScheduleChartRowHeaders(List<ScheduleChartRow> rows, int totalHeaderHeight, IScheduleChartColourScheme colourScheme, IScheduleChartSettings settings) {
		this.rows = rows;
		this.totalHeaderHeight = totalHeaderHeight;
		this.colourScheme = colourScheme;
		this.settings = settings;
	}

	Optional<Integer> lockedHeaderY = Optional.empty();

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		final int height = settings.getRowHeight();
		int y = bounds.y;
		for (int i = 0; i < rows.size(); i++) {
			res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, y, bounds.width, height).bgColour(colourScheme.getRowHeaderBgColour(i))
					.borderColour(colourScheme.getRowOutlineColour(i)).create());
			res.add(BasicDrawableElements.Text.from(bounds.x, y, rows.get(i).getName()).padding(2).textColour(colourScheme.getRowHeaderTextColour(i)).create());
			y += height + 1;
		}
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, y, bounds.width, bounds.height - y).bgColour(colourScheme.getRowHeaderBgColour(-1))
				.borderColour(colourScheme.getRowOutlineColour(-1)).create());

		// top left rectangle
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, lockedHeaderY.orElse(bounds.y), bounds.width, totalHeaderHeight).bgColour(colourScheme.getRowHeaderBgColour(1))
				.borderColour(colourScheme.getRowOutlineColour(-1)).create());

		return res;
	}

	public void setLockedHeaderY(int y) {
		lockedHeaderY = Optional.of(y);
	}

	public Optional<Integer> getLockedHeaderY() {
		return lockedHeaderY;
	}

}
