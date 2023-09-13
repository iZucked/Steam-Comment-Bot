/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.widgets.schedulechart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ScheduleFilterSupport {

	private final ScheduleCanvasState canvasState;
	private final IScheduleChartSettings settings;
	
	private final Map<String, Predicate<ScheduleChartRowKey>> activeFilters = new HashMap<>();
	private final Map<String, Predicate<ScheduleChartRowKey>> defaultFilters = new HashMap<>();

	public ScheduleFilterSupport(ScheduleCanvasState canvasState, IScheduleChartSettings settings) {
		this.canvasState = canvasState;
		this.settings = settings;
	}
	
	public void hideRow(ScheduleChartRowKey key) {
		canvasState.getHiddenRowKeys().add(key);
	}
	
	public void showRow(ScheduleChartRowKey key) {
		canvasState.getHiddenRowKeys().remove(key);
	}
	
	public void toggleShowHide(ScheduleChartRowKey key) {
		if (!canvasState.getHiddenRowKeys().add(key)) {
			canvasState.getHiddenRowKeys().remove(key);
		}
	}

	public void showHiddenRows() {
		canvasState.getHiddenRowKeys().clear();
		activeFilters.clear();
	}

	public boolean isFiltered() {
		return !canvasState.getHiddenRowKeys().isEmpty();
	}
	
	public boolean isRowHidden(ScheduleChartRowKey key) {
		return canvasState.getHiddenRowKeys().contains(key);
	}
	
	public void applyFilter(String name, Predicate<ScheduleChartRowKey> selector) {
		final List<ScheduleChartRowKey> keys = canvasState.getRows().stream().map(ScheduleChartRow::getKey).filter(selector).toList();
		canvasState.getHiddenRowKeys().addAll(keys);
		activeFilters.put(name, selector);
	}
	
	public void removeFilter(String name) {
		if (!activeFilters.containsKey(name)) {
			return;
		}
		final List<ScheduleChartRowKey> keys = canvasState.getRows().stream().map(ScheduleChartRow::getKey).filter(activeFilters.get(name)).toList();
		canvasState.getHiddenRowKeys().removeAll(keys);
		activeFilters.remove(name);
	}
	
	public boolean isFilterActive(String name) {
		return activeFilters.containsKey(name);
	}

	public void applyDefaultFilters() {
		for (var entry : defaultFilters.entrySet()) {
			applyFilter(entry.getKey(), entry.getValue());
		}
	}
	
	public void addDefaultFilter(String name, Predicate<ScheduleChartRowKey> p) {
		defaultFilters.put(name, p);
	}
	
	public void removeDefaultFilter(String name) {
		defaultFilters.remove(name);
	}

}
