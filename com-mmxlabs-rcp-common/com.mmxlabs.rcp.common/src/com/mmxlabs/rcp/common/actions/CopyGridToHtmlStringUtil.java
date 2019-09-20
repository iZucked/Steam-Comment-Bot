/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToHtmlStringUtil {

	protected static final Logger LOG = LoggerFactory.getLogger(CopyGridToHtmlStringUtil.class);
	protected final Grid table;

	private boolean rowHeadersIncluded = false;
	// Set border around everything?
	protected final boolean includeBorder = false;
	private boolean showBackgroundColours = false;
	private boolean showForegroundColours = false;
	private boolean includeAllColumns = false;
	protected IAdditionalAttributeProvider additionalAttributeProvider = null;
	protected static final String EOL = System.getProperty("line.separator");

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
				sw.write("<table border='0'>" + EOL);
			} else {
				sw.write("<table >" + EOL);

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

	protected void addPreTableRows(@NonNull final StringWriter sw) {
		final String[] additionalPreRows = getAdditionalPreRows();
		if (additionalPreRows != null) {
			for (final String r : additionalPreRows) {
				sw.write(r);
			}
		}
	}

	protected void addHeader(@NonNull final StringWriter sw) {
		// final int numColumns = table.getColumnCount();
		// write the head
		sw.write("<thead>" + EOL);

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
						combineAttributes(new String[] { colourString, String.format(" rowSpan='%d' ", table.getColumnGroupCount() > 0 ? 2 : 1) }, getAdditionalHeaderAttributes(column)));
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
					addCell(topRow, "th", columnGroup.getText(), combineAttributes(new String[] { colourString, String.format(" colSpan=%d ", groupCount) }, getAdditionalHeaderAttributes(column)));
				}
				// Add in the bottom row info.
				addCell(bottomRow, "th", column.getText(), combineAttributes(new String[] { colourString }, getAdditionalHeaderAttributes(column)));
			}

		}
		// If we have column groups, then write the top header row, otherwise skip it.
		if (!seenGroups.isEmpty()) {
			sw.write("<tr>");
			sw.write(topRow.toString());
			sw.write("</tr>" + EOL);
			sw.write("<tr>");
			sw.write(bottomRow.toString());
			sw.write("</tr>" + EOL + "</thead>" + EOL);
		} else {
			sw.write("<tr>");
			sw.write(singleRow.toString());
			sw.write("</tr>" + EOL + "</thead>" + EOL);

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

	protected void processTableRow(final StringWriter sw, final int numColumns, final GridItem item, final int[] rowOffsets) throws IOException {
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
				String colourString;
				if (showBackgroundColours) {
					if (c == null) {
						colourString = "bgcolor='white'";
					} else {
						colourString = String.format("bgcolor='#%02X%02X%02X'", c.getRed(), c.getGreen(), c.getBlue());
					}
				} else {
					colourString = "";
				}
				final String fontString;
				if (showForegroundColours) {
					final Color fc = item.getForeground(colIdx);
					if (fc != null) {
						String tempFontString = String.format("<font color='#%02X%02X%02X'>", fc.getRed(), fc.getGreen(), fc.getBlue());
						final Font font = item.getFont(colIdx);
						if (font != null) {
							final FontData fdata = font.getFontData()[0];
							if (fdata != null) {
								if (fdata.getStyle() == SWT.ITALIC) {
									tempFontString += "<i>";
								} else if (fdata.getStyle() == SWT.BOLD) {
									tempFontString += "<b>";
								} else if (fdata.getStyle() == (SWT.BOLD+SWT.ITALIC)) {
									tempFontString += "<i><b>";
								}
							}
						}
						fontString = tempFontString;
					} else {
						fontString = "";
					}
				} else {
					fontString = "";
				}
				addCell(sw, item.getText(colIdx),
						combineAttributes(new String[] { colourString, String.format(" rowSpan='%d' ", 1 + item.getRowSpan(colIdx)), String.format(" colSpan='%d' ", 1 + item.getColumnSpan(colIdx)) },
								getAdditionalAttributes(item, colIdx)), fontString);
				// Increment col idx.
				// FIXME: Does this work correctly for sortable columns? No existing reports to test against.
				i += item.getColumnSpan(colIdx);
				rowOffsets[colIdx] = item.getRowSpan(colIdx);
			} else {
				rowOffsets[colIdx] = rowOffsets[colIdx] - 1;
			}
		}
		sw.write("</tr>" + EOL);
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
		addCell(sw, text, attributes, "");
	}
	
	public final void addCell(final StringWriter sw, final String text, final String[] attributes, final String fontAttributes) {
		addCell(sw, "td", text, attributes, fontAttributes);
	}
	
	public final void addCell(final StringWriter sw, final String tag, String text, final String[] attributes) {
		addCell(sw, tag, text, attributes, "");
	}

	public final void addCell(final StringWriter sw, final String tag, String text, final String[] attributes, final String fontAttributes) {
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

		if (!fontAttributes.isEmpty()) {
			sw.write(fontAttributes);
		}
		sw.write(text);
		if (!fontAttributes.isEmpty()) {
			if (fontAttributes.contains("<b>")) {
				sw.write("</b>");
			}
			if (fontAttributes.contains("<i>")) {
				sw.write("</i>");
			}
			sw.write("</font>");
		}
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
	
	public final boolean isShowForegroundColours() {
		return showForegroundColours;
	}

	public final void setShowForegroundColours(final boolean showForegroundColours) {
		this.showForegroundColours = showForegroundColours;
	}

	public void setAdditionalAttributeProvider(final IAdditionalAttributeProvider additionalAttributeProvider) {
		this.additionalAttributeProvider = additionalAttributeProvider;
	}

}
