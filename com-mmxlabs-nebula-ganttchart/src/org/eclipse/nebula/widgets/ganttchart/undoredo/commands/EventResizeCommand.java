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

package org.eclipse.nebula.widgets.ganttchart.undoredo.commands;

import java.util.Calendar;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;

// same as move just we ignore the section details
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
