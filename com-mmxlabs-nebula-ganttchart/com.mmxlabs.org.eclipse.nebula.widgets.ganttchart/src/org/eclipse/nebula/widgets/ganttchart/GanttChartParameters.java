package org.eclipse.nebula.widgets.ganttchart;

import org.eclipse.nebula.widgets.ganttchart.label.EventLabelFontSize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * All numbers related to the sizes and spacings of ui elements of the Gantt
 * chart
 * 
 * @author Andrey Popov
 *
 */
public class GanttChartParameters {

	public static void updateFontSize(EventLabelFontSize size) {
		fontSize = size;
		recalculateTextVerticalExtent();
	}
	
	private static final String SAMPLE_STRING = "BliCDjgy[";
	
	/*
	 * Comes from the ratio of string extent to the size of letter "j" inside it
	 */
	private static final int LETTER_SCALE_FACTOR_NUMERATOR = 832;
	private static final int LETTER_SCALE_FACTOR_DENOMENATOR = 888;

	private static final EventLabelFontSize INITIAL_FONT_SIZE = // TODO read from preferences
			EventLabelFontSize.MEDIUM;
	private static EventLabelFontSize fontSize = INITIAL_FONT_SIZE;
	
	private static int currentTextVerticalExtent;
	
	static {
		recalculateTextVerticalExtent();
	}

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
	 * Getting actual string height
	 */
	private static void recalculateTextVerticalExtent() {
		// Preparation
		final int dummyImageSize = 2;
		final Image temporaryImage = new Image(Display.getDefault(), dummyImageSize, dummyImageSize);
		final GC temporaryGC = new GC(temporaryImage);
		final Font temporaryFont = GanttChartParameters.getStandardFont();
		temporaryGC.setFont(temporaryFont);
		
		// Recalculation
		currentTextVerticalExtent = temporaryGC.stringExtent(SAMPLE_STRING).y;
		
		// Resources disposal
		temporaryImage.dispose();
		temporaryFont.dispose();
		temporaryGC.dispose();
	}
	
	/**
	 * Get default font object with standard event label font height
	 * @return
	 */
	public static Font getStandardFont() {
		final String fontDataName = Display.getDefault().getSystemFont().getFontData()[0].getName();
		final int fontHeight = GanttChartParameters.getStandardEventLabelFontHeight();
		final FontData fontData = new FontData(fontDataName, fontHeight, SWT.NORMAL);
		return new Font(Display.getDefault(), fontData);
	}

	/**
	 * Size of the event label font height in points
	 */
	private static int getStandardEventLabelFontHeight() {
		return 3 * fontSize.getFontHeightInPixels() / 4;
	}

	private static int getStandartEventHeight() {
		return getActualTextExtent() + 2 * getEventLabelPadding();
	}
	
	private static int getActualTextExtent() {
		return currentTextVerticalExtent * LETTER_SCALE_FACTOR_NUMERATOR / LETTER_SCALE_FACTOR_DENOMENATOR;
	}

	public static int getRowHeight() {
		return getStandartEventHeight() + 2 * STANDART_FIXED_ROW_V_PADDING;
	}

	public static int getEventSpacerSize() {
		return STANDART_EVENT_SPACER_SIZE;
	}
	
	public static int getRowPadding() {
		return STANDART_FIXED_ROW_V_PADDING;
	}
	
	public static int getTextVerticalAlignDisplacement() {
		return getActualTextExtent() - currentTextVerticalExtent - fontSize.getMargin();
	}

	public static ISettings getSettings() {
		return new Settings();
	}

	public static class Settings extends AbstractSettings {

		// Private constructor to hide the public one
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

	public static int getEventLabelPadding() {
		return fontSize.getMargin();
	}
}
