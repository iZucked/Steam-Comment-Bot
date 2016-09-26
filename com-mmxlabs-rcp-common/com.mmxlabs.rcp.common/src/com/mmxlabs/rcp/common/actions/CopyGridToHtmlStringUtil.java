/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.graphics.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToHtmlStringUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CopyGridToHtmlStringUtil.class);
	private final Grid table;

	private boolean rowHeadersIncluded = false;
	// Set border around everything?
	private final boolean includeBorder = false;
	private boolean showBackgroundColours = false;
	private boolean includeAllColumns = false;
	private IAdditionalAttributeProvider additionalAttributeProvider = null;

	public CopyGridToHtmlStringUtil(final Grid table, final boolean includeRowHeaders, final boolean includeAllColumns) {

		this.table = table;
		this.rowHeadersIncluded = includeRowHeaders;
		this.includeAllColumns = includeAllColumns;
	}

	public String convert() {
		if (additionalAttributeProvider != null) {
			additionalAttributeProvider.begin();
		}
		try {
			final StringWriter sw = new StringWriter();

			// Note this may be zero if no columns have been defined. However an
			// implicit column will be created in such cases
			final int numColumns = table.getColumnCount();
			if (includeBorder) {
				sw.write("<table border='0'>\n");
			} else {
				sw.write("<table >\n");

			}
			try {
				addPreTableRows(sw);
				addHeader(sw);
				// Ensure at least 1 column to grab data
				final int numberOfColumns = Math.max(5, numColumns);
				final int[] rowOffsets = new int[numberOfColumns];

				for (final GridItem item : table.getItems()) {
					processTableRow(sw, numberOfColumns, item, rowOffsets);
				}
			} catch (final IOException e) {
				// should not occur, since we use a StringWriter
				LOG.error(e.getMessage(), e);
			}
			sw.write("</table>");

			return sw.toString();
		} finally {
			if (additionalAttributeProvider != null) {
				additionalAttributeProvider.done();
			}
		}
	}

	private void addPreTableRows(@NonNull final StringWriter sw) {
		final String[] additionalPreRows = getAdditionalPreRows();
		if (additionalPreRows != null) {
			for (final String r : additionalPreRows) {
				sw.write(r);
			}
		}
	}

	private void addHeader(@NonNull final StringWriter sw) {
		// final int numColumns = table.getColumnCount();
		// write the head
		sw.write("<thead>\n");

		// Create temporary writers to create both header rows concurrently.
		final StringWriter topRow = new StringWriter();
		final StringWriter bottomRow = new StringWriter();
		final StringWriter singleRow = new StringWriter();

		// top left blank cell
		if (rowHeadersIncluded) {
			addCell(topRow, "th", getTopLeftCellUpperText(), new String[] {});
			addCell(bottomRow, "th", getTopLeftCellLowerText(), new String[] {});
			addCell(singleRow, "th", getTopLeftCellText(), new String[] {});
		}
		// Set of column groups already seen. This assumes all columns within a group are next to each other
		final Set<GridColumnGroup> seenGroups = new HashSet<>();
		final int[] columnOrder = includeAllColumns ? getAllColumns(table) : table.getColumnOrder();
		for (final int i : columnOrder) {
			final GridColumn column = table.getColumn(i);
			if (!includeAllColumns && !column.isVisible()) {
				continue;
			}
			// Get the column group
			final GridColumnGroup columnGroup = column.getColumnGroup();
			final String colourString;
			if (showBackgroundColours) {
				colourString = "bgcolor='grey'";
			} else {
				colourString = "";
			}

			if (columnGroup == null) {
				// No group? Then cell bottom row cell should fill both header rows
				addCell(topRow, "th", column.getText(),
						combineAttributes(new String[] { colourString, String.format("rowSpan='%d'", table.getColumnGroupCount() > 0 ? 2 : 1) }, getAdditionalHeaderAttributes(column)));
				addCell(singleRow, "th", column.getText(), combineAttributes(new String[] { colourString }, getAdditionalHeaderAttributes(column)));
			} else {
				// Part of column group. Only add group if we have not previously seen it.
				if (seenGroups.add(columnGroup)) {
					int groupCount = 0;
					for (GridColumn c : columnGroup.getColumns()) {
						if (c.isVisible()) {
							groupCount++;
						}
					}
					addCell(topRow, "th", columnGroup.getText(), combineAttributes(new String[] { colourString, String.format("colSpan=%d", groupCount) }, getAdditionalHeaderAttributes(column)));
				}
				// Add in the bottom row info.
				addCell(bottomRow, "th", column.getText(), combineAttributes(new String[] { colourString }, getAdditionalHeaderAttributes(column)));
			}

		}
		// If we have column groups, then write the top header row, otherwise skip it.
		if (!seenGroups.isEmpty()) {
			sw.write("<tr>");
			sw.write(topRow.toString());
			sw.write("</tr>\n");
			sw.write("<tr>");
			sw.write(bottomRow.toString());
			sw.write("</tr>\n</thead>\n");
		} else {
			sw.write("<tr>");
			sw.write(singleRow.toString());
			sw.write("</tr>\n</thead>\n");

		}
	}

	@NonNull
	protected String getTopLeftCellText() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTopLeftCellText();
		}
		return "";
	}

	@NonNull
	protected String getTopLeftCellUpperText() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTopLeftCellUpperText();
		}
		return "";
	}

	@NonNull
	protected String getTopLeftCellLowerText() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getTopLeftCellLowerText();
		}
		return "";
	}

	private int @NonNull [] getAllColumns(final @NonNull Grid table) {
		final int[] indicies = new int[table.getColumnCount()];
		for (int i = 0; i < indicies.length; ++i) {
			indicies[i] = i;
		}
		return indicies;
	}

	private void processTableRow(final StringWriter sw, final int numColumns, final GridItem item, final int[] rowOffsets) throws IOException {
		// start a row
		sw.write("<tr>");

		if (rowHeadersIncluded) {
			addCell(sw, item.getHeaderText(), getAdditionalRowHeaderAttributes(item));
		}

		final int[] columnOrder = includeAllColumns ? getAllColumns(table) : table.getColumnOrder();
		for (int i = 0; i < columnOrder.length; ++i) {
			final int colIdx = columnOrder[i];
			final GridColumn column = table.getColumn(colIdx);
			if (!includeAllColumns && !column.isVisible()) {
				continue;
			}
			// If offset is greater than zero, skip this row
			if (rowOffsets[colIdx] == 0) {
				final Color c = item.getBackground(colIdx);
				final String colourString;
				if (showBackgroundColours) {
					if (c == null) {
						colourString = "bgcolor='white'";
					} else {
						colourString = String.format("bgcolor='#%02X%02X%02X'", c.getRed(), c.getGreen(), c.getBlue());
					}
				} else {
					colourString = "";
				}
				addCell(sw, item.getText(colIdx),
						combineAttributes(new String[] { colourString, String.format("rowSpan='%d'", 1 + item.getRowSpan(colIdx)), String.format("colSpan='%d'", 1 + item.getColumnSpan(colIdx)) },
								getAdditionalAttributes(item, colIdx)));
				// Increment col idx.
				// FIXME: Does this work correctly for sortable columns? No existing reports to test against.
				i += item.getColumnSpan(colIdx);
				rowOffsets[colIdx] = item.getRowSpan(colIdx);
			} else {
				rowOffsets[colIdx] = rowOffsets[colIdx] - 1;
			}
			// end row
			if ((i + 1) >= numColumns) {
				sw.write("</tr>\n");
			}
		}
	}

	private String[] combineAttributes(final String[] strings, final String[] additionalAttributes) {
		if (strings == null && additionalAttributes == null) {
			return new String[0];
		} else if (strings == null) {
			return additionalAttributes;
		} else if (additionalAttributes == null) {
			return strings;
		}

		final String[] combined = new String[strings.length + additionalAttributes.length];
		System.arraycopy(strings, 0, combined, 0, strings.length);
		System.arraycopy(additionalAttributes, 0, combined, strings.length, additionalAttributes.length);

		return combined;
	}

	private String[] getAdditionalRowHeaderAttributes(final GridItem item) {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getAdditionalRowHeaderAttributes(item);
		}
		return null;
	}

	private @NonNull String @Nullable [] getAdditionalPreRows() {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getAdditionalPreRows();
		}
		return null;
	}

	private String[] getAdditionalHeaderAttributes(final GridColumn column) {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getAdditionalHeaderAttributes(column);
		}
		return null;
	}

	private String[] getAdditionalAttributes(final GridItem item, final int i) {
		if (additionalAttributeProvider != null) {
			return additionalAttributeProvider.getAdditionalAttributes(item, i);
		}
		return null;
	}

	public final void addCell(final StringWriter sw, final String text, final String[] attributes) {
		addCell(sw, "td", text, attributes);
	}

	public final void addCell(final StringWriter sw, final String tag, String text, final String[] attributes) {
		if (attributes == null) {
			sw.write("<" + tag + ">");
		} else {
			sw.write("<" + tag);
			for (final String attribute : attributes) {
				sw.write(" " + attribute);
			}
			sw.write(">");
		}
		text = htmlEscape(text);

		if (text == null || text.equals("")) {
			text = "&nbsp;";
		}

		sw.write(text); // TODO: escape this for HTML
		sw.write("</" + tag + ">");
	}

	/**
	 * Does rough HTML escaping on a piece of text.
	 * 
	 * @param text
	 * @return
	 */
	public final String htmlEscape(final String text) {
		if (text == null) {
			return null;
		}
		return text //
				.replace("&", "&amp;") //
				.replace(">", "&gt;") //
				.replace("<", "&gt;") //
				.replace("\"", "&quot;") //
				.replace("'", "&#39;") //
		;
	}

	public final void setRowHeadersIncluded(final boolean b) {
		this.rowHeadersIncluded = b;
	}

	public final boolean isShowBackgroundColours() {
		return showBackgroundColours;
	}

	public final void setShowBackgroundColours(final boolean showBackgroundColours) {
		this.showBackgroundColours = showBackgroundColours;
	}

	public void setAdditionalAttributeProvider(final IAdditionalAttributeProvider additionalAttributeProvider) {
		this.additionalAttributeProvider = additionalAttributeProvider;
	}

}
