/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.swt.SWT;

import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;

/**
 * A named group which report columns can be attached to. The configuration dialog allows the user to edit the placement and visibility of these groups, which affects all columns in the group. Saved
 * configurations reflect only the placement and visibility of the groups.
 * 
 * @author Simon McGregor
 * 
 */
public class ColumnBlock {
	private List<ColumnHandler> columnHandlers = new ArrayList<>();
	private boolean userVisible = false;

	private boolean modeVisible = false;
	public String blockID;
	public String blockName;
	private ColumnType columnType;
	/**
	 * Place holder columns are not purged.
	 */
	private boolean placeholder;
	public String tooltip;
	private GridColumnGroup gridColumnGroup;

	public ColumnBlock(final String blockID, final String blockName, final ColumnType columnType) {
		this.blockID = blockID;
		this.blockName = blockName;
		this.columnType = columnType;
	}

	public void addColumn(final ColumnHandler handler) {
		columnHandlers.add(handler);
		if (handler.column != null) {
			final GridColumn column = handler.column.getColumn();
			if (!column.isDisposed()) {
				column.setVisible(userVisible && modeVisible);
			}
		}
	}

	public void setUserVisible(final boolean visible) {
		this.userVisible = visible;
		updateVisibility();
	}

	protected void updateVisibility() {
		for (final ColumnHandler handler : columnHandlers) {
			if (handler.column != null) {
				final GridColumn column = handler.column.getColumn();
				if (!column.isDisposed()) {
					column.setVisible(userVisible && modeVisible);
				}
			}
		}
	}

	public boolean isModeVisible() {
		return modeVisible;
	}

	public void setViewState(final boolean isMultiple, final boolean isPinDiff) {
		switch (columnType) {
		case DIFF:
			modeVisible = isPinDiff;
			break;
		case MULTIPLE:
			modeVisible = isMultiple;
			break;
		case NORMAL:
		default:
			modeVisible = true;
			break;
		}
		updateVisibility();
	}

	public ColumnHandler findHandler(final GridColumn column) {
		for (final ColumnHandler handler : columnHandlers) {
			if (handler.column.getColumn() == column) {
				return handler;
			}
		}
		return null;
	}

	public void setColumnType(final ColumnType columnType) {
		this.columnType = columnType;
		updateVisibility();
	}

	public ColumnType getColumnType() {
		return columnType;
	}

	public boolean isPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(final boolean placeholder) {
		this.placeholder = placeholder;
	}

	public boolean getVisible() {
		return userVisible;
	}

	@Override
	public String toString() {
		return String.format("Column block '%s'", blockName);
	}

	public GridColumnGroup getOrCreateColumnGroup(final Grid grid) {
		if (gridColumnGroup == null) {
			if (columnHandlers.size() > 1) {
				gridColumnGroup = new GridColumnGroup(grid, SWT.NONE);
				gridColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
				gridColumnGroup.setText(blockName);
			}
		}
		return gridColumnGroup;
	}

	public List<ColumnHandler> getColumnHandlers() {
		return columnHandlers;
	}

}