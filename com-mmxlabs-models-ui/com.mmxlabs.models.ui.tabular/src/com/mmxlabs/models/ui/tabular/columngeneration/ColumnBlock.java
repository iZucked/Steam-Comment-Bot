/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
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
	public String configurationName;
	public final String blockOrderKey;
	private ColumnType columnType;
	private final String blockType;
	private final String blockGroup;
	/**
	 * Place holder columns are not purged.
	 */
	private boolean placeholder;
	// Coloum group behaviour
	private boolean expandable = false;
	private boolean expandByDefault = true;

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public boolean isExpandByDefault() {
		return expandByDefault;
	}

	public void setExpandByDefault(boolean expandByDefault) {
		this.expandByDefault = expandByDefault;
	}

	public String tooltip;
	private GridColumnGroup gridColumnGroup;
	private boolean forceGroup;

	public ColumnBlock(final String blockID, final String blockName, final String blockType, final String blockGroup, final String blockOrderKey, final ColumnType columnType, final String configurationName) {
		this.blockID = blockID;
		this.blockName = blockName;
		this.blockType = blockType;
		this.blockGroup = blockGroup;
		this.columnType = columnType;
		this.blockOrderKey = blockOrderKey;
		this.configurationName = configurationName;
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
		if (gridColumnGroup == null && grid != null) {
			if (forceGroup || columnHandlers.size() > 1) {
				int style = SWT.CENTER;
				if (expandable) {
					style |= SWT.TOGGLE;
				}

				gridColumnGroup = new GridColumnGroup(grid, style);
				gridColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());				
				gridColumnGroup.setText(blockName);
				gridColumnGroup.setExpanded(false);
			}
		}
		return gridColumnGroup;
	}

	public List<ColumnHandler> getColumnHandlers() {
		return columnHandlers;
	}

	@NonNull
	public String getblockType() {
		return blockType == null ? "default" : blockType;
	}

	@NonNull
	public String getblockGroup() {
		return blockGroup == null ? getblockType() : blockGroup;
	}

	public void setForceGroup(boolean forceGroup) {
		this.forceGroup = forceGroup;
	}

}