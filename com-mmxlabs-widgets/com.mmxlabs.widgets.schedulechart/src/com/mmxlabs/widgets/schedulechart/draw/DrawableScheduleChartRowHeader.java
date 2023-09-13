package com.mmxlabs.widgets.schedulechart.draw;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvas;
import com.mmxlabs.widgets.schedulechart.ScheduleChartMode;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.ScheduleChartRowKey;
import com.mmxlabs.widgets.schedulechart.ScheduleFilterSupport;
import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements.Padding;

public class DrawableScheduleChartRowHeader extends DrawableElement {
	
	private final Control parent;
	private final IScheduleChartSettings settings;
	private final IScheduleChartColourScheme colourScheme;
	private final ScheduleChartMode scm;
	private final ScheduleFilterSupport filterSupport;
	private final DrawableScheduleChartRow dscr;
	private DrawableCheckboxButton checkButton;

	public DrawableScheduleChartRowHeader(ScheduleCanvas parent, DrawableScheduleChartRow dscr, ScheduleFilterSupport filterSupport, ScheduleChartMode scm, IScheduleChartSettings settings) {
		this.parent = parent;
		this.dscr = dscr;
		this.filterSupport = filterSupport;
		this.scm = scm;
		this.colourScheme = settings.getColourScheme();
		this.settings = settings;
	}

	@Override
	protected List<BasicDrawableElement> getBasicDrawableElements(Rectangle bounds, DrawerQueryResolver queryResolver) {
		List<BasicDrawableElement> res = new ArrayList<>();
		final int checkBoxWidth = settings.filterModeCheckboxColumnWidth();
		res.add(BasicDrawableElements.Rectangle.withBounds(bounds.x, bounds.y, bounds.width, bounds.height).bgColour(colourScheme.getRowHeaderBgColour(dscr.getRowNum()))
					.borderColour(colourScheme.getRowOutlineColour(dscr.getRowNum())).create());
		
		boolean isFilterMode = scm == ScheduleChartMode.FILTER;
		if (isFilterMode) {
			final ScheduleChartRowKey rowKey = dscr.getScheduleChartRow().getKey();
			checkButton = new DrawableCheckboxButton(parent, !filterSupport.isRowHidden(rowKey)) {
				
				@Override
				public void checkAction() {
					filterSupport.toggleShowHide(rowKey);
					parent.redraw();
				}

			};
			checkButton.setBounds(new Rectangle(bounds.x, bounds.y, checkBoxWidth, bounds.height));
			res.addAll(checkButton.getBasicDrawableElements());
		}
		
		int textBoxStart = bounds.x + (isFilterMode ? checkBoxWidth : 0);
		String name = dscr.getScheduleChartRow().getName();
		int heightOfText = queryResolver.findSizeOfText(name, Display.getDefault().getSystemFont()).y;
		res.add(BasicDrawableElements.Text.from(textBoxStart, bounds.y + (bounds.height + 1) / 2 - heightOfText / 2, name).padding(new Padding(settings.getRowHeaderLeftPadding(), settings.getRowHeaderRightPadding(), 0, 0)).textColour(colourScheme.getRowHeaderTextColour(dscr.getRowNum())).create());
		return res;
	}
	
	public ScheduleChartRow getScheduleChartRow() {
		return dscr.getScheduleChartRow();
	}

	public DrawableCheckboxButton getCheckbox() {
		return checkButton;
	}

}
