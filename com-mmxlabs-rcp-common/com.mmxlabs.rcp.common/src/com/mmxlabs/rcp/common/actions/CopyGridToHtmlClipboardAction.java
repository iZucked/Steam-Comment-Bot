/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.csv.CSVWriter;
import com.mmxlabs.rcp.common.internal.Activator;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToHtmlClipboardAction extends Action {

	private final Grid table;

	private boolean rowHeadersIncluded = false;

	public CopyGridToHtmlClipboardAction(final Grid table) {

		super("Copy");

		this.table = table;

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
		final boolean getRowHeaders = rowHeadersIncluded && table.isRowHeaderVisible();

		final int numColumns = table.getColumnCount();

		
		sw.write("<table border='1'>\n");
		try {
			addHeader(sw);
			for (final GridItem item : table.getItems()) {
				// Ensure at least 1 column to grab data
				processTableRow(sw, Math.max(1, numColumns), item);
			}
		}
		catch (IOException e) {
			e.printStackTrace(); // should not occur, since we use a StringWriter
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
		sw.write("<thead>\n<tr>");
		// top left blank cell
		if (rowHeadersIncluded) {
			addCell(sw, "th", "", new String [] { "bgcolor='grey'" });
		}
		// column headings
		for (int i = 0; i < numColumns; ++i) {
			addCell(sw, "th", table.getColumn(i).getText(), new String [] { "bgcolor='grey'" });
		}
		sw.write("</tr>\n</thead>\n");		
	}

	private void processTableRow(final StringWriter sw, final int numColumns, final GridItem item) throws IOException {
		// start a row 
		sw.write("<tr>");
		
		
		if (rowHeadersIncluded) {
			addCell(sw, item.getHeaderText(), new String [] { "bgcolor='gray'" });
		}
		
		for (int i = 0; i < numColumns; ++i) {
			final Color c = item.getBackground(i);
			final String colourString; 
			if (c == null) {
				colourString = "bgcolor='white'";
			}
			else {
				colourString = String.format("bgcolor='#%02X%02X%02X'", c.getRed(), c.getGreen(), c.getBlue());				
			}
			
			addCell(sw, item.getText(i), new String [] { colourString, "" });
			// end row
			if ((i + 1) == numColumns) {
				sw.write("</tr>\n");
			}
		}
	}
	
	public void addCell(StringWriter sw, String text, String [] attributes) {
		addCell(sw, "td", text, attributes);
	}
	

	public void addCell(StringWriter sw, String tag, String text, String [] attributes) {
		if (attributes == null) {
			sw.write("<" + tag + ">" + text + "</" + tag + ">");			
		}
		else {
			sw.write("<" + tag);
			for (String attribute: attributes) {
				sw.write(" " + attribute);				
			}
			sw.write(">");
		}
		text = htmlEscape(text);
		
		if (text.equals("")) {
			text = "&nbsp;";
		}			
		
		sw.write(text); // TODO: escape this for HTML
		sw.write("</" + tag + ">");
	}
	
	/**
	 * Does rough HTML escaping on a piece of text. 
	 * @param text
	 * @return
	 */
	// TODO: replace with proper HTML escaping from a library somewhere
	public String htmlEscape(String text) {
		return text.replace("&", "&amp;").replace(">", "&gt;").replace("<", "&gt;").replace("\"", "&quot;");
	}
	

	public void setRowHeadersIncluded(final boolean b) {
		this.rowHeadersIncluded = true;
	}

}
