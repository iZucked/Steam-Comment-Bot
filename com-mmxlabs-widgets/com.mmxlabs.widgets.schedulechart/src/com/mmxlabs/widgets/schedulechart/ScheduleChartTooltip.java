/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventTooltip;
import com.mmxlabs.widgets.schedulechart.draw.GCBasedScheduleElementDrawer;

public class ScheduleChartTooltip extends Shell {

	private DrawableScheduleEventTooltip currentEvent = null;
	private final GC fakeGC;
	private final GCBasedScheduleElementDrawer fakeDrawer;

	public ScheduleChartTooltip(final Shell parentShell, final int style) {
		super(parentShell, style);
		
		// Make shell not visible in case of ALT+TAB
        getDisplay().addFilter(SWT.Deactivate, e -> { 
            	setVisible(false);
        });

		// Create a local GC used for sizing outside of the paint events
		fakeGC = new GC(this);
		fakeDrawer = new GCBasedScheduleElementDrawer(fakeGC);

		this.addPaintListener(e -> {
			if (currentEvent != null) {
				final GC gc = e.gc;
				final var localDrawer = new GCBasedScheduleElementDrawer(gc);
				localDrawer.drawOne(currentEvent, localDrawer);
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Allow sub-classing
	}

	@Override
	public void dispose() {
		this.fakeGC.dispose();
		super.dispose();
	}

	/**
	 * Update the tool tip event to render
	 * 
	 * @param dt
	 * @param loc
	 *            Display co-ords for the tooltip location
	 */
	public void setTooltipEvent(@Nullable final DrawableScheduleEventTooltip dt, final Point loc) {
		// If the event has changed...
		if (currentEvent != dt) {
			this.currentEvent = dt;
			// Hide if no even
			if (this.currentEvent == null) {
				setVisible(false);
				return;
			}
			// Move the tooltip to the desired location
			setLocation(loc);
			// Compute the size of the tooltip and adjust shell size to the tool tip size
			final Point size = dt.computeSize(fakeDrawer);
			currentEvent.setBounds(new Rectangle(0,0, size.x, size.y));
			setSize(size);
		}
	}
	
	public void show() {
		this.setVisible(true);
		this.setEnabled(true);
		this.forceActive();
	}
	
	public void hide() {
		this.setVisible(false);
		this.setEnabled(false);
	}

}
