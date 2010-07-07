/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.nebula.widgets.ganttchart;

import java.util.Calendar;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.GC;

public class GanttEventListenerAdapter implements IGanttEventListener {

	@Override
	public void eventDoubleClicked(GanttEvent event, MouseEvent me) {
	}

	@Override
	public void eventPropertiesSelected(List<GanttEvent> events) {
	}

	@Override
	public void eventsDeleteRequest(List<GanttEvent> events, MouseEvent me) {
	}

	@Override
	public void eventSelected(GanttEvent event, List<GanttEvent> allSelectedEvents, MouseEvent me) {
	}

	@Override
	public void eventsMoved(List<GanttEvent> events, MouseEvent me) {
	}

	@Override
	public void eventsResized(List<GanttEvent> events, MouseEvent me) {
	}
	
	@Override
	public void eventsMoveFinished(List<GanttEvent> events, MouseEvent me) {
	}

	@Override
	public void eventsResizeFinished(List<GanttEvent> events, MouseEvent me) {
	}

	@Override
	public void zoomedIn(int newZoomLevel) {
	}

	@Override
	public void zoomedOut(int newZoomLevel) {
	}

	@Override
	public void zoomReset() {
	}
	
	@Override
	public void eventHeaderSelected(Calendar newlySelectedDate, List<Calendar> allSelectedDates) {
	}

	@Override
	public void lastDraw(GC gc) {
	}

    @Override
	public void phaseMoved(GanttPhase phase, MouseEvent me) {
    }

    @Override
	public void phaseMoveFinished(GanttPhase phase, MouseEvent me) {
    }

    @Override
	public void phaseResized(GanttPhase phase, MouseEvent me) {
    }

    @Override
	public void phaseResizeFinished(GanttPhase phase, MouseEvent me) {
    }

}
