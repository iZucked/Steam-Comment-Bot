/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import java.io.StringWriter;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.rcp.common.internal.Activator;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

/**
 * Copies "rendered" table contents to the clipboard. This will maintain the column order, sort order and label provider output.
 * 
 * @author Simon Goodall
 * 
 */
public class CopyGridToExcelMSClipboardAction extends Action {

	private CopyGridToExcelMLStringUtil util;
	private @Nullable Runnable preOperation;
	private @Nullable Runnable postOperation;

	public CopyGridToExcelMSClipboardAction(final Grid table, final boolean includeRowHeaders) {
		this(table, includeRowHeaders, null, null);
	}

	public CopyGridToExcelMSClipboardAction(final Grid table, final boolean includeRowHeaders, final @Nullable Runnable preOperation, final @Nullable Runnable postOperation) {

		super("Copy");
		this.preOperation = preOperation;
		this.postOperation = postOperation;

		util = new CopyGridToExcelMLStringUtil(table, includeRowHeaders, false);

		setText("Copy");
		setDescription("Copies grid data into the clipboard");
		setToolTipText("Copies grid data into the clipboard");
		CommonImages.setImageDescriptors(this, IconPaths.Copy);
	}

	public void setAdditionalAttributeProvider(IAdditionalAttributeProvider provider) {
		util.setAdditionalAttributeProvider(provider);
	}

	@Override
	public void run() {

		if (preOperation != null) {
			preOperation.run();
		}
		try {
			String contents = util.convert();

			{
				final StringWriter sw = new StringWriter();
				// Dump header info
				// sw.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				// sw.append("<?mso-application progid=\"Excel.Sheet\"?>");
				// sw.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
				// sw.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
				// sw.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
				// sw.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"");
				// sw.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">");
				// // TODO: Get from input / date?
				// sw.append(" <Styles/>");
				// sw.append(" <Worksheet ss:Name=\"Schedule\">");
				// sw.append(" <Table ss:ExpandedColumnCount=\"10\" ss:ExpandedRowCount=\"10\" x:FullColumns=\"1\" x:FullRows=\"1\" ss:DefaultRowHeight=\"13.2\">");
				// sw.append(" <Row>");
				// sw.append(" <Cell><Data ss:Type=\String\">Hello</Data></Cell>");
				//// sw.append(" <Cell><Data ssType:\"String\">hello</Data></Cell>");
				// sw.append(" </Row>");
				// sw.append(" </Table>");
				// sw.append(" </Worksheet>");
				// sw.append(" </Workbook>");
				// contents = sw.toString();
				System.out.println(contents);
			}
			// Create a new clipboard instance
			final Display display = Display.getDefault();
			final Clipboard cb = new Clipboard(display);
			try {
				// Create the text transfer and set the contents
				final SpreadSheelMLTransfer textTransfer = new SpreadSheelMLTransfer();
				byte[] byteContent = contents.getBytes();

				cb.setContents(new Object[] { byteContent }, new Transfer[] { textTransfer });
			} finally {
				// Clean up our local resources - system clipboard now has the data
				cb.dispose();
			}
		} finally {
			if (postOperation != null) {
				postOperation.run();
			}
		}
	}

	public void setRowHeadersIncluded(final boolean b) {
		util.setRowHeadersIncluded(b);
	}

	public boolean isShowBackgroundColours() {
		return util.isShowBackgroundColours();
	}

	public void setShowBackgroundColours(boolean showBackgroundColours) {
		util.setShowBackgroundColours(showBackgroundColours);
	}

}
