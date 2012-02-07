/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.tableview;

import org.eclipse.emf.common.ui.ViewerPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author hinton
 * 
 */
public class MasterDetailView extends Composite {
	public interface IMasterDetailControlProvider {
		public ViewerPane createMasterViewer(final Composite parent);

		public Control createDetailView(final Composite parent);
	}

	public MasterDetailView(final Composite parent, final int style, final IMasterDetailControlProvider controlProvider) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		final ViewerPane master = controlProvider.createMasterViewer(this);
		final Control detail = controlProvider.createDetailView(this);

		master.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		detail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

}
