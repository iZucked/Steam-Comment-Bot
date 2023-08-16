package com.mmxlabs.widgets.schedulechart;

import java.util.List;
import java.util.Optional;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleChartRow;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;

public class EventSelectionHandler implements MouseListener {
	
	private ScheduleCanvas canvas;
	private ScheduleCanvasState canvasState;

	public EventSelectionHandler(ScheduleCanvas canvas, ScheduleCanvasState canvasState) {
		this.canvas = canvas;
		this.canvasState = canvasState;
		canvas.addMouseListener(this);
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Optional<ScheduleEvent> optClickedEvent = findClickedEvent(e);
		
		if (optClickedEvent.isPresent()) {
			ScheduleEvent clickedEvent = optClickedEvent.get();
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
	
	Optional<ScheduleEvent> findClickedEvent(MouseEvent e) {
		final List<DrawableScheduleChartRow> drawnRows = canvasState.getLastDrawnContent();
		for (final DrawableScheduleChartRow row: drawnRows) {
			if (row.getBounds().contains(e.x, e.y)) {
				for (final DrawableScheduleEvent evt: row.getLastDrawnEvents()) {
					if (evt.getBounds().contains(e.x, e.y) && evt.getScheduleEvent().isVisible()) {
						return Optional.of(evt.getScheduleEvent());
					}
				}
				return Optional.empty();
			}
		}
		
		return Optional.empty();
	}

}
