package org.eclipse.nebula.widgets.ganttchart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * All numbers related to the sizes and spacings of ui elements of the Gantt chart
 * 
 * @author Andrey Popov
 *
 */
 // TODO: Make geometry values dependent on User preferences
public class GanttChartParameters {

	/*
	 * Preferably should be a number that is divisible by 4
	 */
	private static final int STANDART_EVENT_LABEL_FONT_HEIGHT_PIXELS = 16;
	
	/**
	 * Size of the event label height in points
	 */
	public static final int STANDART_EVENT_LABEL_FONT_HEIGHT = 3 * STANDART_EVENT_LABEL_FONT_HEIGHT_PIXELS / 4;

	private static final int STANDART_EVENT_SPACER_SIZE = 0;

	private static final int STANDART_EVENT_V_PADDING = Math.max(8, STANDART_EVENT_LABEL_FONT_HEIGHT_PIXELS / 4);
	private static final int STANDART_EVENT_HEIGHT = STANDART_EVENT_LABEL_FONT_HEIGHT_PIXELS + 2 * STANDART_EVENT_V_PADDING;

	private static final int STANDART_FIXED_ROW_V_PADDING = 8;
	private static final int STANDART_FIXED_ROW_HEIGHT = STANDART_EVENT_HEIGHT + 2 * STANDART_FIXED_ROW_V_PADDING;

	private static final int MINIMUM_SECTION_HEIGHT = 5;
	private static final int SECTION_TEXT_SPACER_SIZE = 0;
	private static final int SECTION_BAR_DIVIDER_HEIGHT = 1;
	private static final int EVENTS_TOP_SPACER = 0;
	private static final int EVENTS_BOTTOM_SPACER = 0;
	private static final int SELECTION_LINE_WIDTH = 3;
	private static final int HEADER_MONTH_HEIGHT = 22;
	private static final int HEADER_DAY_HEIGHT = 22;

	private GanttChartParameters() {
		
	}
	
	public static int getRowHeight() {
		return STANDART_FIXED_ROW_HEIGHT;
	}
	
	public static int getEventSpacerSize() {
		return STANDART_EVENT_SPACER_SIZE;
	}
	
	public static ISettings getSettings() {
		return new Settings();
	}
	
	public static class Settings extends AbstractSettings {
		

		private Settings() {
			
		}
		
		@Override
		public boolean enableResizing() {
			return false;
		}

		@Override
		public boolean useSplitArrowConnections() {
			return false;
		}

		@Override
		public Color getDefaultEventColor() {
			return ColorCache.getColor(221, 220, 221);
		}

		@Override
		public boolean showPlannedDates() {
			return false;
		}

		@Override
		public String getTextDisplayFormat() {
			return "#name#";
		}

		@Override
		public int getSectionTextSpacer() {
			return SECTION_TEXT_SPACER_SIZE;
		}

		@Override
		public int getMinimumSectionHeight() {
			return MINIMUM_SECTION_HEIGHT;
		}

		@Override
		public int getNumberOfDaysToAppendForEndOfDay() {
			return 0;
		}

		@Override
		public boolean allowBlankAreaVerticalDragAndDropToMoveChart() {
			return true;
		}

		@Override
		public boolean lockHeaderOnVerticalScroll() {
			return true;
		}

		@Override
		public boolean drawFillsToBottomWhenUsingGanttSections() {
			return true;
		}

		@Override
		public int getSectionBarDividerHeight() {
			return SECTION_BAR_DIVIDER_HEIGHT;
		}

		@Override
		public boolean showGradientEventBars() {
			return false;
		}

		@Override
		public boolean drawSectionsWithGradients() {
			return false;
		}

		@Override
		public boolean allowArrowKeysToScrollChart() {
			return true;
		}

		@Override
		public boolean showBarsIn3D() {
			return false;
		}

		@Override
		public int getEventsTopSpacer() {
			return EVENTS_TOP_SPACER;
		}

		@Override
		public int getEventsBottomSpacer() {
			return EVENTS_BOTTOM_SPACER;
		}

		@Override
		public boolean showDeleteMenuOption() {
			return false;
		}

		@Override
		public boolean showMenuItemsOnRightClick() {
			return true;
		}

		@Override
		public boolean showDefaultMenuItemsOnEventRightClick() {
			return false;
		}

		@Override
		public int getSelectionLineWidth() {
			return SELECTION_LINE_WIDTH;
		}

		@Override
		public int getSelectionLineStyle() {
			return SWT.LINE_SOLID;
		}

		@Override
		public int getHeaderMonthHeight() {
			return HEADER_MONTH_HEIGHT;
		}

		@Override
		public int getHeaderDayHeight() {
			return HEADER_DAY_HEIGHT;
		}
		
		@Override
		public int getEventHeight() {
			return STANDART_EVENT_HEIGHT;
		}
	}
}
