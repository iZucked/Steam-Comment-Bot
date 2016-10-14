/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package org.eclipse.nebula.widgets.ganttchart;

public class VerticalDragModes {

    /**
     * Vertical dragging is disabled
     */
    public static final int NO_VERTICAL_DRAG      = 0;
    
    /**
     * Vertical dragging is enabled for any type of chart
     */
    public static final int ANY_VERTICAL_DRAG     = 1;
    
    /**
     * Vertical dragging is only enabled between two different {@link GanttSection}s
     */
    public static final int CROSS_SECTION_VERTICAL_DRAG = 2;

}
