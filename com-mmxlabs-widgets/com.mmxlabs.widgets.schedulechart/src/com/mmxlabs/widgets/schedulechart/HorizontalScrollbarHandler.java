package com.mmxlabs.widgets.schedulechart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;

public class HorizontalScrollbarHandler {

	private final ScrollBar hBar;
	private final ScheduleTimeScale timeScale;

	private int lastScrollPosition;

	public HorizontalScrollbarHandler(ScrollBar horizontalBar, ScheduleTimeScale timeScale) {
		this.hBar = horizontalBar;
		this.timeScale = timeScale;

		hBar.addListener(SWT.Selection, this::handle);
		hBar.setMaximum(hBar.getMaximum() * timeScale.getUnitWidthForCurrentZoomLevel());
	}

	void handle(Event e) {
		int offset = hBar.getSelection() - lastScrollPosition;
		handle(offset, false);
	}

	void handle(MouseEvent e) {
		handle(e.count * 2 * timeScale.getUnitWidthForCurrentZoomLevel(), false);
	}

	void handle(KeyEvent e) {
		if (!(e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT)) {
			return;
		}

		int increment = timeScale.getUnitWidthForCurrentZoomLevel();
		int offset = (e.keyCode == SWT.ARROW_RIGHT) ? increment : -increment;
		int newPos = hBar.getSelection() + offset;
		if (newPos >= hBar.getMaximum() || newPos < hBar.getMinimum()) {
			return;
		}

		hBar.setSelection(newPos);
		handle(offset, true);
	}

	private void handle(int pixelOffset, boolean roundToBoundaries) {
		if (pixelOffset == 0) {
			return;
		}

		int actualPixelsScrolled = timeScale.scroll(pixelOffset, roundToBoundaries);
		lastScrollPosition = lastScrollPosition + actualPixelsScrolled;
		hBar.getParent().redraw();
	}

}