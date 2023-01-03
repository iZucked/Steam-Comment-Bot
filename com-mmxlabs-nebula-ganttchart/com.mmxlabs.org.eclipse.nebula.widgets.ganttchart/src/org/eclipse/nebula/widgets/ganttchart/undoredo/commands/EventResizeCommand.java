/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.undoredo.commands;

import java.util.Calendar;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

/**
 * Represents one resize event in the chart that can be done/redone.
 * 
 * @author Emil
 *
 */
public class EventResizeCommand extends EventMoveCommand {

    /**
     * Creates a new undoable/redoable Resize Event.
     * 
     * @param event {@link GanttEvent} being moved
     * @param startDateBefore Estimated start date prior to move
     * @param startDateAfter Estimated start date after to move
     * @param endDateBefore Estimated end date before move
     * @param endDateAfter Estimated end date after move
     * @param revisedStartDateBefore Revised start date prior to move
     * @param revisedStartDateAfter Revised start date after move
     * @param revisedEndDateBefore Revised end date prior to move
     * @param revisedEndDateAfter Revised end date after move
     */
    public EventResizeCommand(final GanttEvent event, final Calendar startDateBefore, final Calendar startDateAfter, final Calendar endDateBefore, final Calendar endDateAfter, final Calendar revisedStartDateBefore, final Calendar revisedStartDateAfter, final Calendar revisedEndDateBefore, final Calendar revisedEndDateAfter) {
        super(event, startDateBefore, startDateAfter, endDateBefore, endDateAfter, revisedStartDateBefore, revisedStartDateAfter, revisedEndDateBefore, revisedEndDateAfter, null, null, -1, -1);
    }
    
}
