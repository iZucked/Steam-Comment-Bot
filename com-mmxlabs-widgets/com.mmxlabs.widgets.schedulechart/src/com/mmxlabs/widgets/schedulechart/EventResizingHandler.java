package com.mmxlabs.widgets.schedulechart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class EventResizingHandler implements MouseListener, MouseMoveListener {
	
	private static final int RESIZE_HANDLE_RADIUS = 2;
	private static final Cursor ARROW_CURSOR = Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW);
	private static final Cursor HAND_CURSOR = Display.getDefault().getSystemCursor(SWT.CURSOR_SIZEWE);
	

	private final ScheduleCanvas canvas;
	private final ScheduleTimeScale timeScale;
	private final IScheduleChartSettings settings;
	private final EventHoverHandler eventHoverHandler;
	
	private boolean resizing = false;
	private int startX = 0;
	private int endX = 0;
	private Optional<DrawableScheduleEvent> hoveredResizableEvent;
	private boolean hoveredOnStart;
	
	public EventResizingHandler(ScheduleCanvas canvas, ScheduleTimeScale timeScale, IScheduleChartSettings settings, EventHoverHandler eventHoverHandler) {
		this.canvas = canvas;
		this.timeScale = timeScale;
		this.settings = settings;
		this.eventHoverHandler = eventHoverHandler;
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (!settings.allowWindowResizing()) return;
		
		if (resizing) {
			endX = e.x;

			ScheduleEvent resizableEvent = hoveredResizableEvent.get().getScheduleEvent();
			LocalDateTime date = timeScale.getDateTimeAtX(endX).getFirst();
			if (hoveredOnStart && date.isBefore(resizableEvent.getWindowEndDate())) {
				resizableEvent.setWindowStartDate(date);
				canvas.redraw();
			} else if (date.isAfter(resizableEvent.getWindowStartDate())) {
				resizableEvent.setWindowEndDate(date);
				canvas.redraw();
			} else {
//				resizing = false;
			}

//			canvas.redraw();
			return;
		}
		
		Optional<DrawableScheduleEvent> optResizableEvent = canvas.findEvents(e.x, e.y, RESIZE_HANDLE_RADIUS, 0).stream().filter(dse -> dse.getScheduleEvent().isResizable()).reduce((f, s) -> s);
		
		if (optResizableEvent.isPresent()) {
			DrawableScheduleEvent resizableEvent = optResizableEvent.get();
			
			Rectangle b = resizableEvent.getBounds();
			int eventStartX = b.x;
			int eventEndX = b.x + b.width;
			
			if (Math.abs(eventStartX - e.x) < RESIZE_HANDLE_RADIUS || Math.abs(eventEndX - e.x) < RESIZE_HANDLE_RADIUS) {
				hoveredResizableEvent = optResizableEvent;
				hoveredOnStart = Math.abs(eventStartX - e.x) < RESIZE_HANDLE_RADIUS;
				canvas.setCursor(HAND_CURSOR);
				return;
			}
		}
		
		hoveredResizableEvent = Optional.empty();
		canvas.setCursor(ARROW_CURSOR);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (!settings.allowWindowResizing() || hoveredResizableEvent.isEmpty()) return;
		resizing = true;
		startX = e.x;
		canvas.fireScheduleEvent(l -> l.eventSelected(hoveredResizableEvent.get().getScheduleEvent(), List.of(hoveredResizableEvent.get().getScheduleEvent()), e));
		eventHoverHandler.disable();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (!settings.allowWindowResizing() || !resizing) return;
		canvas.fireScheduleEvent(l -> l.eventResized(hoveredResizableEvent.get().getScheduleEvent()));
		hoveredResizableEvent.get().getScheduleEvent().updateWindow();
		resizing = false;
		hoveredResizableEvent = Optional.empty();
		eventHoverHandler.enable();
	}

}
