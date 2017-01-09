/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.nebula.widgets.grid.Grid;
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
public class CopyGridToHtmlClipboardAction extends Action {

	private CopyGridToHtmlStringUtil util;

	public CopyGridToHtmlClipboardAction(final Grid table, final boolean includeRowHeaders) {

		super("Copy");

		util = new CopyGridToHtmlStringUtil(table, includeRowHeaders, false);

		setText("Copy");
		setDescription("Copies grid data into the clipboard");
		setToolTipText("Copies grid data into the clipboard");
		setImageDescriptor(Activator.getImageDescriptor("/icons/copy.gif"));
	}

	public void setAdditionalAttributeProvider(IAdditionalAttributeProvider provider) {
		util.setAdditionalAttributeProvider(provider);
	}

	@Override
	public void run() {

		String contents = util.convert();

		// Create a new clipboard instance
		final Display display = Display.getDefault();
		final Clipboard cb = new Clipboard(display);
		try {
			// Create the text transfer and set the contents
			final TextTransfer textTransfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { contents }, new Transfer[] { textTransfer });
		} finally {
			// Clean up our local resources - system clipboard now has the data
			cb.dispose();
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
