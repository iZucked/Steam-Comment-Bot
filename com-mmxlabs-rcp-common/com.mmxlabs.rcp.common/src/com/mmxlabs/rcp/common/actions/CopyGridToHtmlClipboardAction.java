/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jdt.annotation.Nullable;
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
public class CopyGridToHtmlClipboardAction extends Action{

	protected CopyGridToHtmlStringUtil util;
	protected Runnable preOperation;
	protected Runnable postOperation;

	public CopyGridToHtmlClipboardAction(final Grid table, final boolean includeRowHeaders) {
		this(table, includeRowHeaders, null, null);
	}

	public CopyGridToHtmlClipboardAction(final Grid table, final boolean includeRowHeaders, final @Nullable Runnable preOperation, final @Nullable Runnable postOperation) {

		super("Copy");
		this.preOperation = preOperation;
		this.postOperation = postOperation;

		util = new CopyGridToHtmlStringUtil(table, includeRowHeaders, false);
		
		setText("Copy");
		setDescription("Copies grid data into the clipboard");
		setToolTipText("Copies grid data into the clipboard");
		setImageDescriptor(Activator.getImageDescriptor("/icons/copy.gif"));
	}
	
	public void setShowForegroundColours(final boolean showForegroundColours) {
		if (util != null) {
			util.setShowForegroundColours(showForegroundColours);
		}
	}
	
	public void setShowBackgroundColours(final boolean showBackgroundColours) {
		if (util != null) {
			util.setShowBackgroundColours(showBackgroundColours);
		}
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
			String contents =  "<meta charset=\"UTF-8\"/>"  + util.convert();

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
		if (util == null) return false;
		return util.isShowBackgroundColours();
	}
	
	public boolean isShowForegroundColours() {
		if (util == null) return false;
		return util.isShowForegroundColours();
	}
}
