/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.editor.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.ValidationStatusProvider;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.databinding.edit.IEMFEditValueProperty;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.forms.AbstractDataBindingFormDialog;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.IValidationService;
import com.mmxlabs.models.ui.validation.gui.GroupedValidationStatusContentProvider;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusComparator;
import com.mmxlabs.models.ui.validation.gui.ValidationStatusLabelProvider;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * A Dialog to present options for running an optimisation or evaluation that
 * can be changed.
 * 
 * @author Simon Goodall
 * 
 */
public class ParameterModesDialog extends AbstractDataBindingFormDialog {

	private static final String SWTBOT_KEY = "org.eclipse.swtbot.widget.key";

	public enum DataType {
		Boolean, PositiveInt, Date, MonthYear, Choice, Label, Button, Tree
	}

	public enum DataSection {
		General, Controls, Toggles, Advanced, Validation
	}

	public static class ChoiceData {

		private final List<Pair<String, Object>> choices = new LinkedList<>();
		public final List<BiConsumer<Label, Object>> changeHandlers = new LinkedList<>();
		public boolean enabled = true;
		public Function<UserSettings, Boolean> enabledHook;
		public String disabledMessage;

		public void addChoice(final String name, final Object value) {
			choices.add(new Pair<>(name, value));
		}
	}

	public static class OptionGroup {
		public String name;
		public DataSection dataSection;
	}

	public class Option {
		final DataSection dataSection;
		final OptionGroup group;
		final EObject data;
		final EObject defaultData;
		final DataType dataType;
		final ChoiceData choiceData;
		final EStructuralFeature[] features;
		final String label;
		final String swtBotId;
		final EditingDomain editingDomain;
		final IScenarioDataProvider scenarioProvider;
		final String tooltip;
		Control control;
		MouseListener listener;
		public boolean enabled = true;

		public void setListener(MouseListener listener) {
			this.listener = listener;
		}

		public Control getControl() {
			return this.control;
		}

		@NonNull
		public final List<IValidator> validators = new LinkedList<>();
		public final List<Consumer<IObservableValue<?>>> validatorCallbacks = new LinkedList<>();

		public Option(final DataSection dataSection, final OptionGroup group, final EditingDomain editingDomain, final String label, final String tooltip, final EObject data,
				final EObject defaultData, final DataType dataType, final ChoiceData choiceData, String swtBotId, IScenarioDataProvider scenarioProvider, final EStructuralFeature... features) {
			this.dataSection = dataSection;
			this.group = group;
			this.data = data;
			this.defaultData = defaultData;
			this.dataType = dataType;
			this.choiceData = choiceData;
			this.swtBotId = swtBotId;
			this.features = features;
			this.editingDomain = editingDomain;
			this.label = label;
			this.scenarioProvider = scenarioProvider;
			this.tooltip = tooltip;
		}
	}

	private final Map<DataSection, List<Option>> optionsMap = new EnumMap<DataSection, List<Option>>(DataSection.class);

	@Nullable
	private String title;

	private List<ValidationStatusProvider> extraValidators = new LinkedList<>();

	private boolean showName;

	private String nameSuggestion;

	public void setNameSuggestion(String nameSuggestion) {
		this.nameSuggestion = nameSuggestion;
	}

