package com.mmxlabs.models.ui.validation.gui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * {@link Dialog} implementation to render {@link IStatus} messages in a tree view.
 * 
 * @author Simon Goodall
 * 
 */

public class ValidationStatusDialog extends Dialog {

	private final IStatus status;

	public IStatus getStatus() {
		return status;
	}

	public ValidationStatusDialog(final IShellProvider parentShell, final IStatus status) {
		super(parentShell);
		this.status = status;
		setShellStyle(SWT.SHELL_TRIM);
	}

	public ValidationStatusDialog(final Shell parentShell, final IStatus status) {
		super(parentShell);
		this.status = status;
		setShellStyle(SWT.SHELL_TRIM);
	}

	@Override
	protected void configureShell(final Shell newShell) {

		newShell.setText("Validation");

		super.configureShell(newShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		final Composite composite = (Composite) super.createDialogArea(parent);

		final TreeViewer viewer = new TreeViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setLinesVisible(true);

		viewer.setContentProvider(new ValidationStatusContentProvider());
		viewer.setLabelProvider(new ValidationStatusLabelProvider());
		viewer.setComparator(new ValidationStatusComparator());

		viewer.setInput(status);

		return composite;
	}

}
