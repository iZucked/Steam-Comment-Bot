/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.scenario.actions.RollForwardEngine.RollForwardDescriptor;
import com.mmxlabs.models.lng.scenario.actions.impl.UpdateDateChange;
import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class RollForwardDialog extends Dialog {

	private static final String DIALOG_SECTION = "com.mmxlabs.models.lng.scenario.action.RollForwardDialog";

	// Following fields are used to persist checkbox settings
	private static final String DIALOG_RELATIVE_CHECKBOX_SUFFIX = "_relative_checkbox_checked";
	private static final String DIALOG_PROMPT_END_NAME = "prompt_end";
	private static final String DIALOG_CHARTER_START_NAME = "charter_start";
	private static final String DIALOG_FREEZE_DATE_NAME = "freeze_date";
	private static final String DIALOG_UPDATE_WINDOWS_NAME = "update_slot_windows_checked";

	private static final String TOOLTIP_FREEZE_DATE = "Cargoes and Events before or over this date will be frozen. Vessel assignment will be fixed, pairings will be fixed and arrival window will be set to currently scheduled arrival time.";
	private static final String TOOLTIP_UPDATE_WINDOWS = "Update the arrival window for slots and vessel events that occur before the freeze date?";

	private final EditingDomain domain;
	private final LNGScenarioModel scenarioModel;

	private GridTableViewer previewViewer;
	private DateTime freezeDate;

	// form controls that are used to drive the roll forward logic
	private DateTime promptStart;
	private DateTime promptEnd;
	private DateTime horizon = null;
	private DateTime charterOutStart = null;
	private DateTime charterOutEnd = null;
	private Button updateSlotWindowCheckbox;

	private List<IRollForwardChange> changes;

	private final Map<String, Button> persistenceStringsForCheckboxes = new HashMap<>();
	private final Map<DateTime, Period> relativeTimeFieldPeriods = new HashMap<>();

	public RollForwardDialog(final Shell shell, final IScenarioDataProvider scenarioDataProvider) {
		super(shell);
		this.domain = scenarioDataProvider.getEditingDomain();
		this.scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
	}

	public RollForwardDialog(final IShellProvider parentShellProvider, final IScenarioDataProvider scenarioDataProvider) {
		super(parentShellProvider);
		this.domain = scenarioDataProvider.getEditingDomain();
		this.scenarioModel = scenarioDataProvider.getTypedScenario(LNGScenarioModel.class);
	}

	/**
	 * Recalculates all the fields that have been designated to be computed
	 * automatically from the prompt start date.
	 */
	protected void recalculateRelativeDateFields() {
		for (final DateTime field : relativeTimeFieldPeriods.keySet()) {
			final LocalDate newDate = getLocalDateFromDateTimeField(promptStart).plus(relativeTimeFieldPeriods.get(field));
			setDateTimeFieldFromLocalDate(field, newDate);
		}
	}

	/**
	 * Sets the value of a DateTime GUI control from a LocalDate object
	 * 
	 * @param field
	 * @param date
	 */
	protected void setDateTimeFieldFromLocalDate(final DateTime field, final LocalDate date) {
		field.setSeconds(0);
		field.setMinutes(0);
		field.setHours(0);

		field.setYear(date.getYear());
		field.setMonth(date.getMonthValue() - 1);
		field.setDay(date.getDayOfMonth());
	}

	/**
	 * Get the value of a DateTime GUI control as a LocalDate object
	 * 
	 * @param field
	 * @return
	 */
	protected LocalDate getLocalDateFromDateTimeField(final DateTime field) {
		return LocalDate.of(field.getYear(), 1 + field.getMonth(), field.getDay());

	}

	/**
	 * Returns an English representation of a Period object in years, months and
	 * days.
	 * 
	 * @param period
	 * @return
	 */
	protected String periodToNiceString(final Period period) {
		String result = "";

		final int years = period.getYears();
		final int months = period.getMonths();
		final int days = period.getDays();

		if (years > 0) {
			result += years + "y";
		}

		if (months > 0) {
			if (!result.isBlank()) {
				result += ", ";
			}
			result += months + "m";
		}

		if (days > 0) {
			if (!result.isBlank()) {
				result += ", ";
			}
			result += days + "d";

		}

		if (result.isBlank()) {
			result = "0d";
		}

		return result;

	}

	/**
	 * If the specified checkbox is set, updates the specified DateTime control to
	 * be a certain period of time after the prompt start date.
	 * 
	 * @param checkbox
	 * @param dateField
	 * @param amount
	 */
	protected void updateFieldIfCheckboxSet(final Button checkbox, final DateTime dateField, final Period amount) {
		final boolean selected = checkbox.getSelection();

		dateField.setEnabled(!selected);
		if (selected) {
			relativeTimeFieldPeriods.put(dateField, amount);
			recalculateRelativeDateFields();
		} else {
			relativeTimeFieldPeriods.remove(dateField);
		}

	}

	/**
	 * Creates a form label and a matching date control, initialised with the
	 * specified values.
	 * 
	 * @param mform
	 * @param parent
	 * @param label
	 * @param date
	 * @return A {@code List} of the controls created.
	 */
	protected List<Control> createLabelAndDateControl(final FormToolkit toolkit, final Composite parent, final String label, final @Nullable LocalDate date) {
		final ArrayList<Control> result = new ArrayList<>();
		result.add(toolkit.createLabel(parent, label));

		final DateTime dateField = new DateTime(parent, SWT.DATE);
		result.add(dateField);

		dateField.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				recalculateRelativeDateFields();
				refreshPreview();
			}

		});

		toolkit.adapt(dateField, true, true);

		if (date != null) {
			setDateTimeFieldFromLocalDate(dateField, date);
		}

		return result;
	}

	@Override
	protected void configureShell(final Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Roll Forward");
		newShell.setImage(CommonImages.getImage(IconPaths.RollForward_24, IconMode.Enabled));

	}

	/**
	 * Creates the controls for the roll forward dialog.
	 */
	@Override
	protected Control createDialogArea(final Composite pparent) {

		final Composite parent = (Composite) super.createDialogArea(pparent);// new Composite(pparent, SWT.NONE);

		final FormToolkit toolkit = new FormToolkit(getShell().getDisplay());
		toolkit.adapt(parent);
		parent.addDisposeListener(e -> toolkit.dispose());

		// Dates section.
		{
			final Group datesGroup = new Group(parent, SWT.NONE);
			datesGroup.setLayout(new GridLayout(5, false));
			datesGroup.setText("Dates to update");
			toolkit.adapt(datesGroup);
			final Composite row = datesGroup;

			// Prompt row
			{

				final Period increment = Period.of(0, 0, 90);

				final List<Control> startControls = createLabelAndDateControl(toolkit, row, "Prompt Start", null);
				promptStart = (DateTime) startControls.get(1);

				final List<Control> endControls = createLabelAndDateControl(toolkit, row, "to", LocalDate.now().plus(increment));
				promptEnd = (DateTime) endControls.get(1);

				final Button btn = new Button(row, SWT.PUSH);
				btn.setText(periodToNiceString(increment));
				btn.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final LocalDate newDate = getLocalDateFromDateTimeField(promptStart).plus(increment);
						setDateTimeFieldFromLocalDate((DateTime) endControls.get(1), newDate);
						refreshPreview();
					}
				});
			}
			// Horizon row
			if (scenarioModel.getSchedulingEndDate() != null) {
				final List<Control> controls = createLabelAndDateControl(toolkit, row, "Horizon", scenarioModel.getSchedulingEndDate());
				horizon = (DateTime) controls.get(1);

				final Label spacer = new Label(row, SWT.None);
				spacer.setLayoutData(GridDataFactory.swtDefaults().span(3, 1).create());
			}
			// Charter out row

			final CharterOutMarketParameters charterDates = ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterOutMarketParameters();
			if (charterDates.getCharterOutStartDate() != null || charterDates.getCharterOutEndDate() != null) {
				{
					if (charterDates.getCharterOutStartDate() != null) {
						final List<Control> controls = createLabelAndDateControl(toolkit, row, "Charter out", charterDates.getCharterOutStartDate());
						charterOutStart = (DateTime) controls.get(1);
					}

					if (charterDates.getCharterOutEndDate() != null) {
						final List<Control> controls = createLabelAndDateControl(toolkit, row, "to", charterDates.getCharterOutEndDate());
						charterOutEnd = (DateTime) controls.get(1);
					}
					final Label spacer = new Label(row, SWT.None);

				}

				if (charterOutStart != null) {
					final Period increment = Period.of(0, 1, 0);

					toolkit.createLabel(row, ""); // empty label for empty grid cell

					final Button btn = new Button(row, SWT.PUSH);
					btn.setText("Prompt +" + periodToNiceString(increment));
					btn.addSelectionListener(new SelectionAdapter() {

						@Override
						public void widgetSelected(final SelectionEvent e) {
							final LocalDate newDate = getLocalDateFromDateTimeField(charterOutStart).plus(increment);
							setDateTimeFieldFromLocalDate(charterOutStart, newDate);
							refreshPreview();
						}
					});
				}

			}
		}
		// Input Section
		{
			final Group inputGroup = new Group(parent, SWT.NONE);
			inputGroup.setLayout(new GridLayout(3, false));
			inputGroup.setText("Input");
			toolkit.adapt(inputGroup);

			// Freeze Date
			{
				final Period increment = Period.of(0, 1, 0);
				final List<Control> controls = createLabelAndDateControl(toolkit, inputGroup, "Archive before", LocalDate.now().plus(increment));

				final Button btn = new Button(inputGroup, SWT.PUSH);
				btn.setText(periodToNiceString(increment));
				btn.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(final SelectionEvent e) {
						final LocalDate newDate = getLocalDateFromDateTimeField(promptStart).plus(increment);
						setDateTimeFieldFromLocalDate((DateTime) controls.get(1), newDate);
						refreshPreview();
					}
				});

				freezeDate = (DateTime) controls.get(1);
				final Control lbl = controls.get(0);

				lbl.setToolTipText(TOOLTIP_FREEZE_DATE);
				freezeDate.setToolTipText(TOOLTIP_FREEZE_DATE);

				// option to update slot windows
				updateSlotWindowCheckbox = toolkit.createButton(inputGroup, "Update historical windows", SWT.CHECK);
				// mark the checkbox for persistence
				persistenceStringsForCheckboxes.put(DIALOG_UPDATE_WINDOWS_NAME, updateSlotWindowCheckbox);
				getBooleanValue(DIALOG_UPDATE_WINDOWS_NAME, updateSlotWindowCheckbox);

				updateSlotWindowCheckbox.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(final SelectionEvent e) {
						refreshPreview();
					}

				});
				updateSlotWindowCheckbox.setToolTipText(TOOLTIP_UPDATE_WINDOWS);

			}

		}
		// Preview Section
		{
			final Group previewGroup = new Group(parent, SWT.NONE);
			previewGroup.setLayout(new FillLayout());
			// Table will fill dialog area, but limit initial height
			final GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.heightHint = 100;
			previewGroup.setLayoutData(layoutData);

			previewGroup.setText("Preview of changes");
			toolkit.adapt(previewGroup);

			previewViewer = new GridTableViewer(previewGroup, SWT.H_SCROLL | SWT.V_SCROLL);
			previewViewer.setContentProvider(new ArrayContentProvider());

			final GridViewerColumn col = new GridViewerColumn(previewViewer, SWT.NONE);
			col.getColumn().setWidth(300);

			previewViewer.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(final Object element) {
					if (element instanceof IRollForwardChange) {
						final IRollForwardChange change = (IRollForwardChange) element;
						return change.getMessage();
					}

					return super.getText(element);
				}
			});

		}
		refreshPreview();

		return pparent;
	}

	/**
	 * Creates a list of {@link IRollForwardChange} objects representing the changes
	 * to the relevant date fields in the data model.
	 * 
	 * @return A list of {@link IRollForwardChange} objects (that will all be
	 *         {@link UpdateDateChange} instances).
	 */
	private List<IRollForwardChange> generateDateChanges() {
		final List<IRollForwardChange> result = new LinkedList<>();

		final EAttribute promptStartFeature = LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart();
		final EAttribute promptEndFeature = LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd();

		// update the prompt start
		result.add(new UpdateDateChange("Prompt start", scenarioModel, promptStartFeature, getLocalDateFromDateTimeField(promptStart), domain));
		// update the prompt end
		result.add(new UpdateDateChange("Prompt end", scenarioModel, promptEndFeature, getLocalDateFromDateTimeField(promptEnd), domain));

		// update the horizon, if set
		if (horizon != null) {
			result.add(new UpdateDateChange("Horizon", scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), getLocalDateFromDateTimeField(horizon), domain));
		}

		// update the charter in and charter out dates, if set
		final CharterOutMarketParameters charterDates = ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterOutMarketParameters();

		if (charterOutStart != null) {
			result.add(new UpdateDateChange("Charter out start", charterDates, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE,
					getLocalDateFromDateTimeField(charterOutStart), domain));
		}

		if (charterOutEnd != null) {
			result.add(new UpdateDateChange("Charter out end", charterDates, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE,
					getLocalDateFromDateTimeField(charterOutEnd), domain));
		}

		return result;
	}

	private void refreshPreview() {

		final RollForwardEngine engine = new RollForwardEngine();

		final RollForwardDescriptor descriptor = engine.new RollForwardDescriptor();
		{
			final LocalDate dt = LocalDate.of(freezeDate.getYear(), 1 + freezeDate.getMonth(), freezeDate.getDay());
			descriptor.put(RollForwardDescriptor.FreezeDate, dt);
			descriptor.put(RollForwardDescriptor.UpdateWindows, updateSlotWindowCheckbox.getSelection());
		}

		final Button button = getButton(IDialogConstants.OK_ID);
		try {
			final LocalDate dt = LocalDate.of(freezeDate.getYear(), 1 + freezeDate.getMonth(), freezeDate.getDay());
			changes = generateDateChanges();
			changes = engine.generateChanges(descriptor, domain, scenarioModel, dt, changes);

			previewViewer.setInput(changes);
			if (button != null) {
				button.setEnabled(true);
			}
		} catch (final Exception e) {
			previewViewer.setInput(new String[] { e.getMessage() });
			if (button != null) {
				button.setEnabled(false);
			}
		}

	}

	@Override
	protected void okPressed() {
		BusyIndicator.showWhile(PlatformUI.getWorkbench().getDisplay(), new Runnable() {

			@Override
			public void run() {
				// Apply changes
				final CompoundCommand cmd = new CompoundCommand("Roll Forward");
				for (final IRollForwardChange change : changes) {
					cmd.append(change.getCommand());
				}
				if (cmd.canExecute()) {
					domain.getCommandStack().execute(cmd);
				} else {
					throw new RuntimeException("Unable to execute command");
				}
			}
		});

		for (final Entry<String, Button> entry : persistenceStringsForCheckboxes.entrySet()) {
			setBooleanValue(entry.getKey(), entry.getValue());
		}

		super.okPressed();
	}

	private void getBooleanValue(final String key, final Button checkBox) {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		if (dialogSettings != null) {
			final IDialogSettings section = dialogSettings.getSection(DIALOG_SECTION);
			if (section != null) {
				final boolean value = section.getBoolean(key);
				checkBox.setSelection(value);
			}
		}
	}

	private void setBooleanValue(final String key, final Button checkBox) {
		final IDialogSettings dialogSettings = Activator.getDefault().getDialogSettings();
		if (dialogSettings != null) {
			final IDialogSettings section = DialogSettings.getOrCreateSection(dialogSettings, DIALOG_SECTION);
			section.put(key, checkBox.getSelection());
		}
	}
}
