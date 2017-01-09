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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.mmxlabs.common.csv.CSVWriter;
import com.mmxlabs.rcp.common.internal.Activator;

/**
 * Copies "rendered" tree contents to the clipboard. This will maintain the column order, sort order and label provider output. Child entries not yet visible (parent is not expanded) will not be
 * copied by this action.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyTreeToClipboardAction extends Action {

	private final Tree tree;

	private final char separator;

	public CopyTreeToClipboardAction(final Tree tree) {
		this(tree, '\t');
	}

	public CopyTreeToClipboardAction(final Tree tree, final char separator) {

		super("Copy");

		this.tree = tree;
		this.separator = separator;

		setText("Copy");
		setDescription("Copies tree data into the clipboard");
		setToolTipText("Copies tree data into the clipboard");
		setImageDescriptor(Activator.getImageDescriptor("/icons/copy.gif"));
	}

	@Override
	public void run() {

		final StringWriter sw = parseTreeIntoStringWriter();
		
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

	
	public StringWriter parseTreeIntoStringWriter() {

		final StringWriter sw = new StringWriter();
		final CSVWriter cw = new CSVWriter(sw, separator);

		// Note this may be zero if no columns have been defined. However an
		// implicit column will be created in such cases
		final int numColumns = tree.getColumnCount();

		try {
			// Header row
			for (int i = 0; i < numColumns; ++i) {
				final TreeColumn tc = tree.getColumn(i);
				cw.addValue(tc.getText());
				if ((i + 1) == numColumns) {
					cw.endRow();
				} 
			}
	
			for (final TreeItem item : tree.getItems()) {
				// Ensure at least 1 column to grab data
				processTreeItem(cw, Math.max(1, numColumns), item);
			}
		}
		catch (IOException e) {
			e.printStackTrace(); // should not occur, since we use a StringWriter
		}
		
		return sw;
		
	}
	
	/**
	 * Recursive function to process child {@link TreeItem}s
	 * 
	 * @param cw
	 * @param numColumns
	 * @param item
	 * @throws IOException 
	 */
	private void processTreeItem(final CSVWriter cw, final int numColumns, final TreeItem item) throws IOException {

		for (int i = 0; i < numColumns; ++i) {

			cw.addValue(item.getText(i));
			// Add EOL or separator char as appropriate
			if ((i + 1) == numColumns) {
				cw.endRow();
			} 
		}

		// Recurse.....
		for (final TreeItem child : item.getItems()) {
			processTreeItem(cw, numColumns, child);
		}
	}
}
