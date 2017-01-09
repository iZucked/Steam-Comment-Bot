/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart.dnd;

import java.util.List;

import org.eclipse.nebula.widgets.ganttchart.GanttEvent;
import org.eclipse.nebula.widgets.ganttchart.GanttSection;

/**
 * Convenience class for keeping track of vertical DND items
 * 
 * @author cre
 *
 */
public class VerticalDragDropManager {

    private GanttSection _targetSection;
    private GanttEvent   _topEvent;
    private GanttEvent   _bottomEvent;
    private List         _surroundingEvents;

    public GanttSection getTargetSection() {
        return _targetSection;
    }

    public void setTargetSection(final GanttSection targetSection) {
        _targetSection = targetSection;
    }

    public GanttEvent getTopEvent() {
        return _topEvent;
    }

    public void setTopEvent(final GanttEvent topEvent) {
        _topEvent = topEvent;
    }

    public GanttEvent getBottomEvent() {
        return _bottomEvent;
    }

    public void setBottomEvent(final GanttEvent bottomEvent) {
        _bottomEvent = bottomEvent;
    }

    public List getSurroundingEvents() {
        return _surroundingEvents;
    }

    public void setSurroundingEvents(final List surroundingEvents) {
        _surroundingEvents = surroundingEvents;
    }

    public void clear() {
        _targetSection = null;
        _topEvent = null;
        _bottomEvent = null;
        _surroundingEvents = null;
    }

}
