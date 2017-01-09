/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.mmxlabs.common.csv.CSVWriter;
import com.mmxlabs.rcp.common.internal.Activator;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyTableToClipboardAction extends Action {

	private final Table table;

	private final char separator;

	public CopyTableToClipboardAction(final Table table) {
		this(table, '\t');
	}

	public CopyTableToClipboardAction(final Table table, final char separator) {

		super("Copy");

		this.table = table;
		this.separator = separator;

		setText("Copy");
		setDescription("Copies table data into the clipboard");
		setToolTipText("Copies table data into the clipboard");
		setImageDescriptor(Activator.getImageDescriptor("/icons/copy.gif"));
	}

	@Override
	public void run() {

		final StringWriter sw = parseTableIntoStringWriter();
		
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

	public final StringWriter parseTableIntoStringWriter() {

		final StringWriter sw = new StringWriter();
		final CSVWriter cw = new CSVWriter(sw, separator);

		// Note this may be zero if no columns have been defined. However an
		// implicit column will be created in such cases
		final int numColumns = table.getColumnCount();

		try {
			// Header row
			for (int i = 0; i < numColumns; ++i) {
				final TableColumn tc = table.getColumn(i);
				cw.addValue(tc.getText());
				if ((i + 1) == numColumns) {
					cw.endRow();
				} 
			}
	
			for (final TableItem item : table.getItems()) {
				// Ensure at least 1 column to grab data
				processTableItem(cw, Math.max(1, numColumns), item);
			}
		}
		catch (IOException e) {
			e.printStackTrace(); // should not occur, since we use a StringWriter
		}
		
		return sw;
		
	}
	
	private void processTableItem(final CSVWriter cw, final int numColumns, final TableItem item) throws IOException {
		for (int i = 0; i < numColumns; ++i) {

			cw.addValue(item.getText(i));
			// Add EOL or separator char as appropriate
			if ((i + 1) == numColumns) {
				cw.endRow();
			} 
		}
	}
}
