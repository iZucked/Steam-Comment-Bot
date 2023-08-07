package com.mmxlabs.widgets.schedulechart.draw;

import java.time.DayOfWeek;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.IScheduleChartColourScheme;

public class DefaultScheduleCanvasColourScheme implements IScheduleChartColourScheme {
	
	private static final Color BG = new Color(new RGB(230, 239, 249));

	@Override
	public Color getBaseBgColour() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	}

	@Override
	public Color getHeaderBgColour(int headerNum) {
		return BG;
	}

	@Override
	public Color getHeaderTextColour(int headerNum) {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public Color getHeaderOutlineColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}

	@Override
	public Color getRowHeaderBgColour(int rowNum) {
		return BG;
	}

	@Override
	public Color getRowHeaderTextColour(int rowNum) {
		return Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public Color getRowBgColour(int rowNum) {
		return BG;
	}

	@Override
	public Color getRowOutlineColour(int rowNum) {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}

	@Override
	public Color getDayBgColour(DayOfWeek d) {
		return BG;
	}

	@Override
	public Color getGridStrokeColour() {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}

}
