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

package org.eclipse.nebula.widgets.ganttchart.themes;

import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.nebula.widgets.ganttchart.IColorManager;
import org.eclipse.swt.graphics.Color;

public class ColorThemeGrayBlue implements IColorManager {

	@Override
	public Color getArrowColor() {
		return ColorCache.getColor(0, 0, 0);
	}

	@Override
	public Color getReverseArrowColor() {
		return ColorCache.getColor(128, 0, 0);
	}

	@Override
	public Color getBlack() {
		return ColorCache.getColor(0, 0, 0);
	}

	@Override
	public Color getEventBorderColor() {
		return ColorCache.getColor(0, 0, 0);
	}

	@Override
	public Color getFadeOffColor1() {
		return ColorCache.getColor(147, 147, 147);
	}

	@Override
	public Color getFadeOffColor2() {
		return ColorCache.getColor(170, 170, 170);
	}

	@Override
	public Color getFadeOffColor3() {
		return ColorCache.getColor(230, 230, 230);
	}

	@Override
	public Color getLineColor() {
		return ColorCache.getColor(220, 220, 220);
	}

	@Override
	public Color getWeekDividerLineColor() {
		return ColorCache.getColor(75, 107, 143);
	}

	@Override
	public Color getPercentageBarColorTop() {
		return ColorCache.getColor(84, 84, 84);
	}
		
	@Override
	public Color getPercentageBarColorBottom() {
		return ColorCache.getColor(0, 0, 0);		
	}

	@Override
	public Color getPercentageBarRemainderColorTop() {
		return ColorCache.getColor(200, 200, 200);	
	}

	@Override
	public Color getPercentageBarRemainderColorBottom() {
		return ColorCache.getColor(111, 111, 111);
	}

	@Override
	public Color getTextColor() {
		return ColorCache.getColor(0, 0, 0);
	}

	@Override
	public Color getTodayBackgroundColorTop() {
		return ColorCache.getColor(217, 219, 189);
	}
	
	@Override
	public Color getTodayBackgroundColorBottom() {
		return ColorCache.getColor(225, 232, 228);
	}

	@Override
	public Color getTextHeaderBackgroundColorTop() {
		return ColorCache.getColor(173, 187, 210);
	}
	
	@Override
	public Color getTextHeaderBackgroundColorBottom() {
		return ColorCache.getColor(200, 209, 226);
	}

	@Override
	public Color getTimeHeaderBackgroundColorBottom() {
		return ColorCache.getColor(173, 187, 210);
	}

	@Override
	public Color getTimeHeaderBackgroundColorTop() {
		return ColorCache.getColor(200, 209, 226);
	}
	
	@Override
	public Color getHourTimeDividerColor() {
		return ColorCache.getColor(166, 183, 207);
	}

	@Override
	public Color getMonthTimeDividerColor() {
		return ColorCache.getColor(166, 183, 207);
	}

	@Override
	public Color getWeekTimeDividerColor() {
		return getMonthTimeDividerColor();
	}

	@Override
	public Color getYearTimeDividerColor() {
		return getHourTimeDividerColor();
	}

	@Override
	public Color getWeekdayBackgroundColorTop() {
		return ColorCache.getColor(236, 239, 244);
	}
		
	@Override
	public Color getWeekdayBackgroundColorBottom() {
		return getWhite();
	}

	@Override
	public Color getWhite() {
		return ColorCache.getWhite();
	}

	@Override
	public Color getSaturdayBackgroundColorTop() {
		return ColorCache.getColor(224, 228, 235);
	}
	
	@Override
	public Color getSaturdayBackgroundColorBottom() {
		return ColorCache.getColor(224, 228, 235);
	}

	@Override
	public Color getSaturdayTextColor() {
		return ColorCache.getColor(92, 75, 29);
	}

	@Override
	public Color getSundayBackgroundColorTop() {
		return getSaturdayBackgroundColorTop();
	}

	@Override
	public Color getSundayBackgroundColorBottom() {
		return getSaturdayBackgroundColorBottom();
	}
		
	@Override
	public Color getWeekdayTextColor() {
		return getBlack();
	}

	@Override
	public Color getSundayTextColor() {
		return getSaturdayTextColor();
	}

	@Override
	public Color getRevisedEndColor() {
		return ColorCache.getColor(255, 0, 0);
	}

	@Override
	public Color getRevisedStartColor() {
		return ColorCache.getColor(0, 180, 0);
	}

	@Override
	public Color getZoomBackgroundColorTop() {
		return ColorCache.getColor(131, 131, 131);
	}
		
