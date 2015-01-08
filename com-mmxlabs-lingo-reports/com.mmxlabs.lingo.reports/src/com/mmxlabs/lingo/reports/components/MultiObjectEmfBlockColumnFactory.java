package com.mmxlabs.lingo.reports.components;

import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.lingo.reports.views.formatters.IFormatter;

public class MultiObjectEmfBlockColumnFactory extends EmfBlockColumnFactory {
	final String columnName;
	final String blockID;
	final String blockDisplayName;
	final String tooltip;
	final ColumnType columnType;
	final IFormatter formatter;
	final ETypedElement[][] path;

	public MultiObjectEmfBlockColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final IFormatter formatter, final ETypedElement[][] path) {
		this.blockID = blockID;
		this.columnName = title;
		this.blockDisplayName = title;
		this.tooltip = tooltip;
		this.columnType = columnType;
		this.formatter = formatter;
		this.path = path;
	}

	public MultiObjectEmfBlockColumnFactory(final String blockID, final String title, final String blockName, final String tooltip, final ColumnType columnType, final IFormatter formatter,
			final ETypedElement[][] path) {
		this.blockID = blockID;
		this.columnName = title;
		this.blockDisplayName = blockName;
		this.tooltip = tooltip;
		this.columnType = columnType;
		this.formatter = formatter;
		this.path = path;
	}

	@Override
	public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
		final ColumnBlock block = blockManager.createBlock(blockID, blockDisplayName, columnType);
		block.tooltip = tooltip;
		return blockManager.createColumn(block, columnName, formatter, path);
	}

}