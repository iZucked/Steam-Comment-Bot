/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.html.HtmlEscapers;
import com.mmxlabs.rcp.common.internal.Activator;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToHtmlClipboardAction extends Action {

	private static final Logger LOG = LoggerFactory.getLogger(CopyGridToHtmlClipboardAction.class);
	private final Grid table;

	private boolean rowHeadersIncluded = false;

	public CopyGridToHtmlClipboardAction(final Grid table, final boolean includeRowHeaders) {

		super("Copy");

		this.table = table;
		this.rowHeadersIncluded = includeRowHeaders;

		setText("Copy");
		setDescription("Copies grid data into the clipboard");
		setToolTipText("Copies grid data into the clipboard");
		setImageDescriptor(Activator.getImageDescriptor("/icons/copy.gif"));
	}

	@Override
	public void run() {

		final StringWriter sw = new StringWriter();

		// Note this may be zero if no columns have been defined. However an
		// implicit column will be created in such cases
		final int numColumns = table.getColumnCount();

		sw.write("<table border='1'>\n");
		try {
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

		// Create a new clipboard instance
		final Display display = Display.getDefault();
		final Clipboard cb = new Clipboard(display);
		try {
			// Create the text transfer and set the contents
			final TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { sw.toString() }, new Transfer[] { textTransfer });
		} finally {
			// Clean up our local resources - system clipboard now has the data
			cb.dispose();
		}
	}

	private void addHeader(final StringWriter sw) {
		final int numColumns = table.getColumnCount();
		// write the head
		sw.write("<thead>\n");

		// Create temporary writers to create both header rows concurrently.
		final StringWriter topRow = new StringWriter();
		final StringWriter bottomRow = new StringWriter();

		// top left blank cell
		if (rowHeadersIncluded) {
			addCell(topRow, "th", "", new String[] { "bgcolor='grey'" });
			addCell(bottomRow, "th", "", new String[] { "bgcolor='grey'" });
		}
		// Set of column groups already seen. This assumes all columns within a group are next to each other
		final Set<GridColumnGroup> seenGroups = new HashSet<>();
		for (int i = 0; i < numColumns; ++i) {

			final GridColumn column = table.getColumn(i);
			// Get the column group
			final GridColumnGroup columnGroup = column.getColumnGroup();
			if (columnGroup == null) {
				// No group? Then cell bottom row cell should fill both header rows
				addCell(topRow, "th", column.getText(), new String[] { "bgcolor='grey'", String.format("rowSpan='%d'", table.getColumnCount() > 0 ? 2 : 1) });
			} else {
				// Part of column group. Only add group if we have not previously seen it.
				if (seenGroups.add(columnGroup)) {
					addCell(topRow, "th", columnGroup.getText(), new String[] { "bgcolor='grey'", String.format("colSpan=%d", columnGroup.getColumns().length) });
				}
				// Add in the bottom row info.
				addCell(bottomRow, "th", column.getText(), new String[] { "bgcolor='grey'" });
			}

		}
		// If we have column groups, then write the top header row, otherwise skip it.
		if (!seenGroups.isEmpty()) {
			sw.write("<tr>");
			sw.write(topRow.toString());
			sw.write("</tr>\n");
		}
		sw.write("<tr>");
		sw.write(bottomRow.toString());
		sw.write("</tr>\n</thead>\n");
	}

	private void processTableRow(final StringWriter sw, final int numColumns, final GridItem item, final int[] rowOffsets) throws IOException {
		// start a row
		sw.write("<tr>");

		if (rowHeadersIncluded) {
			addCell(sw, item.getHeaderText(), new String[] { "bgcolor='gray'" });
		}

		for (int i = 0; i < numColumns; ++i) {
			// If offset is greater than zero, skip this row
			if (rowOffsets[i] == 0) {
				final Color c = item.getBackground(i);
				final String colourString;
				if (c == null) {
					colourString = "bgcolor='white'";
				} else {
					colourString = String.format("bgcolor='#%02X%02X%02X'", c.getRed(), c.getGreen(), c.getBlue());
				}
				addCell(sw, item.getText(i), new String[] { colourString, String.format("rowSpan='%d'", 1 + item.getRowSpan(i)), String.format("colSpan='%d'", 1 + item.getColumnSpan(i)) });
				// Increment col idx.
				i += item.getColumnSpan(i);
				rowOffsets[i] = item.getRowSpan(i);
			} else {
				rowOffsets[i] = rowOffsets[i] - 1;
			}
			// end row
			if ((i + 1) >= numColumns) {
				sw.write("</tr>\n");
			}
		}
	}

	public void addCell(final StringWriter sw, final String text, final String[] attributes) {
		addCell(sw, "td", text, attributes);
	}

	public void addCell(final StringWriter sw, final String tag, String text, final String[] attributes) {
		if (attributes == null) {
			sw.write("<" + tag + ">" + text + "</" + tag + ">");
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
	public String htmlEscape(final String text) {
		if (text == null) {
			return null;
		}
		return HtmlEscapers.htmlEscaper().escape(text);
	}

	public void setRowHeadersIncluded(final boolean b) {
		this.rowHeadersIncluded = true;
	}

}
