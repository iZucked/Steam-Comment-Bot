package com.mmxlabs.widgets.schedulechart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;

import com.mmxlabs.widgets.schedulechart.draw.BasicDrawableElements;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleTimeScale;
import com.mmxlabs.widgets.schedulechart.draw.DrawerQueryResolver;
import com.mmxlabs.widgets.schedulechart.draw.GCBasedScheduleElementDrawer;
import com.mmxlabs.widgets.schedulechart.draw.ScheduleElementDrawer;

public class ScheduleCanvas extends Canvas {
	
	private Rectangle mainBounds;
	
	private final ScheduleTimeScale timeScale = new ScheduleTimeScale();
	private final DrawableScheduleTimeScale<ScheduleTimeScale> drawableTimeScale = new DrawableScheduleTimeScale<>(timeScale);

	private final HorizontalScrollbarHandler horizontalScrollbarHandler = new HorizontalScrollbarHandler(getHorizontalBar(), timeScale);

	public ScheduleCanvas(final Composite parent) {
		super(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED | SWT.V_SCROLL | SWT.H_SCROLL);
		
		initListeners();
	}
	
	private void initListeners() {
		addPaintListener(e -> ScheduleCanvas.this.repaint(e.gc));
		
		addMouseWheelListener(e -> {
			// TODO: change MOD1 and MOD2 to be dependent on settings object
			if (e.stateMask == SWT.MOD1) {
				timeScale.zoomBy(new Point(e.x, e.y), e.count * 5, e.count > 0);
			} else if (e.stateMask == SWT.MOD2) {
				horizontalScrollbarHandler.handle(e);
			} else {
				// vertical scroll
			}
			redraw();
		});
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				// empty for now
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				horizontalScrollbarHandler.handle(e);
				redraw();
			}
		});
	}

	private void repaint(GC gc) {
		GCBasedScheduleElementDrawer gcBasedDrawer = new GCBasedScheduleElementDrawer(gc);
		drawChartWithDrawer(gcBasedDrawer, gcBasedDrawer);
	}
	
	private void drawChartWithDrawer(ScheduleElementDrawer drawer, DrawerQueryResolver resolver) {
		// Always fill white to avoid occasional issue where we do not render over
		// the unused area correctly.
		drawer.drawOne(BasicDrawableElements.Rectangle.withBounds(getClientArea()).bgColour(Display.getDefault().getSystemColor(SWT.COLOR_WHITE)).create());

		mainBounds = getClientArea();
		timeScale.setBounds(getClientArea());
		drawableTimeScale.setBounds(getClientArea());
		drawer.drawOne(drawableTimeScale, resolver);
	}
	
	public final ScheduleTimeScale getTimeScale() {
		return timeScale;
	}

}
