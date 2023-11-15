/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
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

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventAnnotation;

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
	private Optional<DrawableScheduleEventAnnotation> hoveredResizableAnnotation;
	private ScheduleEventAnnotation beforeResize;
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
		if (!settings.allowResizing()) return;
		
		if (resizing) {
			endX = e.x;

			EditableScheduleEventAnnotation resizableAnnotation = hoveredResizableAnnotation.get().getEditableAnnotation();
			List<LocalDateTime> dates = resizableAnnotation.getDates();
			LocalDateTime date = timeScale.getDateTimeAtX(endX).getFirst();
			if (hoveredOnStart && date.isBefore(dates.get(1))) {
				resizableAnnotation.setDate(0, date);
				canvas.redraw();
			} else if (date.isAfter(dates.get(0))) {
				resizableAnnotation.setDate(1, date);
				canvas.redraw();
			}

			return;
		}
		
		Optional<DrawableScheduleEventAnnotation> optResizableAnnotation = canvas.findAnnotations(e.x, e.y, RESIZE_HANDLE_RADIUS, RESIZE_HANDLE_RADIUS).stream()
				.filter(dse -> dse.getAnnotation() instanceof EditableScheduleEventAnnotation && dse.getScheduleEvent().getSelectionState() == ScheduleEventSelectionState.SELECTED)
				.reduce((f, s) -> s);
		
		if (optResizableAnnotation.isPresent()) {
			DrawableScheduleEventAnnotation resizableEvent = optResizableAnnotation.get();
			
			Rectangle b = resizableEvent.getBounds();
			int eventStartX = b.x;
			int eventEndX = b.x + b.width;
			
			if (Math.abs(eventStartX - e.x) < RESIZE_HANDLE_RADIUS || Math.abs(eventEndX - e.x) < RESIZE_HANDLE_RADIUS) {
				hoveredResizableAnnotation = optResizableAnnotation;
				hoveredOnStart = Math.abs(eventStartX - e.x) < RESIZE_HANDLE_RADIUS;
				canvas.setCursor(HAND_CURSOR);
				return;
			}
		}
		
		hoveredResizableAnnotation = Optional.empty();
		canvas.setCursor(ARROW_CURSOR);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Do nothing
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (!settings.allowResizing() || hoveredResizableAnnotation.isEmpty()) return;
		resizing = true;
		startX = e.x;
		beforeResize = new ScheduleEventAnnotation(hoveredResizableAnnotation.get().getAnnotation());
		canvas.fireScheduleEvent(l -> l.eventSelected(hoveredResizableAnnotation.get().getScheduleEvent(), e));
		eventHoverHandler.disable();
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (!settings.allowResizing() || !resizing) return;
		canvas.fireScheduleEvent(l -> l.annotationEdited(hoveredResizableAnnotation.get().getScheduleEvent(), beforeResize, hoveredResizableAnnotation.get().getAnnotation()));
		resizing = false;
		hoveredResizableAnnotation = Optional.empty();
		eventHoverHandler.enable();
	}

}
