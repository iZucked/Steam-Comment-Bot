package com.mmxlabs.widgets.schedulechart;

import java.util.List;
import java.util.Optional;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class EventSelectionHandler implements MouseListener {
	
	private final ScheduleCanvas canvas;

	public EventSelectionHandler(ScheduleCanvas canvas) {
		this.canvas = canvas;
		canvas.addMouseListener(this);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Optional<DrawableScheduleEvent> optClickedEvent = canvas.findEvent(e.x, e.y);
		
		if (optClickedEvent.isPresent()) {
			ScheduleEvent clickedEvent = optClickedEvent.get().getScheduleEvent();
			canvas.fireScheduleEvent(l -> l.eventSelected(clickedEvent, List.of(clickedEvent), e));
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
