package com.mmxlabs.widgets.schedulechart;

import java.util.Optional;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;

public class EventHoverHandler implements MouseMoveListener {
	
	private final ScheduleCanvas canvas;
	private final ScheduleCanvasState canvasState;

	public EventHoverHandler(ScheduleCanvas canvas, ScheduleCanvasState canvasState) {
		this.canvas = canvas;
		this.canvasState = canvasState;
		canvas.addMouseMoveListener(this);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		Optional<ScheduleEvent> optHoveredEvent = canvas.findEvent(e.x, e.y);
		if (optHoveredEvent.equals(canvasState.getHoveredEvent())) {
			return;
		}
		canvasState.getHoveredEvent()
				.ifPresent(event -> event.setSelectionState(canvasState.getSelectedEvents().contains(event) ? ScheduleEventSelectionState.SELECTED : ScheduleEventSelectionState.UNSELECTED));
		canvasState.setHoveredEvent((optHoveredEvent.isPresent() && !canvasState.getSelectedEvents().contains(optHoveredEvent.get())) ? optHoveredEvent : Optional.empty());
		canvasState.getHoveredEvent().ifPresent(event -> event.setSelectionState(ScheduleEventSelectionState.HOVER));
		canvas.redraw();
	}

}