	@Override
	public Color getZoomBackgroundColorBottom() {
		return ColorCache.getColor(71, 74, 62);
	}

	@Override
	public Color getZoomBorderColor() {
		return ColorCache.getWhite();
	}
	
	@Override
	public Color getZoomTextColor() {
		return ColorCache.getWhite();
	}

	@Override
	public Color getTooltipBackgroundColor() {
		return ColorCache.getColor(217, 229, 242);
	}

	@Override
	public Color getTooltipForegroundColor() {
		return getBlack();
	}

	@Override
	public Color getTooltipForegroundColorFaded() {
		return ColorCache.getColor(100, 100, 100);
	}

	@Override
	public Color getScopeBorderColor() {
		return getBlack();
	}

	@Override
	public Color getScopeGradientColorBottom() {
		return ColorCache.getColor(255, 255, 255);
	}

	@Override
	public Color getScopeGradientColorTop() {
		return ColorCache.getColor(98, 98, 98);
	}

	@Override
	public Color getTopHorizontalLinesColor() {
		return ColorCache.getColor(138, 175, 228);
	}

	@Override
	public Color getTodayLineColor() {
		return ColorCache.getColor(253, 145, 80);
	}

	@Override
	public int getTodayLineAlpha() {
		return 125;
	}

	@Override
	public int getWeekDividerAlpha() {
		return 50;
	}

	@Override
	public boolean useAlphaDrawing() {
		return false;
	}

	@Override
	public boolean useAlphaDrawingOn3DEventDropShadows() {
		return true;
	}

	@Override
	public Color getTickMarkColor() {
		return ColorCache.getColor(170, 164, 152);
	}

	@Override
	public Color getAdvancedTooltipBorderColor() {
		return ColorCache.getColor(118, 118, 118);
	}

	@Override
	public Color getAdvancedTooltipDividerColor() {
		return ColorCache.getColor(158, 187, 221);
	}

	@Override
	public Color getAdvancedTooltipDividerShadowColor() {
		return ColorCache.getColor(255, 255, 255);
	}

	@Override
	public Color getAdvancedTooltipInnerFillBottomColor() {
		return ColorCache.getColor(204, 217, 234);
	}

	@Override
	public Color getAdvancedTooltipInnerFillTopColor() {
		return ColorCache.getColor(255, 251, 252);
	}

	@Override
	public Color getAdvancedTooltipShadowCornerInnerColor() {
		return ColorCache.getColor(131, 131, 131);
	}

	@Override
	public Color getAdvancedTooltipShadowCornerOuterColor() {
		return ColorCache.getColor(148, 148, 148);
	}

	@Override
	public Color getAdvancedTooltipShadowInnerCornerColor() {
		return ColorCache.getColor(186, 186, 186);
	}

	@Override
	public Color getAdvancedTooltipTextColor() {
		return ColorCache.getColor(79, 77, 78);
	}

	@Override
	public Color getActiveSessionBarColorLeft() {
		return getTimeHeaderBackgroundColorTop();
	}

	@Override
	public Color getActiveSessionBarColorRight() {
		return getTimeHeaderBackgroundColorBottom();
	}

	@Override
	public Color getNonActiveSessionBarColorLeft() {
		return ColorCache.getColor(185, 195, 219);
	}

	@Override
	public Color getNonActiveSessionBarColorRight() {
		return ColorCache.getColor(199, 208, 225);
	}

	@Override
	public Color getSessionBarDividerColorLeft() {
		return ColorCache.getColor(176, 188, 213);
	}

	@Override
	public Color getSessionBarDividerColorRight() {
		return ColorCache.getColor(202, 210, 225);
	}	
	
	@Override
	public Color getSelectedDayColorBottom() {
		return ColorCache.getColor(200, 212, 222);
	}

	@Override
	public Color getSelectedDayColorTop() {
		return ColorCache.getColor(173, 192, 207);
	}

	@Override
	public Color getSelectedDayHeaderColorBottom() {
		return ColorCache.getColor(204, 216, 228);
	}

	@Override
	public Color getSelectedDayHeaderColorTop() {
		return ColorCache.getColor(222, 236, 250);
	}

    @Override
	public Color getPhaseHeaderBackgroundColorBottom() {
        return getTimeHeaderBackgroundColorBottom();
    }

    @Override
	public Color getPhaseHeaderBackgroundColorTop() {
        return getTimeHeaderBackgroundColorTop();
    }
	
	
}
