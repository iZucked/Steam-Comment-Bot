/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.mmxlabs.models.lng.scenario.actions.RollForwardEngine.RollForwardDescriptor;
import com.mmxlabs.models.lng.scenario.actions.impl.UpdateDateChange;
import com.mmxlabs.models.lng.scenario.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class RollForwardDialog extends FormDialog {

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
	
	private Map<String, Button> persistenceStringsForCheckboxes = new HashMap<>();
	private Map<DateTime, Period> relativeTimeFieldPeriods = new HashMap<>();

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
	 * Recalculates all the fields that have been designated to be computed automatically from the prompt start date.
	 */
	protected void recalculateRelativeDateFields() {
		for (DateTime field: relativeTimeFieldPeriods.keySet()) {
			LocalDate newDate = getLocalDateFromDateTimeField(promptStart).plus(relativeTimeFieldPeriods.get(field));
			setDateTimeFieldFromLocalDate(field, newDate);
		}
	}
	
	/**
	 * Sets the value of a DateTime GUI control from a LocalDate object
	 * @param field
	 * @param date
	 */
	protected void setDateTimeFieldFromLocalDate(DateTime field, LocalDate date) 
	{
		field.setSeconds(0);
		field.setMinutes(0);
		field.setHours(0);

		field.setYear(date.getYear());
		field.setMonth(date.getMonthValue() - 1);
		field.setDay(date.getDayOfMonth());
	}
	
	/**
	 * Get the value of a DateTime GUI control as a LocalDate object
	 * @param field
	 * @return
	 */
	protected LocalDate getLocalDateFromDateTimeField(DateTime field) 
	{
		return LocalDate.of(field.getYear(), 1 + field.getMonth(), field.getDay());

	}
	
	/**
	 * Returns an English representation of a Period object in years, months and days.
	 * @param period
	 * @return
	 */
	protected String periodToNiceString(Period period) 
	{
		String result = "";
		
		int years = period.getYears();
		int months = period.getMonths();
		int days = period.getDays();
		
		if (years > 0) {
			result += years + " year";
			if (years > 1) {
				result += "s";
			}
		}

		if (months > 0) {
			if (result != "") {
				result += ", ";
			}
			result += months + " month";
			if (months > 1) {
				result += "s";
			}
		}

		if (days > 0) {
			if (result != "") {
				result += ", ";
			}
			result += days + " day";
			if (days > 1) {
				result += "s";
			}
		}
		
		if (result == "") {
			result = "0 days";
		}
		
		return result;
		
	}

	/**
	 * If the specified checkbox is set, updates the specified DateTime control to be a certain period of time after the prompt start date.
	 * @param checkbox
	 * @param dateField
	 * @param amount
	 */
	protected void updateFieldIfCheckboxSet(Button checkbox, DateTime dateField, Period amount) 
	{
		boolean selected = checkbox.getSelection();
		
		dateField.setEnabled(!selected);
		if (selected) {
			relativeTimeFieldPeriods.put(dateField, amount);
			recalculateRelativeDateFields();
		}
		else {
			relativeTimeFieldPeriods.remove(dateField);
		}
		
	}
	
	/**
	 * Creates a form label, a matching date control, and a checkbox that determines whether the date control is derived from the prompt start.
	 * 
	 * @return A {@link List} of the controls created.
	 */
	protected List<Control> createRelativeDateControls(IManagedForm mform, Composite parent, String label, String id, LocalDate date, Period amount)
	{
		List<Control> result = createLabelAndDateControl(mform, parent, label, date);
		DateTime dateField = (DateTime) result.get(1);

		String relativeTimeLabel = "Prompt start plus " + periodToNiceString(amount);
		
		String persistenceKey = id + DIALOG_RELATIVE_CHECKBOX_SUFFIX;
		
		Button checkbox = mform.getToolkit().createButton(parent, relativeTimeLabel, SWT.CHECK);
		result.add(checkbox);
		persistenceStringsForCheckboxes.put(persistenceKey, checkbox);				
		
		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFieldIfCheckboxSet(checkbox, dateField, amount);
				refreshPreview();
			}			
		};		
		
		checkbox.addSelectionListener(listener);
		
		getBooleanValue(persistenceKey, checkbox);

		// manually trigger the checkbox update logic
		updateFieldIfCheckboxSet(checkbox, dateField, amount);
		
		return result;
	}
	
	/**
	 * Creates a form label and a matching date control, initialised with the specified values.
	 * 
	 * @param mform
	 * @param parent
	 * @param label
	 * @param date
	 * @return A {@code List} of the controls created.
	 */
	protected List<Control> createLabelAndDateControl(IManagedForm mform, Composite parent, String label, LocalDate date)
	{
		ArrayList<Control> result = new ArrayList<Control>();
		result.add( mform.getToolkit().createLabel(parent, label) );

		DateTime dateField = new DateTime(parent, SWT.DATE);
		result.add( dateField );
		
		dateField.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				recalculateRelativeDateFields();
				refreshPreview();
			}
			
		});


		mform.getToolkit().adapt(dateField, true, true);
		

		if (date != null) {
			setDateTimeFieldFromLocalDate(dateField, date);
		}
		
		return result;
	}

	/**
	 * Creates the controls for the roll forward dialog.
	 */
	@Override
	protected void createFormContent(final IManagedForm mform) {

		super.createFormContent(mform);
		mform.getForm().setText("Roll Forward");

		mform.getForm().getBody().setLayout(new GridLayout());

		// Dates section.
		{
			final Group datesGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			datesGroup.setLayout(new GridLayout(3, false));
			datesGroup.setText("Dates to update");
			mform.getToolkit().adapt(datesGroup);

			promptStart = (DateTime) createLabelAndDateControl(mform, datesGroup, "Prompt Start", null).get(1);
			
			mform.getToolkit().createLabel(datesGroup, ""); // empty label for empty grid cell

			promptEnd = (DateTime) createRelativeDateControls(mform, datesGroup, "Prompt End", DIALOG_PROMPT_END_NAME, LocalDate.now().plusDays(90), Period.of(0, 0, 90)).get(1);

			if (scenarioModel.getSchedulingEndDate() != null) {
				horizon = (DateTime) createLabelAndDateControl(mform, datesGroup, "Horizon", scenarioModel.getSchedulingEndDate()).get(1);
				mform.getToolkit().createLabel(datesGroup, ""); // empty label for empty grid cell
			}			
			
			CharterOutMarketParameters charterDates = ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterOutMarketParameters();
			
			if (charterDates.getCharterOutStartDate() != null) {
				charterOutStart = (DateTime) createRelativeDateControls(mform, datesGroup, "Charter start date", DIALOG_CHARTER_START_NAME, charterDates.getCharterOutStartDate(), 
						Period.of(0, 1, 0)).get(1);				
				
			}
			
			if (charterDates.getCharterOutEndDate() != null) {
				charterOutEnd = (DateTime) createLabelAndDateControl(mform, datesGroup, "Charter end date", charterDates.getCharterOutEndDate()).get(1);
				mform.getToolkit().createLabel(datesGroup, ""); // empty label for empty grid cell
			}

		}
		// Input Section
		{
			final Group inputGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			inputGroup.setLayout(new GridLayout(3, false));
			inputGroup.setText("Input");
			mform.getToolkit().adapt(inputGroup);

			// Freeze Date
			{
				List<Control> controls = createRelativeDateControls(mform, inputGroup, "Freeze before", DIALOG_FREEZE_DATE_NAME, LocalDate.now().plusMonths(1), Period.of(0,  1,  0) );
				freezeDate = (DateTime) controls.get(1);
				Control lbl = controls.get(0);
				
				lbl.setToolTipText(TOOLTIP_FREEZE_DATE);
				freezeDate.setToolTipText(TOOLTIP_FREEZE_DATE);
				
				// option to update slot windows
				updateSlotWindowCheckbox = mform.getToolkit().createButton(inputGroup, "Update historical slot and vessel event windows", SWT.CHECK);
				// mark the checkbox for persistence
				persistenceStringsForCheckboxes.put(DIALOG_UPDATE_WINDOWS_NAME, updateSlotWindowCheckbox);
				getBooleanValue(DIALOG_UPDATE_WINDOWS_NAME, updateSlotWindowCheckbox);

				updateSlotWindowCheckbox.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						refreshPreview();						
					}
					
				});
				updateSlotWindowCheckbox.setToolTipText(TOOLTIP_UPDATE_WINDOWS);				
				
			}

		}
		// Preview Section
		{
			final Group previewGroup = new Group(mform.getForm().getBody(), SWT.NONE);
			previewGroup.setLayout(new FillLayout());
			// Table will fill dialog area, but limit initial height
			final GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.heightHint = 100;
			previewGroup.setLayoutData(layoutData);

			previewGroup.setText("Preview of changes");
			mform.getToolkit().adapt(previewGroup);

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
	}

	/**
	 * Creates a list of {@link IRollForwardChange} objects representing the changes to the relevant date fields in the data model.
	 * @return A list of {@link IRollForwardChange} objects (that will all be {@link UpdateDateChange} instances).
	 */
	private List<IRollForwardChange> generateDateChanges() {
		List<IRollForwardChange> result = new LinkedList<>();
		
		EAttribute promptStartFeature = LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart();
		EAttribute promptEndFeature = LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd();
	
		// update the prompt start
		result.add(new UpdateDateChange("Prompt Start", scenarioModel, promptStartFeature, getLocalDateFromDateTimeField(promptStart), domain));
		// update the prompt end
		result.add(new UpdateDateChange("Prompt End", scenarioModel, promptEndFeature, getLocalDateFromDateTimeField(promptEnd), domain));
	
		// update the horizon, if set
		if (horizon != null) {			
			result.add(new UpdateDateChange("Horizon", scenarioModel, LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(), getLocalDateFromDateTimeField(horizon), domain));
		}
		
		// update the charter in and charter out dates, if set
		CharterOutMarketParameters charterDates = ScenarioModelUtil.getSpotMarketsModel(scenarioModel).getCharterOutMarketParameters();

		if (charterOutStart != null) {			
			result.add(new UpdateDateChange("Charter Out Start", charterDates, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_START_DATE, getLocalDateFromDateTimeField(charterOutStart), domain));
		}

		if (charterOutEnd != null) {			
			result.add(new UpdateDateChange("Charter Out End", charterDates, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET_PARAMETERS__CHARTER_OUT_END_DATE, getLocalDateFromDateTimeField(charterOutEnd), domain));
		}

		return result ;
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

		/*
		// Store dialog settings
		setBooleanValue(DIALOG_REMOVE_SLOT_EVENT_ENABLE, removeDateCheckBox);
		setBooleanValue(DIALOG_REMOVE_VESSELS_ENABLED, removeVesselsCheckBox);
		setBooleanValue(DIALOG_UPDATE_VESSELS_ENABLED, updateVesselsCheckBox);
		*/
		
		for (Entry<String, Button> entry: persistenceStringsForCheckboxes.entrySet()) {
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
