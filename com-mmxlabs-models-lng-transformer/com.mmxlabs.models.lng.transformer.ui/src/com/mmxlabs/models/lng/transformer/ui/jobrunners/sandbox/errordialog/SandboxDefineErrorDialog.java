/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.errordialog;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Simon Goodall
 * 
 */

public class SandboxDefineErrorDialog extends Dialog {

	private final List<SolutionErrorSet> errors;

	public SandboxDefineErrorDialog(final IShellProvider parentShell, final List<SolutionErrorSet> errors) {
		super(parentShell);
		this.errors = errors;
		setShellStyle(SWT.SHELL_TRIM);
	}

	public SandboxDefineErrorDialog(final Shell parentShell, final List<SolutionErrorSet> errors) {
		super(parentShell);
		this.errors = errors;
		setShellStyle(SWT.SHELL_TRIM);
	}

	@Override
	protected void configureShell(final Shell newShell) {
		newShell.setText("No solutions found");
		super.configureShell(newShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		final Composite composite = (Composite) super.createDialogArea(parent);
		final Text text = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.READ_ONLY);
		{
			final GridData gdText = new GridData(SWT.FILL, SWT.END, true, false);
			gdText.heightHint = 50;
			text.setLayoutData(gdText);
			text.setText("No valid solutions. See reasons below.");
		}

		final TreeViewer viewer = new TreeViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);

		{
			final GridData gdViewer = new GridData(SWT.FILL, SWT.FILL, true, true);
			gdViewer.heightHint = 200;
			viewer.getControl().setLayoutData(gdViewer);
		}

		viewer.getTree().setLinesVisible(true);

		viewer.setContentProvider(new SandboxDefineErrorContentProvider());
		viewer.setLabelProvider(new SandboxDefineErrorLabelProvider());

		viewer.setInput(errors);
		viewer.expandAll();

		viewer.addSelectionChangedListener(event -> {
			final IStructuredSelection iStructuredSelection = event.getStructuredSelection();
			final Object o = iStructuredSelection.getFirstElement();
			if (o instanceof IStatus ss) {
				text.setText(ss.getMessage());
			}
		});

		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		// create OK buttons by default
		createButton(parent, IDialogConstants.OK_ID, "Done", true);
	}
}
