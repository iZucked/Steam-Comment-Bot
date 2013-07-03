package com.mmxlabs.models.lng.transformer.ui.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A Dialog to present options for running an optimisation or evaluation that can be changed.
 * 
 * @author Simon Goodall
 * 
 */
public class ParameterModesDialog extends FormDialog {

	// Properties. Note if we rename these, then we *must* also update the strings in createForm()
	private boolean charterOutGenerationEnabled;
	private boolean defaultCharterOutGenerationEnabled;
	private boolean shippingOnly;
	private boolean defaultShippingOnly;
	private int numberOfIterations;
	private int defaultNumberOfIterations;

	private DataBindingContext dbc;

	private ObservablesManager observablesManager;

	private IManagedForm managedForm;

	private FormToolkit toolkit;

	public ParameterModesDialog(final Shell parentShell) {
		super(parentShell);
	}

	public ParameterModesDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected void createFormContent(final IManagedForm managedForm) {

		this.managedForm = managedForm;
		this.toolkit = managedForm.getToolkit();

		this.dbc = new DataBindingContext();
		this.observablesManager = new ObservablesManager();

		// This call means we do not need to manually manage our databinding objects lifecycle manually.
		observablesManager.runAndCollect(new Runnable() {

			@Override
			public void run() {
				createForm();
			}
		});
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

	private void createForm() {
		// Get the form object and set a title
		final ScrolledForm form = managedForm.getForm();
		form.setLayoutData(new GridData(GridData.FILL_BOTH));
		form.setText("Options");
		toolkit.decorateFormHeading(form.getForm());

		// Add option to reset to default vaules
		form.getToolBarManager().add(new Action("Reset to defaults") {
			@Override
			public void run() {
				// Add in any new fields here
				ParameterModesDialog.this.charterOutGenerationEnabled = defaultCharterOutGenerationEnabled;
				ParameterModesDialog.this.shippingOnly = defaultShippingOnly;
				ParameterModesDialog.this.numberOfIterations = defaultNumberOfIterations;
				// Force UI refresh after changing the data
				dbc.updateTargets();
			}
		});
		form.getToolBarManager().update(true);

		final GridLayout layout = new GridLayout(1, true);
		form.getBody().setLayout(layout);

		// Add in standard options
		createCheckBox(form.getBody(), "Generate Charter Out opportunites", "charterOutGenerationEnabled");
		createCheckBox(form.getBody(), "Optimise shipping only", "shippingOnly");

		// Create an advanced section
		final Section advanced = toolkit.createSection(form.getBody(), SWT.DEFAULT);
		advanced.setText("Advanced Settings");
		// Note, if we want an additional field here, then we should create a composite and add them all to the composite. The composite then becomes the client
		advanced.setClient(createNumberTextBox(advanced, "Number of Iterations", "numberOfIterations"));

		// Initially collapsed
		advanced.setExpanded(false);

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

	/**
	 * Method to map the {@link IStatus} to {@link IMessage}.
	 * 
	 * @See http://tomsondev.bestsolution.at/2009/06/27/galileo-emf-databinding-part-5/
	 * 
	 * @param currentStatus
	 * @param ctx
	 */
	private void handleStateChange(final IStatus currentStatus, final DataBindingContext ctx) {

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
			managedForm.getForm().setMessage("Data invalid", type, list.toArray(new IMessage[0]));
			getButton(IDialogConstants.OK_ID).setEnabled(true);
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
	private int convertType(final int type) {

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

	/**
	 * Create a check box button with the given label for the given property on {@link ParameterModesDialog}
	 * 
	 * @param form
	 * @param label
	 * @param propertyName
	 * @return
	 */
	private Button createCheckBox(final Composite form, final String label, final String propertyName) {
		final Button btn = toolkit.createButton(form, label, SWT.CHECK);
		final Binding bindValue = dbc.bindValue(WidgetProperties.selection().observe(btn), PojoProperties.value(ParameterModesDialog.class, propertyName).observe(this));
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		return btn;
	}

	/**
	 * Create a +ve integer text box with the given label for the given property on {@link ParameterModesDialog}
	 * 
	 * @param form
	 * @param label
	 * @param propertyName
	 * @return
	 */
	private Composite createNumberTextBox(final Composite form, final String label, final String propertyName) {

		final Composite area = toolkit.createComposite(form, SWT.NONE);
		area.setLayout(new GridLayout(2, false));
		toolkit.createLabel(area, label);
		final Text text = toolkit.createText(area, null, SWT.NONE);

		// Define a validator to check that only numbers are entered
		final IValidator validator = new IValidator() {
			@Override
			public IStatus validate(final Object value) {
				if (value instanceof Integer) {
					if (value.toString().matches(".*\\d.*")) {
						return ValidationStatus.ok();
					}
				}
				return ValidationStatus.error("Not a number");
			}
		};

		// Create UpdateValueStratgy and assign
		// to the binding
		final UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setBeforeSetValidator(validator);

		final Binding bindValue = dbc.bindValue(SWTObservables.observeText(text, SWT.Modify), PojoObservables.observeValue(ParameterModesDialog.this, propertyName), strategy, null);

		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		return area;
	}

	public boolean isCharterOutGenerationEnabled() {
		return charterOutGenerationEnabled;
	}

	public void setCharterOutGenerationEnabled(final boolean charterOutGenerationEnabled) {
		this.charterOutGenerationEnabled = charterOutGenerationEnabled;
	}

	public boolean isShippingOnly() {
		return shippingOnly;
	}

	public void setShippingOnly(final boolean shippingOnly) {
		this.shippingOnly = shippingOnly;
	}

	public boolean isDefaultCharterOutGenerationEnabled() {
		return defaultCharterOutGenerationEnabled;
	}

	public void setDefaultCharterOutGenerationEnabled(final boolean defaultCharterOutGenerationEnabled) {
		this.defaultCharterOutGenerationEnabled = defaultCharterOutGenerationEnabled;
	}

	public boolean isDefaultShippingOnly() {
		return defaultShippingOnly;
	}

	public void setDefaultShippingOnly(final boolean defaultShippingOnly) {
		this.defaultShippingOnly = defaultShippingOnly;
	}

	public int getDefaultNumberOfIterations() {
		return defaultNumberOfIterations;
	}

	public void setDefaultNumberOfIterations(final int defaultNumberOfIterations) {
		this.defaultNumberOfIterations = defaultNumberOfIterations;
	}

	public int getNumberOfIterations() {
		return numberOfIterations;
	}

	public void setNumberOfIterations(final int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}

}
