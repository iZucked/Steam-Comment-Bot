/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.ui.tabular.columngeneration;

import java.util.function.Predicate;

import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.util.emfpath.EMFMultiPath;

public class SingleColumnFactoryBuilder {

	private String columnName;
	private String blockID;
	private ColumnType columnType = ColumnType.NORMAL;
	private String tooltip;

	private ICellRenderer renderer;
	private ICellManipulator manipulator;
	private Predicate<Object> rowFilter;
	private ETypedElement[] singleElementPath;
	private ETypedElement[][] multiElementPath;

	private EMFMultiPath multiPathForSorting;
	//
	private String blockType;
	private @Nullable String blockGroup;
	private String orderKey;
	private String blockConfigurationName;

	public SingleColumnFactoryBuilder(String blockID, String columnName) {
		this.blockID = blockID;
		this.columnName = columnName;
	}

	public BlockColumnFactoryDisplayNameChanger build() {
		return new BlockColumnFactoryDisplayNameChanger() {

			@Override
			public ColumnHandler addColumn(final ColumnBlockManager blockManager) {
				final ColumnBlock block = blockManager.createBlock(blockID, columnName, blockType, blockGroup, orderKey, columnType, blockConfigurationName);
				block.tooltip = tooltip;

				ColumnHandlerBuilder builder = blockManager.createColumn(block, columnName)//
						.withRowFilter(rowFilter) //
						.withCellRenderer(renderer) //
						.withCellManipulator(manipulator) //
						.withMultiPathForSorting(multiPathForSorting) //
				;
				if (multiElementPath != null) {
					builder.withMultiElementPath(multiElementPath);
				} else if (singleElementPath != null) { 
					builder.withElementPath(singleElementPath);
				} else {
					builder.withElementPath(new ETypedElement[0]);
				}
				return builder.build();
			}

			@Override
			public void setColumnNameByID(String columnID, String newName) {
				if (blockID.equals(columnID)) {
					columnName = newName;
				}
			}
		};
	}

	public SingleColumnFactoryBuilder withTooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public SingleColumnFactoryBuilder withBlockGroup(String blockGroup) {
		this.blockGroup = blockGroup;
		return this;
	}

	public SingleColumnFactoryBuilder withBlockType(String blockType) {
		this.blockType = blockType;
		return this;
	}

	public SingleColumnFactoryBuilder withOrderKey(String orderKey) {
		this.orderKey = orderKey;
		return this;
	}

	public SingleColumnFactoryBuilder withBlockConfigurationName(String blockConfigurationName) {
		this.blockConfigurationName = blockConfigurationName;
		return this;
	}

	public SingleColumnFactoryBuilder withColumnType(@NonNull ColumnType columnType) {
		this.columnType = columnType;
		return this;
	}

	public SingleColumnFactoryBuilder withElementPath(final ETypedElement... singleElementPath) {
		this.singleElementPath = singleElementPath;
		this.multiElementPath = null;
		return this;
	}

	public SingleColumnFactoryBuilder withMultiElementPath(final ETypedElement[][] multiElementPath) {
		this.multiElementPath = multiElementPath;
		this.singleElementPath = null;
		return this;
	}

	public SingleColumnFactoryBuilder withMultiPathForSorting(final EMFMultiPath multiPathForSorting) {
		this.multiPathForSorting = multiPathForSorting;
		return this;
	}

	public SingleColumnFactoryBuilder withCellRenderer(final ICellRenderer renderer) {
		this.renderer = renderer;
		return this;
	}

	public SingleColumnFactoryBuilder withCellManipulator(final ICellManipulator manipulator) {
		this.manipulator = manipulator;
		return this;
	}

	public <T extends ICellRenderer & ICellManipulator> SingleColumnFactoryBuilder withCellEditor(final T editor) {
		this.renderer = editor;
		this.manipulator = editor;
		return this;
	}

	/**
	 * Returns true to process the row, false to ignore the row
	 * 
	 * @param rowFilter
	 * @return
	 */
	public SingleColumnFactoryBuilder withRowFilter(@Nullable final Predicate<Object> rowFilter) {
		this.rowFilter = rowFilter;
		return this;
	}
	//
	// public SingleColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final Predicate<Object> rowFilter, final ICellRenderer formatter,
	// final ETypedElement... path) {
	// this(blockID, title, tooltip, columnType, null, null, null, rowFilter, formatter, null, null, path);
	// }
	//
	// public SingleColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
	// this(blockID, title, tooltip, columnType, null, null, null, null, formatter, null, null, path);
	// }
	//
	// public SingleColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final String blockType, String blockGroup, final String orderKey,
	// final ICellRenderer formatter, final ICellManipulator manipulator, final ETypedElement... path) {
	// this(blockID, title, tooltip, columnType, blockType, blockGroup, orderKey, null, formatter, manipulator, null, path);
	// }
	//
	// public SingleColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final String blockType, String blockGroup, final String orderKey,
	// final ICellRenderer formatter, final ICellManipulator manipulator, final String configurationName, final ETypedElement... path) {
	// this(blockID, title, tooltip, columnType, blockType, blockGroup, orderKey, null, formatter, manipulator, configurationName, path);
	// }
	//
	// public SingleColumnFactory(final String blockID, final String title, final String tooltip, final ColumnType columnType, final String blockType, String blockGroup, final String orderKey,
	// final Predicate<Object> rowFilter, final ICellRenderer formatter, final ICellManipulator manipulator, final String configurationName, final ETypedElement... path) {
	// this.blockID = blockID;
	// this.columnName = title;
	// this.blockDisplayName = title;
	// this.tooltip = tooltip;
	// this.columnType = columnType;
	// this.rowFilter = rowFilter;
	// this.formatter = formatter;
	// this.manipulator = manipulator;
	// this.path = path;
	// this.blockType = blockType;
	// this.blockGroup = blockGroup;
	// this.orderKey = orderKey;
	// this.blockConfigurationName = configurationName;
	// }
	//
	// protected SingleColumnFactory(final String blockID, final String title, final String blockName, final String tooltip, final ColumnType columnType, final String blockType, String blockGroup,
	// final String orderKey, final ICellRenderer formatter, final ICellManipulator manipulator, final String configurationName, final ETypedElement... path) {
	// this.blockID = blockID;
	// this.columnName = title;
	// this.blockDisplayName = blockName;
	// this.tooltip = tooltip;
	// this.columnType = columnType;
	// this.formatter = formatter;
	// this.manipulator = manipulator;
	// this.path = path;
	// this.blockType = blockType;
	// this.blockGroup = blockGroup;
	// this.orderKey = orderKey;
	// this.blockConfigurationName = configurationName;
	// }

}