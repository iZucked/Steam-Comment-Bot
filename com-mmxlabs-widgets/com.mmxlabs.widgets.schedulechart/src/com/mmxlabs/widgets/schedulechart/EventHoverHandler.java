/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.Optional;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class EventHoverHandler implements MouseMoveListener, MouseListener {
	
	private final ScheduleCanvas canvas;
	private final ScheduleCanvasState canvasState;
	private boolean enabled = true;

	public EventHoverHandler(ScheduleCanvas canvas, ScheduleCanvasState canvasState) {
		this.canvas = canvas;
		this.canvasState = canvasState;
		canvas.addMouseMoveListener(this);
		canvas.addMouseListener(this);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		if (!enabled) return;
		Optional<DrawableScheduleEvent> optHoveredEvent = canvas.findEvent(e.x, e.y);
		if (optHoveredEvent.equals(canvasState.getHoveredEvent())) {
			return;
		}
		canvasState.getHoveredEvent().ifPresent(event -> event.getScheduleEvent().setSelectionState(canvasState.getSelectedEvents().contains(event.getScheduleEvent()) ? ScheduleEventSelectionState.SELECTED : ScheduleEventSelectionState.UNSELECTED));
		canvasState.setHoveredEvent((optHoveredEvent.isPresent() && !canvasState.getSelectedEvents().contains(optHoveredEvent.get().getScheduleEvent())) ? optHoveredEvent : Optional.empty());
		canvasState.getHoveredEvent().ifPresent(event -> event.getScheduleEvent().setSelectionState(ScheduleEventSelectionState.HOVER));
		canvas.redraw();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Do nothing
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// disable on right click and hold
		if (e.button == 3) enabled = false;
		canvasState.setHoveredEvent(Optional.empty());
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (e.button == 3) enabled = true;
		
	}

}
