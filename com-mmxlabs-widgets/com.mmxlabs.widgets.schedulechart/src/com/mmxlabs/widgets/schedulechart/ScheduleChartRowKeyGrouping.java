/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

/**
 * Defines some grouping or parent for some schedule chart rows.
 * This doesn't necessarily mean that grouped rows appear contiguously.
 * 
 * For example, in the NinetyDayScheduleReport, this is used to group a row into its parent scenario.
 * @author Ashvin
 */
public record ScheduleChartRowKeyGrouping(String name, Object data) {}
