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
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

abstract class AbstractSettings implements ISettings {

	@Override
	public String getDateFormat() {
		return "MM/dd/yyyy";
	}

	@Override
	public String getHourDateFormat() {
		return "MM/dd/yyyy HH:mm";
	}
	
	@Override
	public String getWeekHeaderTextDisplayFormatTop() {
		return "MMM dd, ''yy";
	}
	
	@Override
	public String getMonthHeaderTextDisplayFormatTop() {
		return "MMMMM ''yy";
	}
	
	@Override
	public String getDayHeaderTextDisplayFormatTop() {
		return "MMM dd, HH:mm";
	}

	@Override
	public String getYearHeaderTextDisplayFormatTop() {
		return "yyyy";
	}
	
	@Override
	public String getDayHeaderTextDisplayFormatBottom() {
		return "HH:mm";
	}

	@Override
	public String getMonthHeaderTextDisplayFormatBottom() {
		return "MMM dd";
	}

	@Override
	public String getWeekHeaderTextDisplayFormatBottom() {
		return "E";
	}

	@Override
	public String getYearHeaderTextDisplayFormatBottom() {
		return "MMM";
	}

	@Override
	public Color getDefaultEventColor() {
		return ColorCache.getColor(181, 180, 181);
	}

	@Override
	public Color getDefaultGradientEventColor() {
		return ColorCache.getColor(235, 235, 235);
	}

	@Override
	public boolean showPropertiesMenuOption() {
		return true;
	}

	@Override
	public boolean showDeleteMenuOption() {
		return true;
	}

	@Override
	public boolean adjustForLetters() {
		return true;
	}

	@Override
	public boolean consumeEventWhenOutOfRange() {
		return false;
	}

	@Override
	public boolean enableAutoScroll() {
		return true;
	}

	@Override
	public boolean enableResizing() {
		return true;
	}

	@Override
	public int getArrowConnectionType() {
		return CONNECTION_MS_PROJECT_STYLE;
	}

	@Override
	public int getDayHorizontalSpacing() {
		return 3;
	}

	@Override
	public int getDayVerticalSpacing() {
		return 3;
	}

	@Override
	public int getDayWidth() {
		return 16;
	}

	@Override
	public int getEventHeight() {
		return 12;
	}

	@Override
	public int getEventPercentageBarHeight() {
		return 3;
	}

	@Override
	public int getHeaderMonthHeight() {
		return 18;
	}

	@Override
	public int getHeaderDayHeight() {
		return 18;
	}

	@Override
	public int getInitialView() {
		return VIEW_WEEK;
	}

	@Override
	public int getInitialZoomLevel() {
		return ZOOM_DAY_NORMAL;
	}

	@Override
	public int getMonthDayWidth() {
		return 6;
	}

	@Override
	public int getResizeBorderSensitivity() {
		return 3;
	}

	@Override
	public int getTextSpacerConnected() {
		return 9;
	}

	@Override
	public int getTextSpacerNonConnected() {
		return 9;
	}

	@Override
	public int getYearMonthDayWidth() {
		return 3;
	}

	@Override
	public boolean moveLinkedEventsWhenEventsAreMoved() {
		return true;
	}

	@Override
	public boolean showArrows() {
		return true;
	}

	@Override
	public boolean showBarsIn3D() {
		return true;
	}

	@Override
	public boolean showBoldScopeText() {
		return true;
	}

	@Override
	public boolean showDateTips() {
		return true;
	}

	@Override
	public boolean showPlannedDates() {
		return false;
	}

	@Override
	public boolean showGradientEventBars() {
		return true;
	}

	@Override
	public boolean showNumberOfDaysOnBars() {
		return false;
	}

	@Override
	public boolean showOnlyDependenciesForSelectedItems() {
		return false;
	}

	@Override
	public boolean showToolTips() {
		return true;
	}

	@Override
	public int getEventSpacer() {
		return 12;
	}
	
	@Override
	public boolean enableDragAndDrop() {
		return true;
	}

	@Override
	public boolean showZoomLevelBox() {
		return true;
	}

	@Override
	public boolean allowInfiniteHorizontalScrollBar() {
		return true;
	}

	@Override
	public boolean showResizeDateTipOnBorders() {
		return true;
	}

	@Override
	public boolean allowBlankAreaDragAndDropToMoveDates() {
		return true;
	}

	@Override
	public boolean flipBlankAreaDragDirection() {
		return true;
	}

	@Override
	public boolean drawSelectionMarkerAroundSelectedEvent() {
		return true;
	}

	@Override
	public boolean allowCheckpointResizing() {
		return false;
	}

	@Override
	public boolean showMenuItemsOnRightClick() {
		return true;
	}

	@Override
	public int getArrowHeadEventSpacer() {
		return 1;
	}

	@Override
	public int getArrowHeadVerticalAdjuster() {
		return 0;
	}

	@Override
	public Calendar getStartupCalendarDate() {
		return Calendar.getInstance(Locale.getDefault());
	}

	@Override
	public int getCalendarStartupDateOffset() {
		return -4;
	}

	@Override
	public boolean startCalendarOnFirstDayOfWeek() {
		return false;
	}

	@Override
	public int getMoveAreaNegativeSensitivity() {
		return 6;
	}

	@Override
	public boolean enableZooming() {
		return true;
	}
	
	@Override
	public Image getLockImage() {
		return ImageCache.getImage("icons/lock_tiny.gif");
	}

	@Override
	public String getTextDisplayFormat() {
		return "#name# (#pc#%)";
	}

	@Override
	public int getRevisedLineSpacer() {
		return 3;
	}
	
