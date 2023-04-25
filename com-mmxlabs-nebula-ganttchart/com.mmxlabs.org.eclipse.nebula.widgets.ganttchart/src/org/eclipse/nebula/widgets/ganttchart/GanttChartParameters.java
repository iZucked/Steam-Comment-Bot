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
	
	private static final int STANDART_FIXED_ROW_HEIGHT = 28;
	private static final int STANDART_EVENT_SPACER_SIZE = 0;
	private static final int STANDART_EVENT_HEIGHT = 18;

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
			return 0;
		}

		@Override
		public int getMinimumSectionHeight() {
			return 5;
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
			return 1;
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
			return 0;
		}

		@Override
		public int getEventsBottomSpacer() {
			return 0;
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
			return 3;
		}

		@Override
		public int getSelectionLineStyle() {
			return SWT.LINE_SOLID;
		}

		@Override
		public int getHeaderMonthHeight() {
			return 22;
		}

		@Override
		public int getHeaderDayHeight() {
			return 22;
		}
		
		@Override
		public int getEventHeight() {
			return STANDART_EVENT_HEIGHT;
		}
	}
}
