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
 * Similar to {@link CopyGridToJSONUtil} but assuming transposed view
 * 
 * @author Simon Goodall
 * 
 */
public class CopyTransposedGridToJSONUtil {

	protected static final Logger LOG = LoggerFactory.getLogger(CopyTransposedGridToJSONUtil.class);
	protected final Grid table;

	// Set border around everything?
	private boolean includeAllColumns = false;
	protected IAdditionalAttributeProvider additionalAttributeProvider = null;
	protected static final String EOL = System.getProperty("line.separator");

	public CopyTransposedGridToJSONUtil(final Grid table, final boolean includeAllColumns) {

		this.table = table;
		this.includeAllColumns = includeAllColumns;
	}

	public String convert() {
		if (additionalAttributeProvider != null) {
			additionalAttributeProvider.begin();
		}
		try {
			final JSONArray sw = new JSONArray();
			try {
				processTableRow(sw);
			} catch (final Exception e) {
				// should not occur, since we use a StringWriter
				LOG.error(e.getMessage(), e);
			}

			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(sw);
		} catch (JsonProcessingException e) {
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

	protected void processTableRow(final JSONArray parentArray) {

		final int[] columnOrder = includeAllColumns ? getAllColumns(table) : table.getColumnOrder();
		for (int i = 0; i < columnOrder.length; ++i) {
			final int colIdx = columnOrder[i];

			SimpleOrderedJSONObject colData = new SimpleOrderedJSONObject();
			parentArray.add(colData);

			GridColumn column = table.getColumn(colIdx);
			GridColumnGroup columnGroup = column.getColumnGroup();
			String prefix = columnGroup != null ? (columnGroup.getText()) : "";
			if (!prefix.isEmpty() && !column.getText().isEmpty()) {
				prefix = prefix + "-";
			}
			colData.put("item", prefix + column.getText());

			int rowIdx = 0;
			for (final GridItem item : table.getItems()) {
				++rowIdx;
				String text = item.getText(colIdx);
				if (text != null && !text.isEmpty()) {

					String key = item.getHeaderText();
					if (key.isEmpty()) {
						key = Integer.toString(rowIdx);
					}
					if (colData.containsKey(key)) {
						key = key + "-" + rowIdx;
					}
					colData.put(key, text);
				}
			}
		}
	}
}