	private Set<String> existingNames;

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
		form.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));
		form.setText("");
		toolkit.decorateFormHeading(form.getForm());

		// Disable reset button as choice control does not work correctly with it.

		// // Add option to reset to default values
		// form.getToolBarManager().add(new Action("Reset to defaults") {
		// @Override
		// public void run() {
		//
		// for (final Map.Entry<DataSection, List<Option>> e : optionsMap.entrySet()) {
		//
		// for (final Option option : e.getValue()) {
		//
		// EObject from = option.defaultData;
		// if (from != null) {
		// EObject to = option.data;
		// for (int i = 0; i < option.features.length - 1; ++i) {
		// from = (EObject) from.eGet(option.features[i]);
		// to = (EObject) to.eGet(option.features[i]);
		// }
		// final EStructuralFeature f = option.features[option.features.length - 1];
		// to.eSet(f, from.eGet(f));
		// }
		// }
		// dbc.updateTargets();
		// }
		// }
		// });
		// form.getToolBarManager().update(true);

		final GridLayout layout = new GridLayout(1, true);
		form.getBody().setLayout(layout);

		extraValidators.forEach(v -> dbc.addValidationStatusProvider(v));

		// Add in standard options
		final Map<OptionGroup, Composite> groupMap = new HashMap<>();
		if (showName) {
			final Composite area = toolkit.createComposite(form.getBody(), SWT.NONE);
			area.setLayout(new GridLayout(2, false));
			area.setLayoutData(GridDataFactory.fillDefaults().create());

			final Label lbl = toolkit.createLabel(area, "Name");
			final Text text = toolkit.createText(area, null, SWT.NONE);
			text.setLayoutData(GridDataFactory.fillDefaults().hint(400, SWT.DEFAULT).create());
			if (nameSuggestion == null) {
				nameSuggestion = "";
			} else {
				if (existingNames != null) {
					String base = nameSuggestion;
					int counter = 1;
					while (existingNames.contains(nameSuggestion)) {
						nameSuggestion = String.format("%s (%d)", base, counter++);
					}
				}
			}

			text.setText(nameSuggestion);
			// Create UpdateValueStratgy and assign
			// to the binding
			final UpdateValueStrategy strategy = new UpdateValueStrategy();
			{
				// Define a validator to check that only numbers are entered
				final IValidator validator = new IValidator() {
					@Override
					public IStatus validate(final Object value) {
						if (value == null || value.toString().isEmpty()) {
							return ValidationStatus.warning("No name specified");
						}
						if (existingNames != null) {
							if (existingNames.contains(value)) {
								return ValidationStatus.warning("Name clashes with another result");
							}
						}
						return ValidationStatus.ok();
					}
				};
				strategy.setBeforeSetValidator(validator);
			}

			// final CompositeValidator v = new CompositeValidator();
			// v.addValidators(option.validators);
			// strategy.setAfterGetValidator(v);

			final IValueProperty prop = PojoProperties.value("nameSuggestion");
			final Binding bindValue = dbc.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(500, text), prop.observe(this), strategy, null);

			ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		}
		if (optionsMap.containsKey(DataSection.General)) {

			final List<Option> options = optionsMap.get(DataSection.General);
			createOptionSetControls(form.getBody(), groupMap, options);
		}

		if (optionsMap.containsKey(DataSection.Controls) || optionsMap.containsKey(DataSection.Toggles) || optionsMap.containsKey(DataSection.Validation)) {
			final Composite middle = toolkit.createComposite(form.getBody());
			middle.setLayout(new GridLayout(2, false));
			// Create Controls
			{
				final List<Option> options = optionsMap.get(DataSection.Controls);
				if (options != null) {
					final Group group = new Group(middle, SWT.NONE);
					toolkit.adapt(group);
					group.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
					// group.setText("Controls");
					group.setLayout(new GridLayout(1, true));
					createOptionSetControls(group, groupMap, options);
				}
			}
			// Create Toggles
			{
				final List<Option> options = optionsMap.get(DataSection.Toggles);
				if (options != null) {
					final Group group = new Group(middle, SWT.NONE);
					toolkit.adapt(group);
					group.setLayoutData(GridDataFactory.fillDefaults().grab(false, true).create());
					// group.setText("Toggles");
					group.setLayout(new GridLayout(1, true));
					createOptionSetControls(group, groupMap, options);
				}
			}
			
			// Create validation output
			{
				final List<Option> options = optionsMap.get(DataSection.Validation);
				if (options != null) {
					final Group group = new Group(middle, SWT.NONE);
					toolkit.adapt(group);
					group.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2,1).create());
					group.setText("Validation");
					group.setLayout(new GridLayout(1, true));
					createOptionSetControls(group, groupMap, options);
				}
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
					resizeAndCenter(false);
				}

			});
			// Note, if we want an additional field here, then we should create a composite
			// and add them all to the composite. The composite then becomes the client
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

	protected void createOptionSetControls(final Composite realParent, final Map<OptionGroup, Composite> groupMap, final List<Option> options) {
		for (final Option option : options) {
			Composite parent = realParent;
			if (option.group != null) {

				if (groupMap.containsKey(option.group)) {
					parent = groupMap.get(option.group);
				} else {
					final Group g = new Group(parent, SWT.NONE);
					g.setText(option.group.name);
					g.setLayout(new GridLayout(1, true));
					g.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

					toolkit.adapt(g);
					groupMap.put(option.group, g);
					parent = g;
				}
			}

			createOption(parent, option);
		}
	}

	private void createOption(Composite parent, final Option option) {

		if (option.choiceData != null && !option.choiceData.enabled && option.choiceData.disabledMessage != null) {
			parent = toolkit.createComposite(parent);
			parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).create());
		}

		switch (option.dataType) {
		case Boolean:
			option.control = createCheckBox(parent, option);
			break;
		case PositiveInt:
			option.control = createNumberTextBox(parent, option);
			break;
		case Date:
			option.control = createDateEditor(parent, option);
			break;
		case MonthYear:
			option.control = createMonthYearEditor(parent, option);
			break;
		case Choice:
			option.control = createChoiceEditor(parent, option);
			break;
		case Label:
			option.control = createLabel(parent, option);
			break;
		case Button:
			// FIXME using 2014 as an arbitrary number
			Button button = createButton(parent, 2014, option.label, false);
			if (option.listener != null) {
				button.addMouseListener(option.listener);
			}

			button.setToolTipText(option.tooltip);
			button.setLayoutData(GridDataFactory.fillDefaults().hint(39, 39).create());

			option.control = button;
			parent.setLayout(GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).create());

			break;
		case Tree:
			IStatus status = getStatus(option.scenarioProvider, null, true, true, Collections.emptySet());
			option.control = createTreeViewer(parent, option, status);
	
			break;
		default:
			break;
		}

		if (option.choiceData != null && !option.choiceData.enabled && option.choiceData.disabledMessage != null) {
			toolkit.createText(parent, option.choiceData.disabledMessage);
		}
	}

	private Control createLabel(Composite parent, Option option) {
		final Label label = toolkit.createLabel(parent, option.label);
		return label;
	}

	/**
	 * Create a check box button with the given label for the given property on
	 * {@link ParameterModesDialog}
	 * 
	 * @param form
	 * @param label
	 * @param propertyName
	 * @return
	 */
	private Button createCheckBox(final Composite form, final Option option) {
		final Button btn = toolkit.createButton(form, option.label, SWT.CHECK);
		if (!option.enabled) {
			btn.setEnabled(false);
			if (option.choiceData.disabledMessage == null) {
				btn.setToolTipText("Module not licensed");
			}
		}

		if (!option.enabled) {
			btn.setEnabled(false);
			if (option.choiceData.disabledMessage == null) {
				btn.setToolTipText("Module not licensed");
			}
		}

		final EMFUpdateValueStrategy stringToValueStrategy = new EMFUpdateValueStrategy();

		final CompositeValidator v = new CompositeValidator();
		v.addValidators(option.validators);
		stringToValueStrategy.setAfterGetValidator(v);

		final IEMFEditValueProperty prop = EMFEditProperties.value(option.editingDomain, FeaturePath.fromList(option.features));
		final Binding bindValue = dbc.bindValue(WidgetProperties.selection().observe(btn), prop.observe(option.data), stringToValueStrategy, null);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		for (Consumer<IObservableValue<?>> callback : option.validatorCallbacks) {
			callback.accept(prop.observe(option.data));
		}

		return btn;
	}

	/**
	 * Create a +ve integer text box with the given label for the given property on
	 * {@link ParameterModesDialog}
	 * 
	 * @param form
	 * @param label
	 * @param propertyName
	 * @return
	 */
	private Composite createNumberTextBox(final Composite parent, final Option option) {

		final Composite area = toolkit.createComposite(parent, SWT.NONE);
		area.setLayout(new GridLayout(2, false));
		final Label lbl = toolkit.createLabel(area, option.label);
		final Text text = toolkit.createText(area, null, SWT.NONE);
		text.setData(SWTBOT_KEY, option.swtBotId);

		// Create UpdateValueStratgy and assign
		// to the binding
		final EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy();
		if (option.enabled) {
			// Define a validator to check that only numbers are entered
			final IValidator validator = value -> {
				if (value instanceof Integer) {
					if (value.toString().matches(".*\\d.*")) {
						return ValidationStatus.ok();
					}
				}
				return ValidationStatus.error("Not a number");
			};
			strategy.setBeforeSetValidator(validator);
		}

		final CompositeValidator v = new CompositeValidator();
		v.addValidators(option.validators);
		strategy.setAfterGetValidator(v);

		final IEMFEditValueProperty prop = EMFEditProperties.value(option.editingDomain, FeaturePath.fromList(option.features));
		final Binding bindValue = dbc.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(500, text), prop.observe(option.data), strategy, null);

		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		if (!option.enabled) {
			lbl.setEnabled(false);
			text.setEnabled(false);
			text.setToolTipText("Module not licensed");
		}

		for (Consumer<IObservableValue<?>> callback : option.validatorCallbacks) {
			callback.accept(prop.observe(option.data));
		}

		return area;
	}

	private Composite createDateEditor(final Composite parent, final Option option) {
		final Composite area = toolkit.createComposite(parent, SWT.NONE);
		area.setLayout(new GridLayout(2, false));
		area.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		final Label lbl = toolkit.createLabel(area, option.label);
		final DateTimeFormatter format = DateTimeFormatter.ofPattern("d/M/yyyy");

		// Strict parse mode
		final IValidator validator = value -> {
			if (value instanceof String) {
				String valueStr = (String) value;
				if (!valueStr.isEmpty()) {
					try {
						LocalDate.parse((String) value, format);
						return ValidationStatus.ok();
					} catch (final IllegalArgumentException e) {
					}
					return ValidationStatus.error(String.format("'%s' is not a valid date.", value));
				}
			}
			return ValidationStatus.ok();
		};

		final Text text = toolkit.createText(area, null, SWT.NONE);
		text.setData(SWTBOT_KEY, option.swtBotId);
		text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

		final IEMFEditValueProperty prop = EMFEditProperties.value(option.editingDomain, FeaturePath.fromList(option.features));

		final EMFUpdateValueStrategy stringToDateStrategy = new EMFUpdateValueStrategy() {
			@Override
			protected IConverter createConverter(final Object fromType, final Object toType) {
				return new Converter(fromType, toType) {
					@Override
					public Object convert(final Object fromObject) {
						final String value = fromObject == null ? null : fromObject.toString();
						if (value == null || value.isEmpty()) {
							return null;
						}
						try {
							return LocalDate.parse(value, format);
						} catch (final Exception e) {
							return null;
						}
					}
				};
			}
		};

		final EMFUpdateValueStrategy dateToStringStrategy = new EMFUpdateValueStrategy() {
			@Override
			protected IConverter createConverter(final Object fromType, final Object toType) {
				return new Converter(fromType, toType) {
					@Override
					public Object convert(final Object fromObject) {
						if (fromObject instanceof LocalDate) {
							final LocalDate localDate = (LocalDate) fromObject;
							return localDate.format(format);
						}
						return null;
					}
				};
			}
		};

		final CompositeValidator v = new CompositeValidator();
		v.addValidators(option.validators);

		stringToDateStrategy.setAfterGetValidator(validator);
		stringToDateStrategy.setAfterConvertValidator(v);

		final Binding bindValue = dbc.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(500, text), prop.observe(option.data), stringToDateStrategy, dateToStringStrategy);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		if (!option.enabled) {
			lbl.setEnabled(false);
			text.setEnabled(false);
			text.setToolTipText("Module not licensed");
		}
		for (Consumer<IObservableValue<?>> callback : option.validatorCallbacks) {
			callback.accept(prop.observe(option.data));
		}
		return area;

	}

	private Composite createMonthYearEditor(final Composite parent, final Option option) {
		final Composite area = toolkit.createComposite(parent, SWT.NONE);
		area.setLayout(new GridLayout(2, false));
		area.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		final Label lbl = toolkit.createLabel(area, option.label);

		final DateTimeFormatter format = DateTimeFormatter.ofPattern("M/yyyy");

		final IValidator validator = new IValidator() {
			@Override
			public IStatus validate(final Object value) {
				if (value instanceof String) {
					if (value.equals("") == false) {
						try {
							YearMonth.parse((String) value, format);
							return ValidationStatus.ok();
						} catch (final IllegalArgumentException e) {
						}
						return ValidationStatus.error(String.format("'%s' is not a valid date.", value));
					}
				}
				return ValidationStatus.ok();
			}
		};

		final Text text = toolkit.createText(area, null, SWT.NONE);
		text.setData(SWTBOT_KEY, option.swtBotId);
		text.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

		final IEMFEditValueProperty prop = EMFEditProperties.value(option.editingDomain, FeaturePath.fromList(option.features));

		final EMFUpdateValueStrategy stringToDateStrategy = new EMFUpdateValueStrategy() {
			@Override
			protected IConverter createConverter(final Object fromType, final Object toType) {
				return new Converter(fromType, toType) {
					@Override
					public Object convert(final Object fromObject) {
						final String value = fromObject == null ? null : fromObject.toString();
						if (value == null || value.isEmpty()) {
							return null;
						}
						try {
							return YearMonth.parse(value, format);
						} catch (final Exception e) {
							return null;
						}
					}
				};
			}
		};

		final EMFUpdateValueStrategy dateToStringStrategy = new EMFUpdateValueStrategy() {
			@Override
			protected IConverter createConverter(final Object fromType, final Object toType) {
				return new Converter(fromType, toType) {
					@Override
					public Object convert(final Object fromObject) {
						if (fromObject instanceof YearMonth) {
							final YearMonth yearMonth = (YearMonth) fromObject;
							return yearMonth.format(format);
						}
						return null;
					}
				};
			}
		};

		final CompositeValidator v = new CompositeValidator();
		v.addValidators(option.validators);
		stringToDateStrategy.setAfterGetValidator(validator);
		stringToDateStrategy.setAfterConvertValidator(v);
		final Binding bindValue = dbc.bindValue(WidgetProperties.text(SWT.Modify).observeDelayed(500, text), prop.observe(option.data), stringToDateStrategy, dateToStringStrategy);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

		if (!option.enabled) {
			lbl.setEnabled(false);
			text.setEnabled(false);
			text.setToolTipText("Module not licensed");
		}

		for (Consumer<IObservableValue<?>> callback : option.validatorCallbacks) {
			callback.accept(prop.observe(option.data));
		}

		return area;

	}

	private Composite createChoiceEditor(final Composite parent, final Option option) {
		final Composite area = toolkit.createComposite(parent, SWT.NONE);

		final ChoiceData choiceData = option.choiceData;

		area.setLayout(new GridLayout(1 + choiceData.choices.size(), false));
		area.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		final Label lbl = toolkit.createLabel(area, option.label);

		EObject owner = option.data;
		for (int i = 0; i < option.features.length - 1; ++i) {
			owner = (EObject) owner.eGet(option.features[i]);
		}
		final EObject target = owner;
		final EStructuralFeature lastFeature = option.features[option.features.length - 1];

		final CompositeValidator v = new CompositeValidator();
		v.addValidators(option.validators);

		// Create the multi-validator
		final MyValidator validator = new MyValidator(option, v);

		Label mlbl = choiceData.changeHandlers.isEmpty() ? null : toolkit.createLabel(parent, "");

		for (final Pair<String, Object> p : choiceData.choices) {
			final Button btn = toolkit.createButton(area, p.getFirst(), SWT.RADIO);
			if (p.getSecond().equals(target.eGet(lastFeature))) {
				for (BiConsumer<Label, Object> c : choiceData.changeHandlers) {
					c.accept(mlbl, p.getSecond());
				}
				btn.setSelection(true);
			}
			btn.setData(SWTBOT_KEY, option.swtBotId + "." + p.getFirst());

			btn.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(final SelectionEvent e) {
					if (btn.getSelection()) {
						for (BiConsumer<Label, Object> c : choiceData.changeHandlers) {
							c.accept(mlbl, p.getSecond());
						}
						option.editingDomain.getCommandStack().execute(SetCommand.create(option.editingDomain, target, lastFeature, p.getSecond()));
					}
				}
			});

			if (!choiceData.enabled) {
				lbl.setEnabled(false);
				btn.setEnabled(false);
				btn.setToolTipText("Module not licensed");
			}
		}

		dbc.addValidationStatusProvider(validator);

		// ValidationStatusProvider
		ControlDecorationSupport.create(validator, SWT.TOP | SWT.LEFT, area);

		if (choiceData.enabledHook != null) {
			setEnabled(area, choiceData.enabledHook.apply((UserSettings) target));
		}

		return area;

	}
	
	protected IStatus getStatus(final IScenarioDataProvider scenarioDataProvider, @Nullable EObject extraTarget, final boolean optimising, final boolean relaxedValidation, Set<String> extraCategories) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter((constraint, target) -> {
			for (final Category cat : constraint.getCategories()) {
				if (cat.getId().endsWith(".base")) {
					return true;
				} else if (optimising && cat.getId().endsWith(".optimisation")) {
					return true;
				} else if (!optimising && cat.getId().endsWith(".evaluation")) {
					return true;
				}
				// Any extra validation category suffixes to include e.g. for sandbox
				for (String catSuffix : extraCategories) {
					if (cat.getId().endsWith(catSuffix)) {
						return true;
					}
				}
			}

			return false;
		});
		
		final MMXRootObject rootObject = scenarioDataProvider.getTypedScenario(MMXRootObject.class);
		final IStatus status = ServiceHelper.withOptionalService(IValidationService.class, helper -> {
			final DefaultExtraValidationContext extraContext = new DefaultExtraValidationContext(scenarioDataProvider, false, relaxedValidation);
			return helper.runValidation(validator, extraContext, rootObject, extraTarget);
		});
		
		return status;
		
	}
	
	private Composite createTreeViewer(final Composite parent, final Option option, final IStatus status) {
		Composite area = toolkit.createComposite(parent, SWT.NONE);
		
		area.setLayout(new GridLayout(1, false));
		area.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).create());
		
		final TreeViewer viewer = new TreeViewer(area, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.WRAP);

		{
			final GridData gdViewer = new GridData(SWT.FILL, SWT.FILL, true, true);
			// Make text scrolling 
			gdViewer.heightHint = 200;
			gdViewer.widthHint = 450; 
			viewer.getControl().setLayoutData(gdViewer);
		}

		viewer.getTree().setLinesVisible(true);

		viewer.setContentProvider(new GroupedValidationStatusContentProvider());
		viewer.setLabelProvider(new ValidationStatusLabelProvider());
		viewer.setComparator(new ValidationStatusComparator());
		
		viewer.setInput(status);
		viewer.expandAll();
		
		return area;
	}
	

	public OptionGroup createGroup(final DataSection dataSection, final String name) {
		final OptionGroup group = new OptionGroup();
		group.name = name;
		group.dataSection = dataSection;
		return group;
	}

	/**
	 * Adds new elements to the dialog.
	 * 
	 * @param dataSection
	 * @param editingDomian
	 * @param label
	 * @param tooltip
	 * @param data
	 * @param defaultData
	 * @param dataType
	 * @param features
	 */
	public Option addOption(final DataSection dataSection, final OptionGroup group, final EditingDomain editingDomian, final String label, final String tooltip, final EObject data,
			final EObject defaultData, final DataType dataType, String swtBotIdPrefix, final IScenarioDataProvider scenarioProvider, final EStructuralFeature... features) {
		return addOption(dataSection, group, editingDomian, label, tooltip, data, defaultData, dataType, null, swtBotIdPrefix, scenarioProvider, features);
	}

	/**
	 * Adds new elements to the dialog.
	 * 
	 * @param dataSection
	 * @param editingDomian
	 * @param label
	 * @param tooltip
	 * @param data
	 * @param defaultData
	 * @param dataType
	 * @param features
	 */
	public Option addOption(final DataSection dataSection, final OptionGroup group, final EditingDomain editingDomian, final String label, final String tooltip, final EObject data,
			final EObject defaultData, final DataType dataType, final ChoiceData choiceData, String swtBotIdPrefix, final IScenarioDataProvider scenarioDataProvider, final EStructuralFeature... features) {

		if (group != null) {
			if (dataSection != group.dataSection) {
				throw new IllegalArgumentException("Group and option should belong to the same datasection");
			}
		}

		final Option option = new Option(dataSection, group, editingDomian, label, tooltip, data, defaultData, dataType, choiceData, swtBotIdPrefix, scenarioDataProvider, features);
		final List<Option> options;
		if (optionsMap.containsKey(dataSection)) {
			options = optionsMap.get(dataSection);
		} else {
			options = new LinkedList<>();
			optionsMap.put(dataSection, options);
		}
		options.add(option);

		return option;
	}

	public void addValidationCallback(final Option option, final Consumer<IObservableValue<?>> callback) {
		option.validatorCallbacks.add(callback);
	}

	public void addValidation(final Option option, final IValidator validator) {
		option.validators.add(validator);
	}

	private static class CompositeValidator implements IValidator {

		private final List<IValidator> validators = new LinkedList<>();

		public void addValidator(@NonNull final IValidator validator) {
			validators.add(validator);
		}

		public void addValidators(@NonNull final List<IValidator> validators) {
			for (final IValidator validator : validators) {
				if (validator != null) {
					this.validators.add(validator);
				}
			}
		}

		@Override
		public IStatus validate(final Object value) {

			for (final IValidator validator : validators) {
				final IStatus status = validator.validate(value);
				if (!status.isOK()) {
					return status;
				}
			}

			return Status.OK_STATUS;
		}

	}

	/**
	 * A validation status provider that is triggered on any value change. Intended
	 * for use where a data binding is not available (e.g. the choice controls).
	 * Ensure dispose is called. Registering this with the data binding context
	 * should be sufficient.
	 * 
	 * @author Simon Goodall
	 *
	 */
	private static class MyValidator extends MultiValidator {
		@NonNull
		private final Option option;

		@NonNull
		private final IValidator v;

		@Override
		public void dispose() {
			option.data.eAdapters().remove(adapter);
			super.dispose();
		}

		private final @NonNull EContentAdapter adapter = new EContentAdapter() {
			@Override
			public void notifyChanged(final Notification notification) {
				super.notifyChanged(notification);
				if (option.choiceData.enabledHook != null) {
					setEnabled(option.control, option.choiceData.enabledHook.apply((UserSettings) notification.getNotifier()));
				}
				revalidate();
			}
		};

		public MyValidator(@NonNull final Option option, @NonNull final IValidator v) {
			this.option = option;
			this.v = v;
			option.data.eAdapters().add(adapter);
		}

		@Override
		protected IStatus validate() {
			return v.validate(option.data);
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addValidationStatusProvider(ValidationStatusProvider validator) {
		this.extraValidators.add(validator);

	}

	public void addNameOption(String nameSuggestion, Set<String> existingNames) {
		this.showName = true;
		this.nameSuggestion = nameSuggestion;
		this.existingNames = existingNames;

	}

	public String getNameSuggestion() {
		return nameSuggestion;
	}

	private static void setEnabled(Control c, boolean enabled) {
		c.setEnabled(enabled);
		if (c instanceof Composite) {
			Composite composite = (Composite) c;
			for (Control c2 : composite.getChildren()) {
				setEnabled(c2, enabled);
			}
		}
	}
}
