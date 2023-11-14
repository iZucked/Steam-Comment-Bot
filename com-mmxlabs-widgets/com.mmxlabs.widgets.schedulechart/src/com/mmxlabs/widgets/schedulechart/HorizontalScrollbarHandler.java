/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;

import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartEventListenerAdapter;

public class HorizontalScrollbarHandler {

	private final ScrollBar hBar;
	private final ScheduleTimeScale timeScale;
	
	private final int scrollSensitivity = 10;

	private int lastScrollPosition;

	public HorizontalScrollbarHandler(ScrollBar horizontalBar, ScheduleTimeScale timeScale) {
		this.hBar = horizontalBar;
		this.timeScale = timeScale;

		hBar.addListener(SWT.Selection, this::handle);
		hBar.setIncrement(scrollSensitivity);
		hBar.setMaximum(hBar.getMaximum() * getUnitWidthForCurrentZoomLevel());
	}

	void handle(Event e) {
		int offset = hBar.getSelection() - lastScrollPosition;
		handle(offset, false);
		hBar.getParent().redraw();
	}

	void handle(MouseEvent e) {
		handle(e.count * 2 * getUnitWidthForCurrentZoomLevel(), false);
	}

	void handle(KeyEvent e) {
		if (!(e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT)) {
			return;
		}

		int increment = getUnitWidthForCurrentZoomLevel();
		int offset = (e.keyCode == SWT.ARROW_RIGHT) ? increment : -increment;
		int newPos = hBar.getSelection() + offset;
		// Check expected position is within bounds
		if (newPos >= (hBar.getMaximum() - hBar.getThumb()) || newPos < hBar.getMinimum()) {
			return;
		}

		hBar.setSelection(newPos);
		handle(offset, true);
	}
	
	IScheduleChartEventListener getEventListener() {
		return new ScheduleChartEventListenerAdapter() {
			@Override
			public void timeScaleZoomLevelChanged(Rectangle mainBounds, IScheduleChartContentBoundsProvider boundsProvider) {
				int leftmostX = timeScale.getXForDateTime(boundsProvider.getLeftmostEvent().getStart());
				int rightmostX = timeScale.getXForDateTime(boundsProvider.getRightmostEvent().getEnd());
				
				if (leftmostX >= mainBounds.x && rightmostX <= mainBounds.x + mainBounds.width) {
					// all the content fits
					hBar.setVisible(false);
					return;
				}
				
				int max = timeScale.getTotalContentWidthInUnits() * getUnitWidthForCurrentZoomLevel();
				int pos = timeScale.getScrollPositionInUnits() * getUnitWidthForCurrentZoomLevel();

				hBar.setMaximum(max);
				hBar.setSelection(pos);
				hBar.setVisible(true);
				// Adjust the scroll bar thumb to the correct size
				hBar.setThumb(timeScale.getBounds().width);
				// Update the scroll position for new zoom level
				lastScrollPosition = pos;
			}
		};
	}

	private void handle(int pixelOffset, boolean roundToBoundaries) {
		if (pixelOffset == 0) {
			return;
		}

		int actualPixelsScrolled = timeScale.scroll(pixelOffset, roundToBoundaries);
		lastScrollPosition = lastScrollPosition + actualPixelsScrolled;
		//hBar.getParent().redraw();
	}
	
	private int getUnitWidthForCurrentZoomLevel() {
		// assumes width unit is a regular unit
		return timeScale.getUnitWidthFromRegularUnit(timeScale.getUnitWidths().zoomLevel().getWidthUnit()).get();
	}

	/**
	 * Call when bounds change to update the scroll bar thumb size
	 * 
	 * @param width
	 */
	public void updateWidth(int width) {
		hBar.setThumb(width);
	}

}