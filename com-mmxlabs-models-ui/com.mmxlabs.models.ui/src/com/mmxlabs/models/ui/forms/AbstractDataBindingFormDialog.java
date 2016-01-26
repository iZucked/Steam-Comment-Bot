/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.ui.forms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * An abstract class providing a {@link FormDialog} with support for EMF Validation and EMF Data Bindings
 * 
 * @author Simon Goodall
 * 
 */
public abstract class AbstractDataBindingFormDialog extends FormDialog {

	protected EMFDataBindingContext dbc;

	protected ObservablesManager observablesManager;

	protected IManagedForm managedForm;

	protected FormToolkit toolkit;

	protected AbstractDataBindingFormDialog(final Shell parentShell) {
		super(parentShell);
	}

	protected AbstractDataBindingFormDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected void createFormContent(final IManagedForm managedForm) {

		this.managedForm = managedForm;
		this.toolkit = managedForm.getToolkit();

		this.dbc = new EMFDataBindingContext();
		this.observablesManager = new ObservablesManager();

		// This call means we do not need to manually manage our databinding objects lifecycle manually.
		observablesManager.runAndCollect(new Runnable() {

			@Override
			public void run() {
				doCreateFormContent();
			}
		});
	}

	@Override
	protected Control createButtonBar(Composite parent) {

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Label sep = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		sep.setLayoutData(gd);

		Composite composite = new Composite(parent, SWT.NONE);
		// create a layout with spacing and margins appropriate for the font
		// size.

		GridLayout layout = new GridLayout();
		layout.numColumns = 0; // this is incremented by createButton
		layout.makeColumnsEqualWidth = true;
		// layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		// layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		// layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		// layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		composite.setLayout(layout);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_CENTER);
		composite.setLayoutData(data);
		composite.setFont(parent.getFont());

		// Add the buttons to the button bar.
		createButtonsForButtonBar(composite);
		return composite;
		// return super.createButtonBar(parent);
	}

	@Override
	public boolean close() {

		if (observablesManager != null) {
			observablesManager.dispose();
		}
		if (dbc != null) {
			dbc.dispose();
		}

		return super.close();
	}

	protected abstract void doCreateFormContent();

	protected final void hookAggregatedValidationStatus() {
		// Link up form validation to the error message bar at the top
		final AggregateValidationStatus aggregateStatus = new AggregateValidationStatus(dbc.getValidationStatusProviders(), AggregateValidationStatus.MAX_SEVERITY);
		aggregateStatus.addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(final ValueChangeEvent event) {
				handleStateChange((IStatus) event.diff.getNewValue(), dbc);
			}
		});
		observablesManager.addObservable(aggregateStatus);
	}

	protected final void hookAggregatedValidationStatusWithResize() {
		// Link up form validation to the error message bar at the top
		final AggregateValidationStatus aggregateStatus = new AggregateValidationStatus(dbc.getValidationStatusProviders(), AggregateValidationStatus.MAX_SEVERITY);
		aggregateStatus.addValueChangeListener(new IValueChangeListener() {

			@Override
			public void handleValueChange(final ValueChangeEvent event) {
				handleStateChange((IStatus) event.diff.getNewValue(), dbc);
				if (!event.diff.getNewValue().equals(event.diff.getOldValue())) {
					resizeAndCenter(true);
				}
			}
		});
		observablesManager.addObservable(aggregateStatus);
	}

	/**
	 * Method to map the {@link IStatus} to {@link IMessage}.
	 * 
	 * @See http://tomsondev.bestsolution.at/2009/06/27/galileo-emf-databinding-part-5/
	 * 
	 * @param currentStatus
	 * @param ctx
	 */
	protected final void handleStateChange(final IStatus currentStatus, final DataBindingContext ctx) {

		final Form form = managedForm.getForm().getForm();
		if (form.isDisposed() || form.getHead().isDisposed()) {
			return;
		}

		if (currentStatus != null && currentStatus.getSeverity() != IStatus.OK) {
			final int type = convertType(currentStatus.getSeverity());

			final List<IMessage> list = new ArrayList<IMessage>();
			final Iterator<?> it = ctx.getValidationStatusProviders().iterator();

			while (it.hasNext()) {
				final ValidationStatusProvider validationStatusProvider = (ValidationStatusProvider) it.next();
				final IStatus status = (IStatus) validationStatusProvider.getValidationStatus().getValue();

				if (!status.isOK()) {
					list.add(new IMessage() {
						@Override
						public Control getControl() {
							return null;
						}

						@Override
						public Object getData() {
							return null;
						}

						@Override
						public Object getKey() {
							return null;
						}

						@Override
						public String getPrefix() {
							return null;
						}

						@Override
						public String getMessage() {
							return status.getMessage();
						}

						@Override
						public int getMessageType() {
							return convertType(status.getSeverity());
						}
					});
				}
			}
			final String msg = String.format("%d error(s)", list.size());
			managedForm.getForm().setMessage(msg, type, list.toArray(new IMessage[0]));
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		} else {
			managedForm.getForm().setMessage(null, IMessageProvider.NONE);
			getButton(IDialogConstants.OK_ID).setEnabled(true);
		}
	}

	/**
	 * Map between IStatus values and {@link IMessageProvider} values
	 * 
	 * @param type
	 * @return
	 */
	protected final int convertType(final int type) {

		if (type == IStatus.ERROR) {
			return IMessageProvider.ERROR;
		}
		if (type == IStatus.WARNING) {
			return IMessageProvider.WARNING;
		}
		if (type == IStatus.INFO) {
			return IMessageProvider.INFORMATION;
		}
		if (type == IStatus.OK) {
			return IMessageProvider.NONE;
		}
		return IMessageProvider.NONE;
	}

	protected void resizeAndCenter(final boolean growOnly) {
		final Shell shell = getShell();
		if (shell != null) {
			shell.layout(true);
			if (growOnly) {
				shell.setMinimumSize(shell.getSize());
			}

			Point computeSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			shell.setSize(Math.min(computeSize.x, shell.getDisplay().getPrimaryMonitor().getBounds().width - 50), Math.min(computeSize.y, shell.getDisplay().getPrimaryMonitor().getBounds().height - 50));
			final Rectangle shellBounds = getParentShell().getBounds();
			final Point dialogSize = shell.getSize();
			shell.setLocation(shellBounds.x + ((shellBounds.width - dialogSize.x) / 2), shellBounds.y + ((shellBounds.height - dialogSize.y) / 2));
		}
	}
}
