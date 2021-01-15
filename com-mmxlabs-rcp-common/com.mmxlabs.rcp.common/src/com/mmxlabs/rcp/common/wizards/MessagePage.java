/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MessagePage extends WizardPage {

	private Text messageTextControl;

	private String message;

	public MessagePage(String title, String message) {
		super(title);
		this.message = message;
		setTitle(title);
		setControl(messageTextControl);
	}

	@Override

	public void createControl(final Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);

		final GridLayout layout = new GridLayout(1, true);
		container.setLayout(layout);

		messageTextControl = new Text(container, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		messageTextControl.setText(message);

		final GridData gd = new GridData();
		gd.verticalAlignment = SWT.CENTER;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.heightHint = 300;
		gd.minimumWidth = 500;
		messageTextControl.setLayoutData(gd);

		setControl(container);

		setPageComplete(true);
	}

}