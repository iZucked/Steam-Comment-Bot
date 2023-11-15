/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.List;
import java.util.Optional;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventAnnotation;

public class EventSelectionHandler implements MouseListener {
	
	private final ScheduleCanvas canvas;

	public EventSelectionHandler(ScheduleCanvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Optional<ScheduleEvent> optClickedEvent = canvas.findEvent(e.x, e.y).map(DrawableScheduleEvent::getScheduleEvent).or(() -> canvas.findAnnotation(e.x, e.y).map(DrawableScheduleEventAnnotation::getScheduleEvent));
		
		if (optClickedEvent.isPresent()) {
			ScheduleEvent clickedEvent = optClickedEvent.get();
			
			canvas.fireScheduleEvent(l -> l.eventSelected(clickedEvent, e));
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// Do nothing
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// Do nothing
	}
	
}
