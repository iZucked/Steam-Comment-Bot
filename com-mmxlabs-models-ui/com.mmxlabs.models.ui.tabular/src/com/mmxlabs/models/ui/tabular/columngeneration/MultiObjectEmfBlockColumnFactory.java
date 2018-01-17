package com.mmxlabs.models.ui.tabular.columngeneration;
///**
// * Copyright (C) Minimax Labs Ltd., 2010 - 2015
// * All rights reserved.
// */
//package com.mmxlabs.models.ui.tabular.columngeneration;
//
//import org.eclipse.emf.ecore.ETypedElement;
//
//import com.mmxlabs.models.ui.tabular.ICellManipulator;
//import com.mmxlabs.models.ui.tabular.ICellRenderer;
//
//public class MultiObjectEmfBlockColumnFactory extends EmfBlockColumnFactory {
//	final String columnName;
//	final String blockID;
//	final String blockDisplayName;
//	final String tooltip;
//	final ColumnType columnType;
//	final ICellRenderer formatter;
//	final ICellManipulator manipulator;
//	final ETypedElement[][] path;
//	final String blockGroup;
//
//	public MultiObjectEmfBlockColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final String blockGroup, final ICellRenderer formatter, final ICellManipulator manipulator, final ETypedElement[][] path) {
//		this.blockID = blockID;
//		this.columnName = title;
//		this.blockDisplayName = title;
//		this.tooltip = tooltip;
//		this.columnType = columnType;
//		this.formatter = formatter;
//		this.manipulator = manipulator;
//		this.path = path;
//		this.blockGroup = blockGroup;
//	}
//
//	public MultiObjectEmfBlockColumnFactory(final String blockID, final String title, final String blockName, final String tooltip, final ColumnType columnType, final String blockGroup, final ICellRenderer formatter, final ICellManipulator manipulator,
//			final ETypedElement[][] path) {
//		this.blockID = blockID;
//		this.columnName = title;
//		this.blockDisplayName = blockName;
//		this.tooltip = tooltip;
//		this.columnType = columnType;
//		this.formatter = formatter;
//		this.manipulator = manipulator;
//		this.path = path;
//		this.blockGroup = blockGroup;
//	}
//
//	@Override
//	public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
//		final ColumnBlock block = blockManager.createBlock(blockID, blockDisplayName, blockGroup, columnType);
//		block.tooltip = tooltip;
//		return blockManager.createColumn(block, columnName, formatter, manipulator, path);
//	}
//
//}