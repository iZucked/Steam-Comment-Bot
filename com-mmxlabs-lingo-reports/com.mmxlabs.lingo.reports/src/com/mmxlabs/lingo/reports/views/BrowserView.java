/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * @author berkan
 *
 */
public class BrowserView extends ViewPart {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.BrowserVieww";

	/**
	 * 
	 */
	public BrowserView() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		final Browser b = new Browser(parent, SWT.NONE); // Uses IE on MS Windows
		b.setUrl("http://www.google.com/");
		b.addProgressListener(new ProgressListener() {
			@Override
			public void completed(ProgressEvent event) {
				System.out.println("Page loaded");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			@Override
			public void changed(ProgressEvent event) {
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
