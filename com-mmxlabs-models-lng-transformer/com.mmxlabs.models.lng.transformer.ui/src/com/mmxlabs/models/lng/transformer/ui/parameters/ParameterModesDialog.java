/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parameters;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.databinding.edit.IEMFEditValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.mmxlabs.models.ui.forms.AbstractDataBindingFormDialog;

/**
 * A Dialog to present options for running an optimisation or evaluation that can be changed.
 * 
 * @author Simon Goodall
 * 
 */
public class ParameterModesDialog extends AbstractDataBindingFormDialog {

	public enum DataType {
		Boolean, PositiveInt
	}

	public enum DataSection {
		Main, Advanced
	}

	private class Option {
		final DataSection dataSection;
		final EObject data;
		final EObject defaultData;
		final DataType dataType;
		final EStructuralFeature[] features;
		final String label;
		final EditingDomain editingDomain;

		public Option(final DataSection dataSection, final EditingDomain editingDomain, final String label, final EObject data, final EObject defaultData, final DataType dataType,
				final EStructuralFeature... features) {
			this.dataSection = dataSection;
			this.data = data;
			this.defaultData = defaultData;
			this.dataType = dataType;
			this.features = features;
			this.editingDomain = editingDomain;
			this.label = label;
		}
	}

	private final Map<DataSection, List<Option>> optionsMap = new EnumMap<DataSection, List<Option>>(DataSection.class);

	public ParameterModesDialog(final Shell parentShell) {
		super(parentShell);
	}

	public ParameterModesDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected void doCreateFormContent() {
		// Get the form object and set a title
		final ScrolledForm form = managedForm.getForm();
		form.setLayoutData(new GridData(GridData.FILL_BOTH));
		form.setText("Settings");
		toolkit.decorateFormHeading(form.getForm());

		// Add option to reset to default values
		form.getToolBarManager().add(new Action("Reset to defaults") {
			@Override
			public void run() {

				for (final Map.Entry<DataSection, List<Option>> e : optionsMap.entrySet()) {

					for (final Option option : e.getValue()) {

						EObject from = option.defaultData;
						EObject to = option.data;
						for (int i = 0; i < option.features.length - 1; ++i) {
							from = (EObject) from.eGet(option.features[i]);
							to = (EObject) to.eGet(option.features[i]);
						}
						final EStructuralFeature f = option.features[option.features.length - 1];
						to.eSet(f, from.eGet(f));

					}
					dbc.updateTargets();
				}
			}
		});
		form.getToolBarManager().update(true);

		final GridLayout layout = new GridLayout(1, true);
		form.getBody().setLayout(layout);

		// Add in standard options
		if (optionsMap.containsKey(DataSection.Main)) {
			final List<Option> options = optionsMap.get(DataSection.Main);
			for (final Option option : options) {
				createOption(form.getBody(), option);
			}
		}

		if (optionsMap.containsKey(DataSection.Advanced)) {

			// Create an advanced section
			final Section advanced = toolkit.createSection(form.getBody(), SWT.DEFAULT);
			advanced.setText("Advanced");

			// Initially collapsed
			advanced.setExpanded(false);
			advanced.addExpansionListener(new ExpansionAdapter() {

				@Override
				public void expansionStateChanged(final ExpansionEvent e) {
					resizeAndCenter();
				}

			});
			// Note, if we want an additional field here, then we should create a composite and add them all to the composite. The composite then becomes the client
			final Composite area = toolkit.createComposite(advanced);
			area.setLayout(new GridLayout(1, true));
			final List<Option> options = optionsMap.get(DataSection.Advanced);
			for (final Option option : options) {
				createOption(area, option);
			}
			advanced.setClient(area);
		}

		hookAggregatedValidationStatusWithResize();
	}

	private void createOption(final Composite parent, final Option option) {

		switch (option.dataType) {
		case Boolean:
			createCheckBox(parent, option);
			break;
		case PositiveInt:
			createNumberTextBox(parent, option);
			break;
		default:
			break;
		}
	}

	/**
	 * Create a check box button with the given label for the given property on {@link ParameterModesDialog}
	 * 
	 * @param form
	 * @param label
	 * @param propertyName
	 * @return
	 */
	private Button createCheckBox(final Composite form, final Option option) {
		final Button btn = toolkit.createButton(form, option.label, SWT.CHECK);
		final IEMFEditValueProperty prop = EMFEditProperties.value(option.editingDomain, FeaturePath.fromList(option.features));
		final Binding bindValue = dbc.bindValue(WidgetProperties.selection().observe(btn), prop.observe(option.data));
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
	private Composite createNumberTextBox(final Composite parent, final Option option) {

		final Composite area = toolkit.createComposite(parent, SWT.NONE);
		area.setLayout(new GridLayout(2, false));
		toolkit.createLabel(area, option.label);
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
		final EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy();
		strategy.setBeforeSetValidator(validator);
		final IEMFEditValueProperty prop = EMFEditProperties.value(option.editingDomain, FeaturePath.fromList(option.features));
		final Binding bindValue = dbc.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(500, text), prop.observe(option.data), strategy, null);

		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);
		return area;
	}

	public void addOption(final DataSection dataSection, final EditingDomain editingDomian, final String label, final EObject data, final EObject defaultData, final DataType dataType,
			final EStructuralFeature... features) {
		final Option option = new Option(dataSection, editingDomian, label, data, defaultData, dataType, features);
		final List<Option> options;
		if (optionsMap.containsKey(dataSection)) {
			options = optionsMap.get(dataSection);
		} else {
			options = new LinkedList<ParameterModesDialog.Option>();
			optionsMap.put(dataSection, options);
		}
		options.add(option);
	}
}
