package org.eclipse.nebula.widgets.ganttchart;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * All numbers related to the sizes and spacings of ui elements of the Gantt chart
 * 
 * @author Andrey Popov
 *
 */
public class GanttChartParameters {
	
	public static void updateFontSize(EventLabelFontSize size) {
		fontSize = size;
	}
	
	private static final EventLabelFontSize initialFontSize = EventLabelFontSize.MEDIUM;
	private static EventLabelFontSize fontSize = initialFontSize;
	
	private static final int STANDART_FIXED_ROW_V_PADDING = 8;
	
	private static final int STANDART_EVENT_SPACER_SIZE = 0;
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
	
	/**
	 * Size of the event label height in points
	 */
	public static int getStandartEventLabelFontHeight() {
		return 3 * fontSize.getFontHeightInPixels() / 4;
	}
	
	private static int getStandartEventVerticalPadding() {
		return Math.max(8, fontSize.getFontHeightInPixels() / 4);
	}
	
	private static int getStandartEventHeight() {
		return fontSize.getFontHeightInPixels() + 2 * getStandartEventVerticalPadding();
	}
	
	public static int getRowHeight() {
		return getStandartEventHeight() + 2 * STANDART_FIXED_ROW_V_PADDING;
	}
	
	public static int getEventSpacerSize() {
		return STANDART_EVENT_SPACER_SIZE;
	}
	
	public static ISettings getSettings() {
		return new Settings();
	}

	public static int preferenceDependentEventYDrawPosCorrection() {
		return (initialFontSize.getFontHeightInPixels() - fontSize.getFontHeightInPixels()) / 2;
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
			return getStandartEventHeight();
		}
	}
}