	@Override
	public Image getDefaultAdvandedTooltipHelpImage() {
		return null;
	}

	@Override
	public Image getDefaultAdvandedTooltipImage() {
		return null;
	}

	@Override
	public boolean roundHourlyEventsOffToNearestHour() {
		return false;
	}
		
	@Override
	public String getDefaultAdvancedTooltipHelpText() {
		return null;
	}

	@Override
	public String getDefaultAdvancedTooltipTitle() {
		return "\\b\\c027050082#name#";
	}

	@Override
	public String getDefaultAdvancedTooltipTextExtended() {
		StringBuffer buf = new StringBuffer();
		buf.append("\\ceRevised: #rs# - #re# (#reviseddays# days)\n");
		buf.append("\\c100100100Planned: #sd# - #ed# (#days# days)\n");
		buf.append("#pc#% complete");
		return buf.toString();//"\\ceStart Date: \\b#sd#\nEnd Date: \\b#ed#\nRevised Start: \\b#rs#\nRevised End: \\b#re#\nDay Span: \\b#days# days\nPercent Complete: \\b#pc#%";
	}

	@Override
	public String getDefaultAdvancedTooltipText() {
		StringBuffer buf = new StringBuffer();
		buf.append("\\cePlanned: #sd# - #ed# (#days# days)\n");
		buf.append("\\c100100100#pc#% complete");
		return buf.toString();
	}

	@Override
	public int getTodayLineStyle() {
		return SWT.LINE_SOLID;
	}

	@Override
	public int getTodayLineWidth() {
		return 2;
	}

	@Override
	public int getTodayLineVerticalOffset() {
		return getHeaderMonthHeight();
	}

	@Override
	public int getVerticalTickMarkOffset() {
		return (getHeaderMonthHeight()-5 > 0 ? getHeaderMonthHeight()-5 : 0);
	}

	@Override
	public boolean drawHeader() {
		return true;
	}

	@Override
	public int getEventsTopSpacer() {
		return 12;
	}

	@Override
	public int getEventsBottomSpacer() {
		return 12;
	}

	@Override
	public int getSectionBarDividerHeight() {
		return 5;
	}

	@Override
	public int getSectionBarWidth() {
		return 20;
	}

	@Override
	public int getMinimumSectionHeight() {
		return 80;
	}

	@Override
	public boolean drawFullPercentageBar() {
		return true;
	}

	@Override
	public int getPercentageBarAlpha() {
		return 255;
	}

	@Override
	public int getRemainderPercentageBarAlpha() {
		return 70;
	}

	@Override
	public int getAdvancedTooltipXOffset() {
		return 15;
	}

	@Override
	public int getDragAllModifierKey() {
		return SWT.SHIFT;
	}

	@Override
	public int getZoomWheelModifierKey() {
		return SWT.MOD1;
	}

	@Override
	public Locale getDefaultLocale() {
		return Locale.getDefault();
	}

	@Override
	public boolean getUseAdvancedTooltips() {
		return true;
	}

	@Override
	public boolean enableLastDraw() {
		return false;
	}

	@Override
	public boolean useSplitArrowConnections() {
		return true;
	}
	
	@Override
	public int getReverseDependencyLineHorizontalSpacer() {
		return 2;
	}
		
	@Override
	public boolean drawVerticalLines() {
		return true;
	}

	@Override
	public boolean drawHorizontalLines() {
		return false;
	}

/*	public boolean useFastDraw() {
		return true;
	}
*/
	@Override
	public int getSectionSide() {
		return SWT.LEFT;
	}

	@Override
	public boolean drawLockedDateMarks() {
		return false;
	}

	@Override
	public boolean showDateTipsOnScrolling() {
		return true;
	}

	@Override
	public boolean drawFillsToBottomWhenUsingGanttSections() {
		return false;
	}

	@Override
	public boolean drawGanttSectionBarToBottom() {
		return false;
	}

	@Override
	public boolean lockHeaderOnVerticalScroll() {
		return false;
	}
	
	@Override
	public boolean showDefaultMenuItemsOnEventRightClick() {
		return true;
	}

	@Override
	public boolean allowScopeMenu() {
		return false;
	}
	
	@Override
	public boolean allowHeaderSelection() {
		return true;
	}

	@Override
	public boolean zoomToMousePointerDateOnWheelZooming() {
		return true;
	}

	@Override
	public Calendar getDDayRootCalendar() {
		Calendar mDDayCalendar = Calendar.getInstance(getDefaultLocale());
		mDDayCalendar.set(Calendar.YEAR, mDDayCalendar.get(Calendar.YEAR));
		mDDayCalendar.set(Calendar.MONTH, Calendar.JANUARY);
		mDDayCalendar.set(Calendar.DATE, 1);
		mDDayCalendar.set(Calendar.HOUR, 0);
		mDDayCalendar.set(Calendar.MINUTE, 0);
		mDDayCalendar.set(Calendar.SECOND, 0);
		mDDayCalendar.set(Calendar.MILLISECOND, 0);
		return mDDayCalendar;
	}

	@Override
	public int getDDaySplitCount() {
		return 10;
	}

	@Override
	public boolean drawEventsDownToTheHourAndMinute() {
		return false;
	}

	@Override
	public boolean moveAndResizeOnlyDependentEventsThatAreLaterThanLinkedMoveEvent() {
		return false;
	}

	@Override
	public boolean forceMouseWheelVerticalScroll() {
		return false;
	}

    @Override
	public int getSectionTextSpacer() {
        return 30;
    }

    @Override
	public int getPhasesHeaderHeight() {
        return 18;
    }

    @Override
	public boolean allowPhaseOverlap() {
        return false;
    }

	
}
