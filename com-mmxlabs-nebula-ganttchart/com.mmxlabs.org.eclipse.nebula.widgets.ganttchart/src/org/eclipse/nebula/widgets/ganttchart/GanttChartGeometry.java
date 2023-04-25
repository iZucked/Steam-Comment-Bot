package org.eclipse.nebula.widgets.ganttchart;

/**
 * All numbers related to the sizes and spacings of ui elements of the Gantt chart
 * 
 * The goal is to describe the geometry in terms of margin and padding
 * 
 * @author Andrey Popov
 *
 */
// TODO: Make geometry values dependent on User preferences
public class GanttChartGeometry {
	private static final int STANDART_FIXED_ROW_HEIGHT = 28;
	private static final int STANDART_EVENT_SPACER_SIZE = 0;
	
	public static int getRowHeight() {
		return STANDART_FIXED_ROW_HEIGHT;
	}
	
	public static int getEventSpacerSize() {
		return STANDART_EVENT_SPACER_SIZE;
	}
}
