/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;

/**
 * @author hinton
 *
 */
public class DatePopup extends PopupComposite {
	private DateAndTime dateAndTime;
	
	public DatePopup(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void createPopupContents(final Shell popup) {
		popup.setLayout (new GridLayout (3, false));

		final DateTime calendar = new DateTime (popup, SWT.CALENDAR | SWT.BORDER);
//		final DateTime date = new DateTime (popup, SWT.DATE | SWT.SHORT);
		final DateTime time = new DateTime (popup, SWT.TIME | SWT.SHORT);
		
		popup.pack();
	}

	@Override
	protected Composite createSmallWidget(final Composite parent, int style) {
		return dateAndTime = new DateAndTime(parent, style);
	}
}
