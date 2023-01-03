/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class SimpleEmfBlockColumnFactory extends EmfBlockColumnFactory implements BlockColumnFactoryDisplayNameChanger{
	String columnName;
	protected final String blockID;
	protected String blockDisplayName;
	final String tooltip;
	protected final ColumnType columnType;
	final ICellRenderer formatter;
	final ICellManipulator manipulator;
	final ETypedElement[] path;
	protected final String blockType;
	protected final String blockGroup;
	protected final String orderKey;
	protected String blockConfigurationName;

	public SimpleEmfBlockColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		this(blockID, title, tooltip, columnType, null, null, null, formatter, null, null, path);
	}
	
	public SimpleEmfBlockColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final String blockType, String blockGroup, final String orderKey,
			final ICellRenderer formatter, final ICellManipulator manipulator, final ETypedElement... path) {
		this(blockID, title, tooltip, columnType, blockType, blockGroup, orderKey, formatter, manipulator, null, path);
	}

	public SimpleEmfBlockColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final String blockType, String blockGroup, final String orderKey,
			final ICellRenderer formatter, final ICellManipulator manipulator, final String configurationName, final ETypedElement... path) {
		this.blockID = blockID;
		this.columnName = title;
		this.blockDisplayName = title;
		this.tooltip = tooltip;
		this.columnType = columnType;
		this.formatter = formatter;
		this.manipulator = manipulator;
		this.path = path;
		this.blockType = blockType;
		this.blockGroup = blockGroup;
		this.orderKey = orderKey;
		this.blockConfigurationName = configurationName;
	}

	protected SimpleEmfBlockColumnFactory(final String blockID, final String title, final String blockName, final String tooltip, final ColumnType columnType, final String blockType,
			String blockGroup, final String orderKey, final ICellRenderer formatter, final ICellManipulator manipulator, final String configurationName, final ETypedElement... path) {
		this.blockID = blockID;
		this.columnName = title;
		this.blockDisplayName = blockName;
		this.tooltip = tooltip;
		this.columnType = columnType;
		this.formatter = formatter;
		this.manipulator = manipulator;
		this.path = path;
		this.blockType = blockType;
		this.blockGroup = blockGroup;
		this.orderKey = orderKey;
		this.blockConfigurationName = configurationName;
	}

	@Override
	public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
		final ColumnBlock block = blockManager.createBlock(blockID, blockDisplayName, blockType, blockGroup, orderKey, columnType, blockConfigurationName);
		block.tooltip = tooltip;
		return blockManager.createColumn(block, columnName, formatter, manipulator, path);
	}

	@Override
	public void setColumnNameByID(String columnID, String newName) {
		if (blockID.equals(columnID)) {
			blockDisplayName = newName;
			columnName = newName;
		}
	}
}