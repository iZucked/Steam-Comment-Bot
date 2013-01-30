/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.rcp.common.internal.Activator;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToClipboardAction extends Action {

	private final Grid table;

	private final char separator;

	private boolean rowHeadersIncluded = false;

	public CopyGridToClipboardAction(final Grid table) {
		this(table, '\t');
	}

	public CopyGridToClipboardAction(final Grid table, final char separator) {

		super("Copy");

		this.table = table;
		this.separator = separator;

		setText("Copy");
		setDescription("Copies grid data into the clipboard");
		setToolTipText("Copies grid data into the clipboard");
		setImageDescriptor(Activator.getImageDescriptor("/icons/copy.gif"));
	}

	@Override
	public void run() {

		final StringBuffer sb = new StringBuffer();

		// Note this may be zero if no columns have been defined. However an
		// implicit column will be created in such cases
		final boolean getRowHeaders = rowHeadersIncluded && table.isRowHeaderVisible();

		final int numColumns = table.getColumnCount();

		// Header row
		if (getRowHeaders) {
			// presume empty top left cell
			sb.append(separator);
		}
		// other header cells
		for (int i = 0; i < numColumns; ++i) {
			final GridColumn tc = table.getColumn(i);
			sb.append(tc.getText());
			if ((i + 1) == numColumns) {
				sb.append("\n");
			} else {
				sb.append(separator);
			}
		}

		for (final GridItem item : table.getItems()) {
			// Ensure at least 1 column to grab data
			processTableItem(sb, Math.max(1, numColumns), item);
		}

		// Create a new clipboard instance
		final Display display = Display.getDefault();
		final Clipboard cb = new Clipboard(display);
		try {
			// Create the text transfer and set the contents
			final TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { sb.toString() }, new Transfer[] { textTransfer });
		} finally {
			// Clean up our local resources - system clipboard now has the data
			cb.dispose();
		}
	}

	private void processTableItem(final StringBuffer sb, final int numColumns, final GridItem item) {
		if (rowHeadersIncluded) {
			sb.append(item.getHeaderText());
			sb.append(separator);
		}
		for (int i = 0; i < numColumns; ++i) {

			sb.append(item.getText(i));
			// Add EOL or separator char as appropriate
			if ((i + 1) == numColumns) {
				sb.append("\n");
			} else {
				sb.append(separator);
			}
		}
	}

	public void setRowHeadersIncluded(final boolean b) {
		this.rowHeadersIncluded = true;
	}
}
