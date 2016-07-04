/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.csv.CSVWriter;
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

		final StringWriter sw = parseGridIntoStringWriter();

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

	public StringWriter parseGridIntoStringWriter() {
		final StringWriter sw = new StringWriter();
		final CSVWriter cw = new CSVWriter(sw, separator);

		// Note this may be zero if no columns have been defined. However an
		// implicit column will be created in such cases
		final boolean getRowHeaders = rowHeadersIncluded && table.isRowHeaderVisible();

		final int numColumns = table.getColumnCount();

		try {
			// Header row
			if (getRowHeaders) {
				// presume empty top left cell
				cw.addValue("");
			}
			// other header cells
			for (int i = 0; i < numColumns; ++i) {
				final GridColumn tc = table.getColumn(i);
				cw.addValue(tc.getText());
				if ((i + 1) == numColumns) {
					cw.endRow();
				} 
			}
	
			for (final GridItem item : table.getItems()) {
				// Ensure at least 1 column to grab data
				processTableItem(cw, Math.max(1, numColumns), item);
			}
		}
		catch (IOException e) {
			e.printStackTrace(); // should not occur, since we use a StringWriter
		}

		return sw;
	}
	
	private void processTableItem(final CSVWriter cw, final int numColumns, final GridItem item) throws IOException {
		if (rowHeadersIncluded) {
			cw.addValue(item.getHeaderText());
		}
		for (int i = 0; i < numColumns; ++i) {
			cw.addValue(item.getText(i));
			// end row
			if ((i + 1) == numColumns) {
				cw.endRow();
			}
		}
	}

	public void setRowHeadersIncluded(final boolean b) {
		this.rowHeadersIncluded = true;
	}

}
