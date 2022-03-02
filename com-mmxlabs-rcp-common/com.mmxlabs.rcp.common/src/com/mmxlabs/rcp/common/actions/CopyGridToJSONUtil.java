/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmxlabs.rcp.common.json.SimpleOrderedJSONObject;

/**
 * Copies "rendered" table contents into a JSONish format. (Note: keys could be duplicated and we have surplus commas)
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToJSONUtil {

	protected static final Logger LOG = LoggerFactory.getLogger(CopyGridToJSONUtil.class);
	protected final Grid table;

	// Set border around everything?
	private boolean includeAllColumns = false;
	protected IAdditionalAttributeProvider additionalAttributeProvider = null;
	protected static final String EOL = System.getProperty("line.separator");

	public CopyGridToJSONUtil(final Grid table, final boolean includeAllColumns) {

		this.table = table;
		this.includeAllColumns = includeAllColumns;
	}

	public String convert() {
		if (additionalAttributeProvider != null) {
			additionalAttributeProvider.begin();
		}
		try {
			final JSONArray sw = new JSONArray();

			// Note this may be zero if no columns have been defined. However an
			// implicit column will be created in such cases
			final int numColumns = table.getColumnCount();

			// Ensure at least 1 column to grab data
			final int numberOfColumns = Math.max(5, numColumns);
			final int[] rowOffsets = new int[numberOfColumns];

			for (final GridItem item : table.getItems()) {
				processTableRow(sw, item, rowOffsets);
			}

			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sw);
		} catch (final JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (additionalAttributeProvider != null) {
				additionalAttributeProvider.done();
			}
		}
	}

	private int @NonNull [] getAllColumns(final @NonNull Grid table) {
		final int[] indicies = new int[table.getColumnCount()];
		for (int i = 0; i < indicies.length; ++i) {
			indicies[i] = i;
		}
		return indicies;
	}

	protected void processTableRow(final JSONArray sw, final GridItem item, final int[] rowOffsets) {
		// start a row
		final SimpleOrderedJSONObject rowData = new SimpleOrderedJSONObject();
		sw.add(rowData);

		final int[] columnOrder = table.getColumnOrder();
		for (int i = 0; i < columnOrder.length; ++i) {
			final int colIdx = columnOrder[i];
			final GridColumn column = table.getColumn(colIdx);
			if (!includeAllColumns && !column.isVisible()) {
				continue;
			}
			// If offset is greater than zero, skip this row
			if (rowOffsets[colIdx] == 0) {

				SimpleOrderedJSONObject colData = rowData;
				// Get the column group
				final GridColumnGroup columnGroup = column.getColumnGroup();
				if (columnGroup != null) {
					String key = columnGroup.getText();
					if (key.isEmpty()) {
						key = Integer.toString(colIdx);
					}
					colData = (SimpleOrderedJSONObject) rowData.computeIfAbsent(key, k -> new SimpleOrderedJSONObject());
				}

				final String text = item.getText(colIdx);
				if (text != null && !text.isEmpty()) {

					// Empty key becomes column idx
					String key = column.getText();
					if (key.isEmpty()) {
						key = Integer.toString(colIdx);
					}

					//// Conflicting key - JSON object is really a Map, so we need unique keys

					// Try one - see of there is a column Id we can append
					if (colData.containsKey(key)) {
						String blockId = (String) column.getData("COLUMN_BLOCK_ID");
						if (blockId != null && !blockId.isBlank()) {
							key = key + "-" + blockId;
						}
					}
					// Try two, append column index (possibly again if the column had no name. This is good for uniqueness, but not repeatability as it depends on the order columns were added to the
					// table. Plugin.xml based code could happen in arbitrary order
					if (colData.containsKey(key)) {
						key = key + "-" + colIdx;
					}
					colData.put(key, text);
				}

				i += item.getColumnSpan(colIdx);
				rowOffsets[colIdx] = item.getRowSpan(colIdx);
			} else {
				rowOffsets[colIdx] = rowOffsets[colIdx] - 1;
			}
		}
	}
}
