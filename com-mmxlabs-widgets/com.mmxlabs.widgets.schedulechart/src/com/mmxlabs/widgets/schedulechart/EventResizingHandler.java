package com.mmxlabs.widgets.schedulechart;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;

public class EventResizingHandler implements MouseListener, MouseMoveListener {

	private final ScheduleCanvas canvas;
	private final EventHoverHandler eventHoverHandler;
	
	public EventResizingHandler(ScheduleCanvas canvas, EventHoverHandler eventHoverHandler) {
		this.canvas = canvas;
		this.eventHoverHandler = eventHoverHandler;
		canvas.addMouseListener(this);
		canvas.addMouseMoveListener(this);
	}

	@Override
	public void mouseMove(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
