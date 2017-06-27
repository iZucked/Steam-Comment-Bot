/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.parser.series;

/**
 * The dated average function needs to be "context" aware. We need to map between internal time units to a calendar month. Assume 1 represents January in the year of time zero. The month is the number
 * of months since this time. So January in the next year is 13.
 * 
 * @author Simon Goodall
 *
 */

public interface CalendarMonthMapper {

	int mapChangePointToMonth(int currentChangePoint);

	int mapMonthToChangePoint(int currentChangePoint);
}
