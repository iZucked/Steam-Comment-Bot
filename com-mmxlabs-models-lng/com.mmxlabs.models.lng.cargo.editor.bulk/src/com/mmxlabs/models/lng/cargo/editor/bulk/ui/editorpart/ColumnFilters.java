/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.ui.editorpart;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;


public class ColumnFilters {
	private Table<String, String, Boolean> groupFilter;
	private final Map<String, Boolean> typeFilter;
	private final String[] defaultTypeFilters;
	
	public ColumnFilters(String ... defaultTypeFilters) {
		this.typeFilter = new HashMap<>();
		this.groupFilter = HashBasedTable.create();
		this.defaultTypeFilters = defaultTypeFilters;
		resetDefaultTypeFilters();
	}
	
	public void resetDefaultTypeFilters() {
		typeFilter.clear();
		groupFilter.clear();
		for (String filter : defaultTypeFilters) {
			typeFilter.put(filter, true);
			groupFilter.put(filter,  filter, true);
		}
	}
	
	public void addGroupFilter(String type, String group) {
		typeFilter.put(type, true);
		groupFilter.put(type, group, true);
	}
	
	public void removeTypeFilter(String type) {
		typeFilter.remove(type);
		groupFilter.row(type).clear();
	}
	
	public void removeGroupFilter(String type, String group) {
		groupFilter.remove(type, group);
	}
	
	public boolean isColumnVisible(ColumnBlock block) {
		if (!checkColumnType(block)) {
			return false;
		}
		if (!checkColumnGroup(block)) {
			return false;
		}
		return true;
	}

	private boolean checkColumnType(ColumnBlock block) {
		if (typeFilter.containsKey(block.getblockType())) {
			return typeFilter.get(block.getblockType());
		} else {
			return false;
		}
	}

	private boolean checkColumnGroup(ColumnBlock block) {
		if (groupFilter.contains(block.getblockType(), block.getblockGroup())) {
			return groupFilter.get(block.getblockType(), block.getblockGroup());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return groupFilter.toString();
	}
	
}
